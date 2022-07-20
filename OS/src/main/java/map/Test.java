package map;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

/**
 * @author leo
 * @date 20220714 22:32:24
 */
public class Test {
    public static void testMap(Map iMap,final int cycle) {
        final Random random = new Random();
        //final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        //final StringBuilder builder = new StringBuilder();
        Instant begin = Instant.now();
        int keyLength = 6;
        for (int i = 0; i < cycle; i++) {
            //builder.setLength(0);
            for (int j = 0; j < keyLength; j++) {
                //builder.append(alphabet.charAt(random.nextInt(26)));
            }
            //String key = builder.toString();
            //builder.setLength(0);
            for (int j = 0; j < 1; j++) {
                //builder.append(alphabet.charAt(random.nextInt(26)));
            }
            //String value = builder.toString();
            iMap.put(random.nextInt(cycle),"value");
        }
        for (int i = 0; i < cycle; i++) {
            //builder.setLength(0);
            for (int j = 0; j < keyLength; j++) {
                //builder.append(alphabet.charAt(random.nextInt(26)));
            }
            //String key = builder.toString();
            iMap.get(random.nextInt(cycle));
        }
        for (int i = 0; i < cycle; i++) {
            //builder.setLength(0);
            for (int j = 0; j < keyLength; j++) {
                //builder.append(alphabet.charAt(random.nextInt(26)));
            }
            //String key = builder.toString();
            iMap.remove(random.nextInt(cycle));
        }
        long l = Duration.between(begin, Instant.now()).toSeconds();
        System.out.println("l = " + l);
    }
}
