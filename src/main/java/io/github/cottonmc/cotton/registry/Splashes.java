package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * A registry to allow manipulation of the splash messages on the main menu.
 */
@Environment(EnvType.CLIENT)
public class Splashes {

    /**
     * Used to make sure MISSINGNO never shows up, that is vanilla behaviour.
     * */
    private static final int MISSINGNO = 0x77f432f;

    /**
     * Vanilla splashes.
     */
    private static List<String> defaultSplashes = new ArrayList<>();
    private static List<String> splashes = new ArrayList<>();
    private static List<String> holidaySplashes = new ArrayList<>();

    public static void initialize() {
        if (Cotton.config.add_cotton_splashes) {
            addSplashes(
                    "It has been planted!",
                    "Fresh and organic!",
                    "Hello Fabric World!",
                    "NullPointerException",
                    "Say hello to the New Age");
            addHolidaySplashOnDate("Happy PI day!", 3, 14);
            addHolidaySplashOnDate("Testing day!", 2, 10);
        }
        Cotton.logger.info("Splashes initialized.");
    }

    /**
     * @return Returns all used splashes.
     */
    public static List<String> getAllSplashes() {
        List<String> s = new ArrayList<>();
        s.addAll(defaultSplashes);
        s.addAll(splashes);
        return s;
    }
    
    /**
     * Used to load the initial Minecraft splashes.
     *
     * @param splashesLoc The location of the default splashes
     */
    public static void loadDefaultSplashes(Identifier splashesLoc) {
        defaultSplashes.clear();
        try (BufferedReader splashReader = new BufferedReader(
                new InputStreamReader(
                        MinecraftClient.getInstance().getResourceManager().getResource(splashesLoc).getInputStream(),
                        StandardCharsets.UTF_8))) {
            String splash;
            while((splash = splashReader.readLine()) != null) {
                splash = splash.trim();
                if(!splash.isEmpty() && splash.hashCode() != MISSINGNO) {
                    defaultSplashes.add(splash);
                }
            }
        }
        catch(IOException e) {
            Cotton.logger.warn("Couldn't read splashes");
        }
    }
    
    /**
     * Used by the main menu to get a splash.
     *
     * @param random The random instance to use
     *
     * @return A random splash, possibly holiday themed
     */
    public static String getRandomSplash(Random random) {
        System.out.println(holidaySplashes.size());
        if (Cotton.config.prioritize_custom_splashes) {
            if (holidaySplashes.isEmpty()) {
                // Roll a dice from 0 to X, and if it lands on 0 (chance is 1/X), use a vanilla splash.
                // Else, use a modded one.
                if (random.nextInt(Cotton.config.custom_splash_priority) == 0) {
                    return vanillaSplashLogic();
                }
                else {
                    return Utils.getRandomElement(splashes);
                }
            }
            else {
                return Utils.getRandomElement(holidaySplashes);
            }
        }
        else {
            if (hasVanillaHoliday()) {
                return getVanillaHolidaySplash();
            }
            else {
                return Utils.getRandomElement(getAllSplashes());
            }
        }
    }

    // This tries to match vanilla behaviour. (aka get a vanilla splash if not holiday, else print holiday splash)
    private static String vanillaSplashLogic() {
        if (hasVanillaHoliday()) {
            return getVanillaHolidaySplash();
        }
        return Utils.getRandomElement(defaultSplashes);
    }

    public static boolean hasVanillaHoliday() {
        return getVanillaHolidaySplash() != null;
    }

    private static String getVanillaHolidaySplash() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) == 24) {
            return "Merry X-mas!";
        } else if (calendar.get(Calendar.MONTH) + 1 == 1 && calendar.get(Calendar.DATE) == 1) {
            return "Happy new year!";
        } else if (calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) == 31) {
            return "OOoooOOOoooo! Spooky!";
        }
        else {
            return null; // but we probably fucked up; this is not good.
        }
    }

    /**
     * Add a splash to the main menu.
     *
     * @param splash The splash to be added
     */
    public static void addSplash(String splash) {
        if (splash.hashCode() != MISSINGNO) {
            splashes.add(splash);
        }
    }

    /**
     * Adds a splash to the main menu.
     *
     * It will override all non-holiday splashes.
     *
     * @param splash The holiday splash to add
     * */
    public static void addHolidaySplash(String splash) {
        if (splash.hashCode() != MISSINGNO) {
            holidaySplashes.add(splash);
        }
    }

    /**
     * Adds a splash to the main menu.
     *
     * It will override all non-holiday splashes.
     *
     * @param splash The holiday splash to add
     * @param month The month when the splash should be applied
     * @param day The day when the splash should be applied
     * */
    public static void addHolidaySplashOnDate(String splash, int month, int day) {
        LocalDate today = LocalDate.now();
        if (month == today.getMonthValue() && day == today.getDayOfMonth()) {
            addHolidaySplash(splash);
        }
    }

    /**
     * Adds some splashes to the main menu.
     *
     * @param _splashes The splashes to add
     * */
    public static void addSplashes(String ... _splashes) {
        for(String splash : _splashes){
            addSplash(splash);
        }
    }
    
    /**
     * Adds some splashes to the main menu.
     *
     * @param _splashes The splashes to add
     * */
    public static void addSplashes(Collection<String> _splashes) {
        for(String splash : _splashes){
            addSplash(splash);
        }
    }
}
