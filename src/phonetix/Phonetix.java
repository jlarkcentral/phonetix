package phonetix;

/**
 * modified version from :
 *
 * @author E. Bergé
 * @translated from PHP to Java by Christophe Schutz
 *
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class Phonetix {

    public static final String resourcePath = System.getProperty("user.dir") + "/src/phonetix/resources/";

    private static final Map<String, String> letter_conversions      = loadCSV(resourcePath + "letter_conversions.csv");
    private static final Map<String, String> IL_conversions          = loadCSV(resourcePath + "IL_conversions.csv");
    private static final Map<String, String> M_to_N_conversions      = loadCSV(resourcePath + "M_to_N_conversions.csv");
    private static final Map<String, String> special_cases           = loadCSV(resourcePath + "special_cases.csv");
    private static final Map<String, String> general_conversions     = loadCSV(resourcePath + "general_conversions.csv");
    private static final Map<String, String> YE_conversions          = loadCSV(resourcePath + "YE_conversions.csv");
    private static final Map<String, String> K_conversions           = loadCSV(resourcePath + "K_conversions.csv");
    private static final Map<String, String> W_conversions           = loadCSV(resourcePath + "W_conversions.csv");
    private static final Map<String, String> G_conversions           = loadCSV(resourcePath + "G_conversions.csv");
    private static final Map<String, String> SI_conversions          = loadCSV(resourcePath + "SI_conversions.csv");
    private static final Map<String, String> H_conversions           = loadCSV(resourcePath + "H_conversions.csv");
    private static final Map<String, String> nasal_conversions       = loadCSV(resourcePath + "nasal_conversions.csv");
    private static final Map<String, String> O_conversions           = loadCSV(resourcePath + "O_conversions.csv");
    private static final Map<String, String> other_conversions       = loadCSV(resourcePath + "other_conversions.csv");
    private static final Map<String, String> terminaison_conversions = loadCSV(resourcePath + "terminaison_conversions.csv");

    /**
     * Generate phonetisation from single-word? term
     *
     * @param str
     * @return str
     */
    public static String phonetize(String str) {

        String soundex = str.toUpperCase();


        soundex = soundex = applyConversion(soundex, letter_conversions);

        soundex = soundex.replaceAll("[^A-Z]", "");

        String soundexBack = soundex;// on sauve le code (utilisé pour les mots très courts)

        soundex = soundex.replaceAll("O[O]+", "OU");    // pré traitement OO... -> OU
        soundex = soundex.replaceAll("SAOU", "SOU");    // pré traitement SAOU -> SOU
        soundex = soundex.replaceAll("OES", "OS");    // pré traitement OES -> OS
        soundex = soundex.replaceAll("CCH", "K");        // pré traitement CCH -> K
        soundex = soundex.replaceAll("CC([IYE])", "KS$1"); // CCI CCY CCE
        soundex = soundex.replaceAll("(.)\\1", "$1");    // supression des répétitions
        // quelques cas particuliers
        for (String key : special_cases.keySet()) {
            if (soundex.equals(key)) return special_cases.get(key);
        }

        // pré-traitements
        soundex = applyConversion(soundex, general_conversions);

        // sons YEUX
        soundex = applyConversion(soundex, YE_conversions);


        soundex = applyConversion(soundex, IL_conversions);

        soundex = soundex.replaceAll("([^AEIOUY])(SC|S)IEM([EA])", "$1$2IAM$3");    // IEM -> IAM
        soundex = soundex.replaceAll("^(SC|S)IEM([EA])", "$1IAM$2");                // IEM -> IAM

        // MP MB -> NP NB

        soundex = applyConversion(soundex, M_to_N_conversions);


        // Sons en K
        soundex = applyConversion(soundex, K_conversions);

        // Weuh (perfectible)
        soundex = applyConversion(soundex, W_conversions);

        // Gueu, Gneu, Jeu et quelques autres
        soundex = applyConversion(soundex, G_conversions);

        // TI -> SI v2.0
        soundex = applyConversion(soundex, SI_conversions);

        // H muet
        soundex = applyConversion(soundex, H_conversions);

        // NASALES
        soundex = applyConversion(soundex, nasal_conversions);

        // Histoire d'Ô
        soundex = applyConversion(soundex, O_conversions);

        // Les retouches!
        soundex = applyConversion(soundex, other_conversions);

        // Terminaisons
        soundex = applyConversion(soundex, terminaison_conversions);

        // cas particuliers, bah au final, je n'en ai qu'un ici
        soundex = soundex.replaceAll("FUEL", "FIOUL");

        String soundexBack2 = soundex;

        // Ce sera le seul code retourné à une seule lettre!
        if (soundex.equals("O")) return (soundex);

        // seconde chance sur les mots courts qui ont souffert de la simplification
        if (soundex.length() < 2) {
            // Sigles ou abréviations
            if (soundexBack.matches("[BCDFGHJKLMNPQRSTVWXYZ][BCDFGHJKLMNPQRSTVWXYZ][BCDFGHJKLMNPQRSTVWXYZ][BCDFGHJKLMNPQRSTVWXYZ]*"))
                return (soundexBack);

            if (soundexBack.matches(("[RFMLVSPJDF][AEIOU]"))) {
                if (soundexBack.length() == 3)
                    return (soundexBack.substring(0, 2));// mots de trois lettres supposés simples
                if (soundexBack.length() == 4)
                    return (soundexBack.substring(0, 3));// mots de quatre lettres supposés simples
            }

            if (soundexBack2.length() > 1) return soundexBack2;
        }

        if (soundex.length() > 1)
            return soundex;
        else
            return "";
    }

    private static String applyConversion(String soundex, Map<String, String> map) {
        for (String conversion : map.keySet()) {
            soundex = soundex.replaceAll(conversion, map.get(conversion));
        }
        return soundex;
    }

    private static String applyConversion(String soundex, String[][] map) {
        for (String[] conversion : map) {
            soundex = soundex.replaceAll(conversion[0], conversion[1]);
        }
        return soundex;
    }

    // poor man's csvbufferedreader
    private static Map<String, String> loadCSV(String path) {
        Map<String, String> map = new HashMap<>();
        String separator = ",";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(separator);
                if (row.length == 1) {
                    map.put(row[0], "");
                }
                else {
                    assertThat(row.length, is(2));
                    map.put(row[0], row[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
