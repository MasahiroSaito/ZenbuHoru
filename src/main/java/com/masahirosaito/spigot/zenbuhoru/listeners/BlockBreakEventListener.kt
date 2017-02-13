package com.masahirosaito.spigot.zenbuhoru.listeners

import com.masahirosaito.spigot.mscore.utils.itemInMainHand
import com.masahirosaito.spigot.zenbuhoru.ZenbuHoru
import com.masahirosaito.spigot.zenbuhoru.ores.Ores
import com.masahirosaito.spigot.zenbuhoru.players.MinePlayer
import net.minecraft.server.v1_10_R1.Item
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.ItemSpawnEvent

class BlockBreakEventListener(val plugin: ZenbuHoru) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onBreakOre(event: BlockBreakEvent) {
        if (event.isCancelled) return

        if (event.block.hasMetadata(plugin.name)) {
            if (event.player.itemInMainHand().type == Material.AIR) {
                event.isCancelled = true
            } else {
                event.player.apply {
                    giveExp(event.expToDrop)
                    world.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1f)
                }
                event.expToDrop = 0
            }
        } else {
            val minePlayer = MinePlayer(event.player).apply { if (!isValid()) return }
            val ores = Ores(event.block, plugin).apply { if (!valid) return }
            ores.breakAll(minePlayer.player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onItemSpawn(event: ItemSpawnEvent) {
        if (event.isCancelled) return
    }
}