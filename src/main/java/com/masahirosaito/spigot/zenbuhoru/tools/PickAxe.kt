package com.masahirosaito.spigot.zenbuhoru.tools

import com.masahirosaito.spigot.mscore.utils.damage
import com.masahirosaito.spigot.mscore.utils.isBroken
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.*

class PickAxe(val itemStack: ItemStack) {
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

    fun damage(amount: Int): Boolean {
        return if (!isUnBreakable()) itemStack.damage(calcDamage(amount)).let { true } else false
    }

    fun canBeDamaged() = true

    fun isUnBreakable() = if (canBeDamaged()) itemStack.itemMeta.spigot().isUnbreakable else true

    fun isBroken() = if (!isUnBreakable()) itemStack.isBroken() else false

    fun calcDamage(amount: Int): Int {
        var damage = amount
        if (hasDurabilityEnchant()) kotlin.repeat(amount) {
            damage -= if (Random().nextInt(itemStack.getEnchantmentLevel(Enchantment.DURABILITY)) == 0) 0 else 1
        }
        return damage
    }

    private fun hasDurabilityEnchant() = itemStack.containsEnchantment(Enchantment.DURABILITY)
}