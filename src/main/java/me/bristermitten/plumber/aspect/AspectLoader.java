package me.bristermitten.plumber.aspect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Injector;
import me.bristermitten.plumber.PlumberPlugin;
import me.bristermitten.plumber.injection.AspectPlumberModule;
import me.bristermitten.plumber.injection.InitialPlumberModule;
import me.bristermitten.plumber.injection.PlumberModule;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class AspectLoader {

    private static AspectLoader instance;
    private final Reflections reflections;
    private final PlumberPlugin plumberPlugin;
    private Set<Class<? extends Aspect>> requiredAspects = new LinkedHashSet<>();
    private BiMap<Class<? extends Annotation>, Class<? extends Aspect>> mappings;
    private Injector injector;

    public AspectLoader(PlumberPlugin plumberPlugin, Reflections reflections) {
        this.plumberPlugin = plumberPlugin;
        this.reflections = reflections;
        this.mappings = HashBiMap.create();
        instance = this;
    }

    public static AspectLoader getInstance() {
        return instance;
    }

    public AspectLoader addThirdPartyAspectAnnotation(Class<? extends Annotation> annotation,
                                                      Class<? extends Aspect> aspect) {
        mappings.put(annotation, aspect);
        return this;
    }

    private void makeMappings() {
        reflections.getTypesAnnotatedWith(AspectAnnotation.class)
                .stream().filter(Class::isAnnotation)
                .map(c -> (Class<? extends Annotation>) c)
                .forEach(annotation -> {
                    AspectAnnotation aa = annotation.getAnnotation(AspectAnnotation.class);
                    mappings.put(annotation, aa.target());
                });
    }

    public AspectLoader ensureLoaded(Class<? extends Aspect> aspectType) {
        if (!requiredAspects.contains(aspectType)) {
            if (injector != null)
                load(injector.getInstance(aspectType));
            requiredAspects.add(aspectType);
        }

        return this;
    }

    public Set<Aspect> loadAll() {
        makeMappings();
        findAll();

        PlumberModule module = new InitialPlumberModule(plumberPlugin, reflections, this, requiredAspects);
        injector = module.createInjector();

        Set<Aspect> aspects = requiredAspects.stream()
                .map(injector::getInstance)
                .collect(Collectors.toSet());

        module = new AspectPlumberModule(module, aspects);
        injector = module.createInjector();
        injector.injectMembers(plumberPlugin);

        aspects.forEach(this::load);
        return aspects;
    }

    private Set<Class> findAspectChildren(Class<? extends Aspect> a) {
        Class<? extends Annotation> mapping = mappings.inverse().get(a);
        if (mapping == null) return Collections.emptySet();
        return reflections.getTypesAnnotatedWith(mapping)
                .stream().filter(c -> !c.isInterface() && !c.isEnum())
                .collect(Collectors.toSet());
    }

    private void findAll() {
        try {
            mappings.forEach((a, as) -> {
                if (!reflections.getTypesAnnotatedWith(a).isEmpty()) {
                    if (as.isAnnotationPresent(Dependency.class)) {
                        Dependency dependency = as.getAnnotation(Dependency.class);
                        requiredAspects.addAll(Arrays.asList(dependency.value()));
                    }
                    requiredAspects.add(as);
                }
            });
        } catch (Throwable f) {
            f.printStackTrace();
        }

    }

    private void load(Aspect a) {
        injector.injectMembers(a);
        a.enable();
        a.loadParts(findAspectChildren(a.getClass()));
    }
}
