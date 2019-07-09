package io.github.cottonmc.cotton.logging;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private Logger log;

    private String prefix;

    private final boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();

    public ModLogger(Class clazz){
        this(clazz.getSimpleName());
    }

    public ModLogger(String name){
        this(name, name);
    }
    public ModLogger(Class clazz, String prefix) {
        this(clazz.getSimpleName(), prefix);
    }

    public ModLogger(String name, String prefix) {
        this.log = LogManager.getFormatterLogger(name);
        setPrefix(prefix);
    }

    private void setPrefix(String prefix) {
        if(prefix.length()>0){
            this.prefix="["+prefix+"]: ";
        } else {
            this.prefix="";
        }
    }

    public void retarget(Logger to) {
        log = to;
    }

    public void log(Level level, String msg, Object... data) {
        log.log(level, prefix + msg, data);
    }

    /**
     * @deprecated Use {@link #log(Level, String, Object...)} instead with the throwable as the last object
     *             after the rest of the data.
     */
    @Deprecated
    public void log(Level level, Throwable ex, String msg, Object... data) {
        log(level, msg, ArrayUtils.add(data, ex));
    }

    public void error(String msg, Object... data) {
        log(Level.ERROR, msg, data);
    }

    public void warn(String msg, Object... data) {
        log(Level.WARN, msg, data);
    }

    public void info(String msg, Object... data) {
        log(Level.INFO, msg, data);
    }

    /**
     * Only log this error in a dev enviroment.
     */
    public void devError(String msg, Object... data) {
        if (isDev) error(msg, data);
    }

    /**
     * Only log this warning in a dev environment.
     */
    public void devWarn(String msg, Object... data) {
        if (isDev) warn(msg, data);
    }

    /**
     * Only log this info in a dev environment.
     */
    public void devInfo(String msg, Object... data) {
        if (isDev) info(msg, data);
    }

    public void debug(String msg, Object... data) {
        log(Level.DEBUG, msg, data);
    }

    public void trace(String msg, Object... data) {
        log(Level.TRACE, msg, data);
    }

    public void all(String msg, Object... data) {
        log(Level.ALL, msg, data);
    }
}
