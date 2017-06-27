package at.ac.wu.seramis.sensor2process.database.source;

import at.ac.wu.seramis.sensor2process.cep.model.events.SenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.database.DataAccess;
import at.ac.wu.seramis.sensor2process.database.Database;
import at.ac.wu.seramis.sensor2process.database.model.RawRead;
import at.ac.wu.seramis.sensor2process.database.model.Sensor;
import javafx.collections.ObservableList;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.ZoneOffset;
import java.util.HashMap;

/**
 * Created by andreas on 11/19/16.
 */
public class BufferReader implements SourceFunction<TemporalSenseEvent>{
    private static final long serialVersionUID = -8171568396068656378L;

    private static HashMap<String, Sensor> _sensors;

    static
    {
        Database.establishConnection(Database.SERVER, Database.PORT, Database.DB_NAME, Database.USER, Database.PASSW);
        _sensors = DataAccess.getSensorMap();
    }


    @Override
        public void run(SourceFunction.SourceContext<TemporalSenseEvent> ctx) throws Exception
        {
            Buffer dbBuffer = new DatabaseBuffer();
            ObservableList<RawRead> buffer = dbBuffer.getBuffer();
            dbBuffer.start();

            boolean running = true;
            while (running)
            {
                if (buffer.size() > 0)
                {
                    RawRead entry = buffer.remove(0);
                    if (entry == null) {
                        ctx.close();
                        running = false;
                    } else {
//                        String s = (entry).toString();

                        SenseEvent senseEvent = new SenseEvent();
                        senseEvent.setId(entry.getEpc());
                        Sensor sensor = _sensors.get(entry.getDevice() + "+" + entry.getAntenna());
                        senseEvent.setX(sensor.getPosX());
                        senseEvent.setY(sensor.getPosY());
                        senseEvent.setSignalStrength(entry.getRssi());
                        senseEvent.setTimestamp(entry.getTimestamp().toInstant(ZoneOffset.ofHours(0)).toEpochMilli());

//                        long timestamp = Long.parseLong(s.substring(s.length() - 13, s.length()));
                        ctx.collectWithTimestamp(senseEvent, senseEvent.getTimestamp());
                    }
                } else {
                    Thread.sleep(100);
                }
            }
        }

        @Override
        public void cancel()
        {
            // TODO Auto-generated method stub
        }
    }
