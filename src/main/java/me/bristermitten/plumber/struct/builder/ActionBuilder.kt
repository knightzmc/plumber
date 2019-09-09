package me.bristermitten.plumber.struct.builder

import me.bristermitten.plumber.struct.builder.impl.ActionBuilderImpl

interface ActionBuilder<T : ActionBuilder<T>> {

    /**
     * Returns the parent object for further configuration
     *
     * @return the parent
     */
    fun or(): TaskLengthConfiguration<T>


    companion object {
        var impl: Class<out ActionBuilder<*>> = ActionBuilderImpl::class.java
    }
}
