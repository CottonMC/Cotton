package io.github.cottonmc.cotton;

import java.util.List;
import java.util.Random;

/**
 * General-purpose utilities. Not public, do not depend on it.
 */
public class Utils {
    public static <T> T getRandomElement(List<T> c) {
        return c.get(new Random().nextInt(c.size() - 1));
    }
}
