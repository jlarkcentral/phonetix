package phonetix;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static phonetix.Phonetix.phonetize;

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

	public static void main(String[] args) {
		for (String key : testMap.keySet()) {
			if (!phonetize(key).equals(testMap.get(key)))
				System.out.println("MISMATCH : \"" + key + "\" −−> \"" + phonetize(key) + "\", should be \"" + testMap.get(key) +"\"");
		}
	}
}