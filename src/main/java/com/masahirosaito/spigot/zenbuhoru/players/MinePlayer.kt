package com.masahirosaito.spigot.zenbuhoru.players

import com.masahirosaito.spigot.mscore.utils.isCreativeMode
import com.masahirosaito.spigot.mscore.utils.itemInMainHand
import com.masahirosaito.spigot.zenbuhoru.tools.PickAxe
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Statistic
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MinePlayer(val player: Player) {
    val tool = PickAxe(player.itemInMainHand())

    fun isValid(): Boolean = when {
        !tool.isValid() -> false
        else -> true
    }

    fun incrementStatics(statistic: Statistic, material: Material, amount: Int) {
        player.incrementStatistic(statistic, material, amount)
    }

    fun breakItemInMainHand() {
        player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
        player.inventory.itemInMainHand = ItemStack(Material.AIR)
    }

    fun damageToTool(amount: Int) {
        if (tool.damage(amount)) if (tool.isBroken()) breakItemInMainHand()
    }

    fun isCreative() = player.isCreativeMode()

    fun isSneaking() = player.isSneaking
}