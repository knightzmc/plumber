package me.bristermitten.plumber.dsl

import com.google.inject.Module
import com.google.inject.assistedinject.FactoryModuleBuilder
import me.bristermitten.plumber.aspect.AbstractAspect
import me.bristermitten.plumber.aspect.RequiredAspect
import me.bristermitten.plumber.struct.player.PPlayer

/**
 * Aspect containing the management of all data classes
 * This includes most of the main objects in Plumber,
 * namely [PPlayer]
 */
@RequiredAspect
class DSLAspect : AbstractAspect() {

    override fun module(): Module {
        return FactoryModuleBuilder()
                .implement(PlayerActionBuilder::class.java, PlayerActionBuilder.impl)
                .implement(TaskLengthConfiguration::class.java, TaskLengthConfiguration.impl)
                .build(BuilderFactory::class.java)
    }
}