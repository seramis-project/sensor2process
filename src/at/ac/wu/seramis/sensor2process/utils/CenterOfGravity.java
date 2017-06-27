package at.ac.wu.seramis.sensor2process.utils;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.factory.TagFactory;
import at.ac.wu.seramis.sensor2process.cep.model.events.factory.TemporalPositionFactory;
import at.ac.wu.seramis.sensor2process.database.model.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CenterOfGravity
{
	// 20/40, 10/20 also fine but this one more refined

	private static final int WINDOW_SIZE = 30000;
	private static final int STEP_SIZE = 15000;
	private static final double DECAY = 0.95;

	private static final long MAX_SECONDS_INFLUENCE = 300;  // events older than 5 minutes don't have any influence anymore

	// Only reads method
	public static <P extends TemporalPosition> P getCenterOfGravity(Iterable<TemporalSenseEvent> reads, TemporalPositionFactory<P> factory)
	{
		return getCenterOfGravityWithPrevious(reads, null, factory);
	}
	public static <P extends TemporalPosition> P getCenterOfGravityWithPrevious(Iterable<TemporalSenseEvent> reads, TemporalPosition lastSeenPos, TemporalPositionFactory<P> factory)
	{
		double lastEventInfluence = getLastEventInfluence(reads, lastSeenPos);

		double rssiSum = 0;
		TemporalSenseEvent tse = reads.iterator().next();
		String epc = tse.getId();
		TemporalSenseEvent lastTse = null;

		for (TemporalSenseEvent read : reads)
		{
			rssiSum += rssiFactor(read.getSignalStrength());
			lastTse = read;
		}
		long timestamp = lastTse != tse ? lastTse.getTimestamp() / 2 + tse.getTimestamp() / 2 : tse.getTimestamp();  // use avg of last and first read in the window.

		double posX = 0;
		double posY = 0;
		double posZ = 0;

		for (TemporalSenseEvent read : reads)
		{
			double factor = rssiFactor(read.getSignalStrength());
			posX += (factor / rssiSum) * read.getX();
			posY += (factor / rssiSum) * read.getY();
			posZ += (factor / rssiSum) * read.getZ();
		}
		if (lastEventInfluence > 0){
			posX = (1-lastEventInfluence)*posX + lastEventInfluence * lastSeenPos.getX();
			posY = (1-lastEventInfluence)*posY + lastEventInfluence * lastSeenPos.getY();
			posZ = (1-lastEventInfluence)*posZ + lastEventInfluence * lastSeenPos.getZ();
		}

		P positionEvent = factory.create();
		positionEvent.setId(epc);
		positionEvent.setX(posX);
		positionEvent.setY(posY);
		positionEvent.setZ(posZ);
		positionEvent.setTimestamp(timestamp);

		return positionEvent;
	}

	public static ArrayList<TemporalPosition> getWindowedTags(ArrayList<TemporalSenseEvent> readsList)
	{

		int i = 0;

		ArrayList<TemporalPosition> tagsList = new ArrayList<>();

		Double x = readsList.get(i).getX();
		Double y = readsList.get(i).getY();
		Date date = new Date(readsList.get(i).getTimestamp());
		long time_millis = readsList.get(i).getTimestamp();
		long limit = readsList.get(readsList.size() - 1).getTimestamp();

		long window_init = 0;
		long window_limit = 0;

		window_init = time_millis;

		window_limit = window_init + WINDOW_SIZE;

		Tag previousTag = new Tag();

		while (window_limit < limit)
		{

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(window_init);
			Date windowInitDate = cal.getTime();
			cal.setTimeInMillis(window_limit);
			Date windowLimitDate = cal.getTime();

			// System.out.println("Window number " + i + "\n Between " +
			// windowInitDate + " and " + windowLimitDate);
			ArrayList<TemporalSenseEvent> windowReads = new ArrayList<>();

			for (int j = 0; j < readsList.size(); j++)
			{

				if (readsList.get(j).getTimestamp() < window_limit && readsList.get(j).getTimestamp() >= window_init)
				{

					cal.setTimeInMillis(readsList.get(j).getTimestamp());
					Date readDate = cal.getTime();
					// System.out.println(readDate + " X:" +
					// readsList.get(j).getSensor().getPosX() + " Y:" +
					// readsList.get(j).getSensor().getPosY());

					windowReads.add(readsList.get(j));
					// System.out.println(listReads.get(j).getTime_millis());

				}

			}
			// Tag tag = CenterOfGravity.getCenterOfGravity(windowReads,
			// previousTag);
			Tag tag = CenterOfGravity.getCenterOfGravityWithPrevious(windowReads, previousTag, new TagFactory());

			// System.out.println(tag.toString());
			i++;
			window_init = window_init + STEP_SIZE;
			window_limit = window_limit + STEP_SIZE;
			if (tag.getX() != 0 && tag.getY() != 0)
			{
				tagsList.add(tag);
			}

			previousTag.setRssi(tag.getRssi());
			previousTag.setX(tag.getX());
			previousTag.setY(tag.getY());

		}

		return tagsList;

	}

	private static double getLastEventInfluence(Iterable<TemporalSenseEvent> reads, TemporalPosition lastSeenPos) {
		if (lastSeenPos == null){
			return 0;
		}
		long readTime = reads.iterator().next().getTimestamp();
		long lastReadTime = lastSeenPos.getTimestamp();
		double secondsPassed = (readTime-lastReadTime) / 1000;
		double influence = 0;

		if (secondsPassed > MAX_SECONDS_INFLUENCE){
			// more than five minutes passed:
		} else {
			influence = 0.5 * (MAX_SECONDS_INFLUENCE - secondsPassed) / MAX_SECONDS_INFLUENCE; // linear influence up to a half for very recent last positions
		}
		return influence;
	}


	private static double rssiFactor(double rssi)
	{
		rssi = rssi + 60;

		double r1 = Math.pow(Math.E, rssi);
		// r1 += 50;
		double r2 = Math.pow(r1, 1.0 / 3);

		return r2;

		// return Math.pow(Math.pow(Math.E, rssi), 1/3);
	}
}
