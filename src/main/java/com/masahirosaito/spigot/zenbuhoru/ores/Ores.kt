package com.masahirosaito.spigot.zenbuhoru.ores

import com.masahirosaito.spigot.mscore.utils.getRelatives
import com.masahirosaito.spigot.zenbuhoru.ZenbuHoru
import net.minecraft.server.v1_10_R1.BlockPosition
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class Ores(val block: Block, val plugin: ZenbuHoru) {
    val valid = isValid()
    val type: Material = block.type
    val blocks = if (valid) getRelativeOres(block) else emptySet<Block>()

    fun size(): Int = blocks.size

    fun breakAll(player: Player) {
        blocks.forEach {
            it.setMetadata(plugin.name, FixedMetadataValue(plugin, player))
            (player as CraftPlayer).handle.playerInteractManager.breakBlock(BlockPosition(it.x, it.y, it.z))
            it.removeMetadata(plugin.name, plugin)
        }
    }

    private fun isValid(): Boolean = when (block.type) {
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.REDSTONE_ORE,
        Material.GLOWING_REDSTONE_ORE,
        Material.LAPIS_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE,
        Material.QUARTZ_ORE,
        Material.GLOWSTONE -> true
        else -> false
    }

    private fun getRelativeOres(block: Block): MutableSet<Block> {
        val unCheckedBlocks = mutableSetOf(block)
        val checkedBlocks = mutableSetOf<Block>()

        while (unCheckedBlocks.isNotEmpty()) {
            unCheckedBlocks.first().let { b ->
                unCheckedBlocks.remove(b)
                checkedBlocks.add(b)
                unCheckedBlocks.addAll(b.getRelatives(1)
                        .filter { it.type == type }
                        .filterNot { checkedBlocks.contains(it) })
            }
        }

        return checkedBlocks
    }
}