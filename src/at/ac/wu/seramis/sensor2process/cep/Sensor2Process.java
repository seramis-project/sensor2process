package at.ac.wu.seramis.sensor2process.cep;

import at.ac.wu.seramis.sensor2process.cep.model.PatternRegistry;
import at.ac.wu.seramis.sensor2process.cep.model.events.AbstractBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.*;
import at.ac.wu.seramis.sensor2process.cep.pgsql.converter.BusinessEventConverter;
import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.stream.sinks.pgsql.BusinessEventPostgresSink;
import at.ac.wu.seramis.sensor2process.cep.stream.sinks.pgsql.SelectiveBusinessEventPostgresSink;
import at.ac.wu.seramis.sensor2process.database.source.BufferReader;
import at.ac.wu.seramis.sensor2process.utils.BPMNAnnotationParser;
import com.google.common.base.Joiner;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.ws.rs.core.Application;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by andreas on 11/18/16.
 */
public class Sensor2Process {
    public Sensor2Process() {
        File bpmnFile = new File("./resources/test/test.bpmn");
//        File bpmnFile = promptForFolder();
        if (bpmnFile != null) {
            try {
                ArrayList<String> annotations = BPMNAnnotationParser.getBPMNAnnotations(new FileInputStream(bpmnFile));
                PatternRegistry registry = PatternRegistry.getInstance();

                StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
                env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

                DataStream<TemporalPosition> positionDataStream = getPositionDataStream(env);

                // extract locations first:
                DataStream<IBusinessEvent> locationStream = null;
                for (String locationKey : PatternRegistry.getInstance().getLocationPatterns().keySet()) {
                    if (annotations.contains(locationKey)) {
                        // enable pattern
                        if (locationStream != null) {
                            locationStream = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, PatternRegistry.getInstance().getLocationPatterns().get(locationKey)).union(locationStream);
                        } else {
                            locationStream = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, PatternRegistry.getInstance().getLocationPatterns().get(locationKey));
                        }
                    }
                }

                DataStream<IBusinessEvent> eventsStream = null;
                for (AbstractBusinessEventPattern pat : registry.getPatterns()) {
                    // enable pattern always
                    if (eventsStream == null) {
                        eventsStream = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, pat);
                    } else {
                        eventsStream = CEPBusinessPosition.toBusinessEventsStream(positionDataStream, pat).union(eventsStream);
                    }
                }
                positionDataStream.print();
                DataStream<IBusinessEvent> allEventsStream = locationStream.union(eventsStream);
                allEventsStream.print();
                DataStream<BusinessEvent> pgsqlBusinessEvents = allEventsStream.map(businessEvent -> BusinessEventConverter.convert(businessEvent));
                pgsqlBusinessEvents.addSink(new SelectiveBusinessEventPostgresSink(URI.create("postgres://<DB_USER>:<DB_PASS>@<SERVER>:<PORT>/<DATABASE_NAME>"), 20, annotations));

                try {
                    env.execute("Simulated DataStream");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(Joiner.on(", ").join(annotations));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private DataStream<TemporalPosition> getPositionDataStream(StreamExecutionEnvironment env) {
        DataStream<TemporalSenseEvent> dataStream = env.
                addSource(new BufferReader()).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalSenseEvent>() {
            @Override
            public long extractAscendingTimestamp(TemporalSenseEvent temporalSenseEvent) {
                return temporalSenseEvent.getTimestamp();

            }
        });
        return CEPCenterOfGravity.extractPositionsFromSensors(dataStream);
//        .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalPosition>()
//        {
//            @Override
//            public long extractAscendingTimestamp(TemporalPosition temporalSenseEvent)
//            {
//                return temporalSenseEvent.getTimestamp();
//            }
//        });
    }

    public static void main(String[] args) {
        new Sensor2Process();

    }

    public File promptForFolder() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith("bpmn") || f.getName().toLowerCase().endsWith("xml");
            }

            @Override
            public String getDescription() {
                return "BPMN 2.0 XML model";
            }
        });

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }

        return null;
    }
}
