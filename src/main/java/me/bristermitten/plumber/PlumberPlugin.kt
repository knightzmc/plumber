/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package me.bristermitten.plumber

import co.aikar.commands.annotation.CommandAlias
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import io.github.classgraph.ClassGraph
import me.bristermitten.plumber.aspect.AspectManager
import me.bristermitten.plumber.aspect.modules.InitialModule
import me.bristermitten.plumber.command.CommandAspect
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.util.logging.LogManager
import kotlin.system.measureTimeMillis

/**
 * Main class of Plumber. A Plugin that uses Plumber should extend this instead of
 * [JavaPlugin], as it is responsible for the loading of the framework,
 * and may handle more in the future.
 *
 *
 * This class will be a Singleton throughout the framework, and at the moment doesn't do much
 * else than the initial setup.
 * On that note, in any Plumber plugin, [PlumberPlugin.loadPlumber]
 * should be called in your [JavaPlugin.onEnable]
 */
@Singleton
open class PlumberPlugin : JavaPlugin {

    @Inject
    protected lateinit var injector: Injector

    constructor()

    constructor(loader: JavaPluginLoader?, description: PluginDescriptionFile?, dataFolder: File?, file: File?) : super(loader, description, dataFolder, file)

    override fun onEnable() {
        loadPlumber()
    }

    /**
     * Load the framework.
     * This entails scanning classes in the classpath, creating instances and injectors
     * through Guice, and loading all necessary aspects.
     * This should be called immediately in [JavaPlugin.onEnable]
     */
    protected fun loadPlumber() {
        initLoggers()
        logger.info("Plumber loading for Plugin $name...")

        val length = measureTimeMillis {
            val ourPackage = javaClass.getPackage().name
            val packages = arrayOf(ourPackage, PlumberPlugin::class.java.getPackage().name)

            val classGraph = ClassGraph()
                    .enableAllInfo()
                    .whitelistPackages(*packages)

            val initial = InitialModule(this, classGraph)
            val initialInjector = Guice.createInjector(initial)

            val manager = initialInjector.getInstance(AspectManager::class.java)

            manager.loadBaseBindings()
            manager.loadAll(this)
        }
        logger.info("Plumber loaded in $length ms!")
    }

    /**
     * Configure both slf4j and the existing Java loggers to use stdout instead of stderr
     */
    private fun initLoggers() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        LogManager.getLogManager().reset()
    }

    fun <T> getInstance(clazz: Class<T>): T {
        return injector.getInstance(clazz)
    }
}
