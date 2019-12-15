package me.bristermitten.rewrite.dsl

import me.bristermitten.rewrite.dsl.core.ActionFilter
import me.bristermitten.rewrite.dsl.core.BooleanOperator

interface PlayerActionFilter: ActionFilter<PlayerActionFilter> {

    fun whenIsInWorld(worldName: String): BooleanOperator<PlayerActionFilter>
}
