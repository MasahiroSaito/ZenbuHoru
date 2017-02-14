package com.masahirosaito.spigot.zenbuhoru.tools

import com.masahirosaito.spigot.mscore.utils.call
import com.masahirosaito.spigot.mscore.utils.damage
import com.masahirosaito.spigot.mscore.utils.isBroken
import com.masahirosaito.spigot.mscore.utils.itemInMainHand
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemBreakEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class PickAxe(val player: Player, val plugin: JavaPlugin) {
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

    fun damage(amount: Int) {
        val damage = calcDamage(amount)
        if (damage <= 0) return

        PlayerItemDamageEvent(player, itemStack, damage).call(plugin).apply {
            if (isCancelled) return
            if (!isUnBreakable()) item.damage(this.damage)
            if (item.isBroken()) {
                PlayerItemBreakEvent(player, item).call(plugin).apply {
                    if (isCancelled) return
                    player.world.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                    player.inventory.itemInMainHand = ItemStack(Material.AIR)
                }
            }
        }
    }

    fun canBeDamaged() = true

    fun isUnBreakable() = if (canBeDamaged()) itemStack.itemMeta.spigot().isUnbreakable else true

    fun calcDamage(amount: Int): Int {
        var damage = amount
        if (hasDurabilityEnchant()) kotlin.repeat(amount) {
            damage -= if (onDamage()) 0 else 1
        }
        return damage
    }

    private fun onDamage() = Random().nextInt(itemStack.getEnchantmentLevel(Enchantment.DURABILITY)) == 0

    private fun hasDurabilityEnchant() = itemStack.containsEnchantment(Enchantment.DURABILITY)
}