package io.github.cottonmc.cotton.logging;

import net.fabricmc.loader.api.FabricLoader;
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
        this.log = LogManager.getLogger(name);
        setPrefix(prefix);
    }

    private void setPrefix(String prefix) {
        if(prefix.length()>0){
            this.prefix="["+prefix+"]: ";
        } else {
            this.prefix="";
        }
    }

    /**
     * ANSI is no longer supported in Cotton loggers, due to no production launcher supporting it.
     */
    @Deprecated
    public void setPrefixFormat(Ansi format) {
    }

    public void retarget(Logger to) {
        log = to;
    }

    public void log(Level level, String msg, Object... data) {
        log.log(level, prefix + String.format(msg, data));
    }

    public void log(Level level, Throwable ex, String msg, Object... data) {
        log.log(level, prefix + String.format(msg, data), ex);
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
        log(Level.TRACE, msg, data);
    }
}
