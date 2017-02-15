package com.masahirosaito.spigot.zenbuhoru.tools

import com.masahirosaito.spigot.mscore.utils.itemInMainHand
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PickAxe(player: Player) {
    val itemStack: ItemStack = player.itemInMainHand()
    val type: Material = itemStack.type

    fun isValid(): Boolean {
        return when (type) {
            Material.DIAMOND_PICKAXE,
            Material.GOLD_PICKAXE,
            Material.IRON_PICKAXE,
            Material.STONE_PICKAXE,
            Material.WOOD_PICKAXE -> true
            else -> false
        }
    }
}