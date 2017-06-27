package at.ac.wu.seramis.sensor2process;

import at.ac.wu.seramis.sensor2process.utils.BPMNAnnotationParser;
import com.google.common.base.Joiner;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by andreas on 11/18/16.
 */
public class IntegrationTest {
    @Test
    public void testLoadPatternsFromBPMN() throws Exception {
        ArrayList<String> annotations = BPMNAnnotationParser.getBPMNAnnotations(IntegrationTest.class.getResourceAsStream("/rfidanalysis/resources/test.bpmn"));
        System.out.println(Joiner.on("\n").join(annotations));
    }
}
