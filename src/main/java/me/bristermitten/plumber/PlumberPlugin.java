/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package me.bristermitten.plumber;

import co.aikar.commands.annotation.CommandAlias;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.bristermitten.plumber.aspect.AspectLoader;
import me.bristermitten.plumber.command.CommandAspect;
import me.bristermitten.plumber.object.DataAspect;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;

/**
 * Main class of the project
 * Plumber equivalent of SpringApplication
 */
public class PlumberPlugin extends JavaPlugin {


    public PlumberPlugin() {
    }

    public PlumberPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Inject
    private Injector injector;

    protected void loadPlugin() {
        String ourPackage = getClass().getPackage().getName();
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setClassLoaders(new ClassLoader[]{getClassLoader()});
        configurationBuilder.filterInputsBy(p -> !p.contains("META-INF"));
        configurationBuilder.filterInputsBy(p -> !p.contains("org.bukkit"));
        Configuration config = configurationBuilder.forPackages(ourPackage);


        Reflections reflections = new Reflections(config);

        new AspectLoader(this, reflections)
                .ensureLoaded(DataAspect.class)
                .addThirdPartyAspectAnnotation(CommandAlias.class, CommandAspect.class)
                .loadAll();
    }

    protected final <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}
