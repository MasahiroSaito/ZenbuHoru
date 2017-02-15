package com.masahirosaito.spigot.zenbuhoru.players

import com.masahirosaito.spigot.mscore.utils.isCreativeMode
import org.bukkit.Location
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player

class MinePlayer(val player: Player) {

    fun isValid(): Boolean = when {
        isCreative() -> false
        else -> true
    }

    fun location(): Location = player.location

    fun spawnExp(amount: Int) {
        location().world.spawn(location(), ExperienceOrb::class.java).experience = amount
    }

    fun isCreative() = player.isCreativeMode()
}