package at.ac.wu.seramis.sensor2process.cep;

import java.sql.SQLException;
import java.util.ArrayList;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingConservativePattern;
import at.ac.wu.seramis.sensor2process.database.DataAccess;
import at.ac.wu.seramis.sensor2process.database.Database;
import at.ac.wu.seramis.sensor2process.database.model.Actor;
import at.ac.wu.seramis.sensor2process.utils.CenterOfGravity;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.utils.BPMNAnnotationParser;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CepTest
{

	public static void main(String[] args) throws SQLException, Exception
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		// Get Actors

		ArrayList<String> bpmnAnnotations = BPMNAnnotationParser.getBPMNAnnotations(CepTest.class.getResourceAsStream("/rfidanalysis/resources/test.bpmn"));

		for (String annotation : bpmnAnnotations)
		{
			System.out.println(annotation);
		}

		Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
		ArrayList<Actor> actorsList = DataAccess.getActors("readings_day");
		ArrayList<TemporalPosition> tagsListFull = new ArrayList<>();

		// Test with only 100 rows

		ArrayList<Actor> actorsListTest = new ArrayList<>(actorsList.subList(0, 100));

		// for (Actor selectedActor : testActorsList)

		// long startTime = System.nanoTime();

		for (Actor selectedActor : actorsListTest)
		{
			DataAccess sql = new DataAccess();

			Actor actor = sql.getActor(selectedActor.toString(), "readings_day");// dao.getActor("urn:epc:tag:gid-96:34860249.20113.100001");
			ArrayList<TemporalSenseEvent> readsList = actor.getReads();
			ArrayList<TemporalPosition> tagsList = CenterOfGravity.getWindowedTags(readsList);
			tagsListFull.addAll(tagsList);

			// System.out.println("Actor: " + actor.toString());

		}

		DataStream<TemporalPosition> tagStream = env.fromCollection(tagsListFull);

		// Test for movement

		DataStream<IBusinessEvent> trivialMovingEvents = CEPBusinessPosition.toMovementStream(tagStream, new MovingTrivialPattern());

		trivialMovingEvents.print();

		DataStream<? extends IBusinessEvent> conservativeMovingEvents = CEPBusinessPosition.toFlatBusinessEventsStream(trivialMovingEvents, new MovingConservativePattern());

		conservativeMovingEvents.print();

		// DataStream<IBusinessEvent> startsMovingEvents =
		// CEPBusinessPosition.toStartsMoving(tagStream, new
		// StartsMovingPattern());
		//
		// startsMovingEvents.print();
		//
		// DataStream<IBusinessEvent> stopsMovingEvents =
		// CEPBusinessPosition.toStopsMoving(tagStream, new
		// StopsMovingPattern());

		// stopsMovingEvents.print();

		// Finally execute the Stream:
		env.execute("Example Business Events");

		// DataStream<Tag> tagStream = env.fromCollection(tagsListFull);
		// DataStream<TestWarning> warnings = toWarningStream(tagStream, new
		// TestWarningPattern());
		// DataStream<Tag> tagStream = tagsList;

		// warnings.print();

		// Finally execute the Stream:
		// env.execute("TestCEP");
		// ForeachActor get tags list
		// For each tag list code the event processor? Stream?

		// long endTime = System.nanoTime();
		// // root.print("", true, listPairs);
		// long elapsedTime = endTime - startTime;
		// double seconds = elapsedTime / 1000000000.0;
		//
		// System.out.println("Took " + (seconds) + "s");
	}

}