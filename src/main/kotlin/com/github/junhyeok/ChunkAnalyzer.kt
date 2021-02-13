package com.github.junhyeok

import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ChunkAnalyzer: JavaPlugin() {
    companion object {
        lateinit var instance:Plugin
    }

    override fun onEnable() {
        instance = this
        val c = Commands()
        server.getPluginCommand(c.cmd)!!.setExecutor(Commands())
    }
}