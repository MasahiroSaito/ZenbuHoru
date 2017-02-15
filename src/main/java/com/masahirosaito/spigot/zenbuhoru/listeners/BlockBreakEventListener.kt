package com.masahirosaito.spigot.zenbuhoru.listeners

import com.masahirosaito.spigot.mscore.utils.getRemainingDurability
import com.masahirosaito.spigot.zenbuhoru.ZenbuHoru
import com.masahirosaito.spigot.zenbuhoru.ores.Ores
import com.masahirosaito.spigot.zenbuhoru.players.MinePlayer
import com.masahirosaito.spigot.zenbuhoru.tools.PickAxe
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Statistic
import org.bukkit.block.Block
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockBreakEventListener(val plugin: ZenbuHoru) : Listener {

    private fun BlockBreakEvent.cancel() {
        isCancelled = true
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onBreakOre(event: BlockBreakEvent) {
        if (event.isCancelled) return

        val minePlayer = MinePlayer(event.player).apply { if (!isValid()) return }

        val tool = PickAxe(event.player)

        if (event.block.hasMetadata(plugin.name)) {

            if (!tool.isValid()) return event.cancel()

            if (event.expToDrop > 0) {
                minePlayer.spawnExp(event.expToDrop)
                dropExperience += event.expToDrop
                event.expToDrop = 0
            }

        } else {

            if (!tool.isValid()) return

            val ores = Ores(event.block, plugin).apply { if (!valid) return }

            initialize(minePlayer.player, event.block, tool.itemStack)

            ores.breakAll(minePlayer.player)

            val location = minePlayer.location()

            ores.brokenBlocks.forEach {
                location.world.getNearbyEntities(it.location, 1.0, 1.0, 1.0).filter { it is Item }
                        .forEach {
                            (it as Item).apply { world.dropItem(location, it.itemStack) }.remove()
                        }
            }

            breakBlockNum = ores.brokenBlocks.size

            update(minePlayer.player, tool.itemStack)

            event.player.sendMessage(buildString {
                append("${ChatColor.DARK_GREEN}")
                append("━━━━━━━━━━━━━━━━━━━━━━━━━━ MINE DATA ━━━━━━━━━━━━━━━━━━━━━━━━━━")
                append("${ChatColor.RESET}\n")
                append("[Blocks=${ChatColor.BLUE}$breakBlockNum${ChatColor.RESET}] ")
                append("[Exp=${ChatColor.GREEN}$dropExperience${ChatColor.RESET}] ")
                append("[Statistic=${ChatColor.AQUA}$oldStatistic→$newStatistic${ChatColor.RESET}] ")
                append("[Durability=${ChatColor.GOLD}$oldDurability→$newDurability${ChatColor.RESET}] ")
            })
        }
    }

    companion object {
        lateinit var blockType: Material
        var breakBlockNum = 0
        var dropExperience = 0
        var oldStatistic = 0
        var newStatistic = 0
        var oldDurability = 0
        var newDurability = 0

        fun initialize(player: Player, block: Block, item: ItemStack) {
            blockType = block.type
            breakBlockNum = 0
            dropExperience = 0
            oldStatistic = player.getStatistic(Statistic.MINE_BLOCK, blockType)
            oldDurability = item.getRemainingDurability()
        }

        fun update(player: Player, item: ItemStack) {
            newStatistic = player.getStatistic(Statistic.MINE_BLOCK, blockType)
            newDurability = item.getRemainingDurability()
        }
    }
}