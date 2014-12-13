package phonetic;

import static org.hamcrest.CoreMatchers.is;
import static phonetic.Phonetic.genererPhonetic;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * it would be nice to have a coherent list of words to test the phonetisation robustness
 *
 */

public class PhoneticTester {

	public static void main(String[] args) {
		assertThat(genererPhonetic("complètement"), is("KONPLETEMAN"));
		assertThat(genererPhonetic("compltement"), is("KONPLETEMAN"));
		assertThat(genererPhonetic("tro"), is("TRO"));
		assertThat(genererPhonetic("trop"), is("TRO"));
		assertThat(genererPhonetic("bien"), is("BIN"));
		assertThat(genererPhonetic("bi1"), is("BIN"));
		assertThat(genererPhonetic("ça"), is("SA"));
		assertThat(genererPhonetic("ca"), is("SA"));
		assertThat(genererPhonetic("carrément"), is("KAREMAN"));
		assertThat(genererPhonetic("carement"), is("KAREMAN"));
	}
}