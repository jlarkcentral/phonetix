package phonetix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static phonetic.Phonetic.genererPhonetic;
import static phonetix.Phonetix.phonetize;
import static phonetix.Phonetixapi.apiphonetize;

/**
 * it would be nice to have a coherent list of words to test the phonetisation robustness
 *
 */

public class PhonetixTester {

	private static final Map<String, String> testMap;
	static
	{
		testMap = new HashMap<String, String>();
		testMap.put("complètement", "KONPLETEMAN");
		testMap.put("compltement", "KONPLETEMAN");
		testMap.put("tro", "TRO");
		testMap.put("trop", "TRO");
		testMap.put("bien", "BIIN");
		testMap.put("bi1", "BIIN");
		testMap.put("ça", "SA");
		testMap.put("ca", "SA");
	}

	private static List<String> mf_words = loadList(System.getProperty("user.dir") + "/src/phonetix/resources/mf_twit_all_words.txt");
	private static List<String> mf_words_nim = loadList("/home/feral/Documents/workspace/TAL_resources/mf_twit_notinmorphalou.txt");


	public static void main(String[] args) {
//		for (String key : testMap.keySet()) {
//			if (!phonetize(key).equals(testMap.get(key)))
//				System.out.println("MISMATCH : \"" + key + "\" −−> \"" + phonetize(key) + "\", should be \"" + testMap.get(key) +"\"");
//		}
		for (int i = 0; i < mf_words.size(); i++) {
			String word = mf_words.get(i);
			System.out.println(word + " −−> " + genererPhonetic(word));
		}
	}


	private static List<String> loadList(String path) {
		List<String> l = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				l.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
}