import java.io.BufferedReader;
import java.io.FileReader;

import edu.cmu.cs.lti.ark.tweetnlp.RunPOSTagger;


public class TaggerUtils {


	static void runPOSTagger(String input, String output) {
		try {
			String[] array = {"-input", input, "-output", output};
			RunPOSTagger.main(array );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
