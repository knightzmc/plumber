package me.bristermitten.plumber.struct.builder

import com.google.inject.assistedinject.Assisted
import me.bristermitten.plumber.struct.DataKey
import me.bristermitten.plumber.struct.builder.impl.KeyChangeChooserImpl
import me.bristermitten.plumber.struct.builder.impl.PlayerTaskLengthConfiguration
import me.bristermitten.plumber.struct.player.PPlayer

interface BuilderFactory {
    fun createPlayerConfiguration(@Assisted value: PlayerActionBuilder): PlayerTaskLengthConfiguration


    fun createPlayerActionBuilder(@Assisted player: PPlayer): PlayerActionBuilder


}
