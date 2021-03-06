package me.bristermitten.plumber.aspect
import me.bristermitten.plumber.reflection.ClassFinder
/**
 * Configuration interface for an Aspect
 * Users can extend this to customise functionality for an Aspect
 * However it is the Aspect's responsibility to get the implementations from [ClassFinder]
 * and load them into the individual Aspect
 *
 * Do not use this interface, instead create a sub-interface that provides the required Type Parameter
 * and declares methods to implement
 */
interface AspectConfig<T: Aspect>


