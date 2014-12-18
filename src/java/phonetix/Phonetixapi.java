package phonetix;

/**
 * Created by feral on 14/12/14.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Phonetix using the International Phonetic Alphabet
 */

/*
API	Mots écrits
b	bal, robe
s	souris, pièce
k	carpe, kiwi, qui
d	date
f	face, phare
ɡ	gare, bague
ʒ	journal, gorge
l	la, alors
m	maman
n	non
ɲ	gnôle, agneau
p	petit
ʁ	rare
t	tordu
v	voir, wagon
z	zèbre, oser
ʃ	chat, short
//Les douze voyelles orales
API	Mots écrits
a	patte, papa
ɑ	pâte, tas
ə	fenêtre
ø	jeu, feu
œ	fleur
e	été, nez
ɛ	mer, j’aimais
o	sot, seau, sceau, saut
ɔ	porte, port, or, mort
i	fille, ami
u	coup, août
y	nu, j’ai eu
Les quatre voyelles nasales
API	Mots écrits
ɑ̃	rang, avant
ɛ̃	rein, brin, pain
ɔ̃	bon, ton
œ̃	brun, un
Les trois semi-consonnes (ou semi-voyelles)
API	Mots écrits
j	yeux ail
w	fouet (/fwɛ/), voir (/vwaʁ/)
ɥ	fuite (/fɥit/), lui (/lɥi/)
*/

public class Phonetixapi {

    private static final String[][] specialCases = {
            {"ÇA", "SA"},
            {"WAGON", "VAGON"},
    };

    private static final String[][] consonantMap = {
        // b rules
        // s rules
        {"Ç", "S"},
        {"C([ÈEI])", "S$1"},
        // k rules
        {"QU", "K"},
        {"C([^EI])", "K$1"},
        // d rules
        // f rules
        {"PH", "F"},
        // g rules
        {"GU([AEIO])", "G$1"},
        // j rules
        {"GE", "J"},
        // l rules
        // m rules
        // n rules
        // gn rules
        // p rules
        // r rules
        // t rules
        // v rules

        // z rules
        {"([AEIOU])S([AEIOU])", "$1Z$2"},
        // ch rules
        {"SH", "CH"},
        {"SCH", "CH"},
    };

    private static final Map<String, String> vowelMap;
    static {
        vowelMap = new HashMap<>();
        // a rules
        vowelMap.put("[ÄÀÂ]", "A");
        // e rules
        vowelMap.put("EU", "E");
        // é rules
        vowelMap.put("EI", "É");
        // è rules
        // o rules
        // i rules
        // ou rules
        // u rules

        // an rules
        // 1 rules
        // on rules

        // ye rules
        // we rules
        // ui rules
    }

    /**
     * Generate phonetisation from single-word? term
     *
     * @param str
     * @return str
     */
    public static String apiphonetize(String str) {

        String soundex = str.toUpperCase();

        // onomatopées
        soundex = soundex.replaceAll("([AEIOU])\\1+$", "$1H");

        // repetitions
        soundex = soundex.replaceAll("(.)\\1+", "$1");



        soundex = applyConversion(soundex, consonantMap);

        soundex = applyConversion(soundex, vowelMap);

        return soundex;
    }

    private static String applyConversion(String soundex, Map<String, String> map) {
        for (String conversion : map.keySet()) {
            if (soundex.length() == 1)
                return soundex;
            else
                soundex = soundex.replaceAll(conversion, map.get(conversion));
        }
        return soundex;
    }

    private static String applyConversion(String soundex, String[][] map) {
        for (String[] conversion : map) {
            if (soundex.length() == 1)
                return soundex;
            else
                soundex = soundex.replaceAll(conversion[0], conversion[1]);
        }
        return soundex;
    }
}
