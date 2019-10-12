@file:Suppress("UNCHECKED_CAST")

package me.bristermitten.plumber.struct.builder.impl

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import me.bristermitten.plumber.struct.builder.ActionBuilder
import me.bristermitten.plumber.struct.builder.TaskLengthConfiguration
import me.bristermitten.plumber.scheduling.TaskFactory
import me.bristermitten.plumber.scheduling.timings.Time
import me.bristermitten.plumber.scheduling.timings.TimeUnitPicker
import me.bristermitten.plumber.scheduling.timings.TimeUnitPickerFactory

internal open class DefaultTaskLengthConfiguration<B : ActionBuilder<B>>
@Inject constructor(
        @Assisted private val value: B,
        private val factory: TimeUnitPickerFactory,
        private val taskFactory: TaskFactory
) : TaskLengthConfiguration<B> {

    private val length: Time = Time()

    override fun forever() {
        length.length = -1
    }

    override fun until(): B {
        return value
    }

    override fun undoAfter(time: Long): TimeUnitPicker<B> {
        length.length = time
        return factory.pick(value, {
            length.unit = it
            taskFactory.create(length, Time.NONE, value).start()
        })
    }
}