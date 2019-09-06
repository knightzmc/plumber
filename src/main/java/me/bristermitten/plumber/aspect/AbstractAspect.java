package me.bristermitten.plumber.aspect;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAspect implements Aspect {
    protected Logger logger;

    private boolean enabled;
    @Inject
    private Injector injector;

    protected AbstractAspect() {
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        logger.info("Enabling aspect " + getClass().getSimpleName());
        doEnable();
        enabled = true;
        logger.info("Done");
    }

    @Override
    public void disable() {
        logger.info("Enabling aspect " + getClass().getSimpleName());
        doDisable();
        enabled = false;
        logger.info("Done");
    }

    protected void doEnable() {
    }

    protected void doDisable() {
    }

    @Override
    public Module getModule() {
        return null;
    }

    protected <T> T instance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}
