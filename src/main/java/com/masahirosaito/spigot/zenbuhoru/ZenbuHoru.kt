package com.masahirosaito.spigot.zenbuhoru

import com.masahirosaito.spigot.mscore.Messenger
import com.masahirosaito.spigot.mscore.utils.register
import com.masahirosaito.spigot.zenbuhoru.listeners.BlockBreakEventListener
import org.bukkit.plugin.java.JavaPlugin

class ZenbuHoru : JavaPlugin() {
    lateinit var messenger: Messenger

    override fun onEnable() {
        messenger = Messenger(this, false)

        BlockBreakEventListener(this).register(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
