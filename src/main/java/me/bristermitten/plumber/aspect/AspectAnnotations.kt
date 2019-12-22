package me.bristermitten.plumber.aspect

import com.google.inject.Module
import kotlin.reflect.KClass

/**
 * Marks that an annotation is used by an aspect, and that the class scanner should initialize that Aspect if not
 * already done.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AspectAnnotation(val target: KClass<out Aspect>)

/**
 * Marks that an aspect is necessary for basic Plumber functionality, and should always be enabled
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiredAspect

/**
 * Allows aspects to link to a custom Module class, instead of having to be initialised to provide a Module
 * in [Aspect.module]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StaticAspectModule(val target: KClass<out Module>, val normalLoad: Boolean = false)

/**
 * Binds an [Aspect] to some third party annotations
 * That is, if these annotations are used at all, the aspect will be loaded
 * This annotation will only be acted upon if it annotates an [Aspect]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ThirdPartyAspectBinding(vararg val targets: KClass<out Annotation>)


