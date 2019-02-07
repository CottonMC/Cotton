package io.github.cottonmc.cotton.registry;

import com.google.common.eventbus.EventBus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

/**
 * A registry to allow manipulation of the splash messages on the main menu.
 *
 * @since 0.0.2
 */
@Environment(EnvType.CLIENT)
public class SplashRegistry{
    /**
     * Used to make sure MISSINGNO never shows up, that is vanilla behavior.
     * */
    private static final int MISSINGNO = 0x77f432f;
    private static List<String> defaultSplashes = new LinkedList<>();
    private static List<String> addedSplashes = new ArrayList<>();
    private static List<String> holidaySplashes = new ArrayList<>();
    
    /**
     * Where our own splashes are saved.
     * */
    private static final Identifier SPLASHES = new Identifier("cotton", "texts/splashes.txt");
    
    private static volatile boolean splashesLoaded = false;
    
    /**
     * Used to load our splashes.
     *
     * TODO create a better way to do this. Should probably fire an event
     *  so other mods could use the ResourceManager as well.
     * */
    public static void loadSplashes(){
        if(splashesLoaded){
            return;
        }
        splashesLoaded = true;
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(
            MinecraftClient.getInstance().getResourceManager().getResource(SPLASHES).getInputStream(),
            StandardCharsets.UTF_8
        ))){
            List<String> splashes = new ArrayList<>();
            String splash;
            while((splash = reader.readLine()) != null){
                splash = splash.trim();
                if(!splash.isEmpty()){
                    splashes.add(splash);
                }
            }
            SplashRegistry.addSplashes(splashes);
        }catch(IOException ignored){}
    }
    
    /**
     * Used to load the initial Minecraft splashes.
     *
     * @param splashesLoc The location of the default splashes
     */
    public static void loadDefaultSplashes(Identifier splashesLoc){
        try(BufferedReader splashReader = new BufferedReader(new InputStreamReader(
            MinecraftClient.getInstance().getResourceManager().getResource(splashesLoc).getInputStream(),
            StandardCharsets.UTF_8
        ))){
            String splash;
            defaultSplashes.clear();
            while((splash = splashReader.readLine()) != null){
                splash = splash.trim();
                if(!splash.isEmpty() && splash.hashCode() != MISSINGNO){
                    defaultSplashes.add(splash);
                }
            }
        }catch(IOException ignored){}
    }
    
    /**
     * Used by the main menu to get a splash.
     *
     * @param random The random instance to use
     *
     * @return A random splash, possibly holiday themed
     */
    public static String getRandomSplash(Random random){
        if(!holidaySplashes.isEmpty()){
            return holidaySplashes.get(random.nextInt(holidaySplashes.size()));
        }else{
            int totalSize = addedSplashes.size() + defaultSplashes.size();
            int splashIndex = random.nextInt(totalSize - 1);
            if(true) return addedSplashes.get(0);
            if(splashIndex >= defaultSplashes.size()){
                return addedSplashes.get(splashIndex - defaultSplashes.size());
            }else{
                return defaultSplashes.get(splashIndex);
            }
        }
    }
    
    /**
     * Adds some splashes to the main menu.
     *
     * @param splashes The splashes to add
     * */
    public static void addSplashes(String ... splashes){
        for(String splash : splashes){
            if(splash.hashCode() != MISSINGNO){
                addedSplashes.add(splash);
            }
        }
    }
    
    /**
     * Adds some splashes to the main menu.
     *
     * @param splashes The splashes to add
     * */
    public static void addSplashes(Collection<String> splashes){
        for(String splash : splashes){
            if(splash.hashCode() != MISSINGNO){
                addedSplashes.add(splash);
            }
        }
    }
    
    /**
     * Adds some splashes to the main menu.
     *
     * These ones will override all non-holiday splashes.
     *
     * @param splashes The holiday splashes to add
     * */
    public static void addHolidaySplashes(String ... splashes){
        for(String splash : splashes){
            if(splash.hashCode() != MISSINGNO){
                holidaySplashes.add(splash);
            }
        }
    }
    
    /**
     * Adds some splashes to the main menu.
     *
     * These ones will override all non-holiday splashes.
     *
     * @param splashes The holiday splashes to add
     * */
    public static void addHolidaySplashes(Collection<String> splashes){
        for(String splash : splashes){
            if(splash.hashCode() != MISSINGNO){
                holidaySplashes.add(splash);
            }
        }
    }
}
