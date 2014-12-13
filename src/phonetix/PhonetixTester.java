package phonetix;

import static org.hamcrest.CoreMatchers.is;
import static phonetic.Phonetic.genererPhonetic;

import static org.hamcrest.MatcherAssert.assertThat;
import static phonetix.Phonetix.phonetize;

/**
 * it would be nice to have a coherent list of words to test the phonetisation robustness
 *
 */

public class PhonetixTester {

	public static void main(String[] args) {
		assertThat(phonetize("complètement"), is("KONPLETEMAN"));
		assertThat(phonetize("compltement"), is("KONPLETEMAN"));
		assertThat(phonetize("tro"), is("TRO"));
		assertThat(phonetize("trop"), is("TRO"));
		assertThat(phonetize("bien"), is("BIN"));
		assertThat(phonetize("bi1"), is("BIN"));
		assertThat(phonetize("ça"), is("SA"));
		assertThat(phonetize("ca"), is("SA"));
		assertThat(phonetize("carrément"), is("KAREMAN"));
		assertThat(phonetize("carement"), is("KAREMAN"));
	}
}