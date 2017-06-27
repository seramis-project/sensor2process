package at.ac.wu.seramis.sensor2process.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BPMNAnnotationParser 
{
	private static final String _PATTERN = "<textAnnotation [^>]+>\\s*\\n?\\s*<text>([^<]+)</text>s*\\n?\\s*</textAnnotation>";
	
	@SuppressWarnings("resource")
	public static ArrayList<String> getBPMNAnnotations(InputStream bpmnFile)
	{
		try
		{
			Scanner bpmnScanner = new Scanner(bpmnFile).useDelimiter("\\Z");
			String bpmn = bpmnScanner.next();
			bpmnScanner.close();
			bpmnFile.close();
						
			ArrayList<String> annotations = new ArrayList<>();

			for ( Matcher m = Pattern.compile(_PATTERN).matcher(bpmn); m.find(); )
			{
				annotations.add(m.toMatchResult().group(1));
			}

			return annotations;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		  return null;
	}
}
