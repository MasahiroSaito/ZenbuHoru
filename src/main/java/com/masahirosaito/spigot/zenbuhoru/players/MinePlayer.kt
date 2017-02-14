package com.masahirosaito.spigot.zenbuhoru.players

import com.masahirosaito.spigot.mscore.utils.call
import com.masahirosaito.spigot.mscore.utils.isCreativeMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Statistic
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerStatisticIncrementEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class MinePlayer(val player: Player, val plugin: JavaPlugin) {

    fun isValid(): Boolean = when {
        isCreative() -> false
        else -> true
    }

    fun location(): Location = player.location

    fun spawnItem(itemStack: ItemStack) {
        location().world.dropItemNaturally(location(), itemStack)
    }

    fun spawnExp(amount: Int) {
        location().world.spawn(location(), ExperienceOrb::class.java).experience = amount
    }

    fun incrementStatics(statistic: Statistic, material: Material, amount: Int) {
        player.getStatistic(statistic, material).let {
            PlayerStatisticIncrementEvent(player, statistic, it, it + amount, material).call(plugin).apply {
                if (!isCancelled) this.player.incrementStatistic(this.statistic, this.material)
            }
        }
    }

    fun isCreative() = player.isCreativeMode()

    fun isSneaking() = player.isSneaking
}