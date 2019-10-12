package me.bristermitten.plumber.aspect

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Module
import org.slf4j.LoggerFactory

/**
 * Boilerplate-handling abstract implementation of [Aspect]
 * Provides logging, enabled-status handling, and a wrapper for Guice's [Injector]
 */
abstract class AbstractAspect : Aspect {

    protected val logger = LoggerFactory.getLogger(javaClass)!!

    private var enabled = false

    @Inject
    private lateinit var injector: Injector

    override fun enable() {
        logger.info("Enabling aspect " + javaClass.simpleName)
        doEnable()
        enabled = true
        logger.info("Done")
    }

    override fun disable() {
        logger.info("Disabling aspect " + javaClass.simpleName)
        doDisable()
        enabled = false
        logger.info("Done")
    }

    protected open fun doEnable() {}

    protected open fun doDisable() {}

    override fun module(): Module? = null

    protected fun <T> instance(clazz: Class<T>): T = injector.getInstance(clazz)
}
