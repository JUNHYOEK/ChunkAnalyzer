package com.github.junhyeok

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.Plugin

class Commands: CommandExecutor {

    val cmd = "ca"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (cmd.equals(command.name, true)) {
            if (sender is Player) {
                if (args.size == 2 && isInt(args[0])) {
                    val count = args[0].toInt()
                    if (args[1] == "true") {
                        val ores = ArrayList<Material>()
                        ores.add(Material.COAL_ORE)
                        ores.add(Material.IRON_ORE)
                        ores.add(Material.REDSTONE_ORE)
                        ores.add(Material.LAPIS_ORE)
                        ores.add(Material.GOLD_ORE)
                        ores.add(Material.DIAMOND_ORE)
                        ores.add(Material.EMERALD_ORE)

                        val oreCount = arrayOf(0, 0, 0, 0, 0, 0, 0)

                        val targetChunk = ArrayList<Chunk>()
                        addChunk(sender.chunk, count, targetChunk)

                        sender.sendMessage("청크 분석 시작")

                        val plugin: Plugin = ChunkAnalyzer.instance
                        var i = 0

                        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                            if (i < targetChunk.size) {
                                val chunk = targetChunk[i]
                                for (y in 0..128) {
                                    for (x in 0..15) {
                                        for (z in 0..15) {
                                            val block:Block = chunk.getBlock(x, y, z)
                                            if (!ores.contains(block.type)) {
                                                block.type = Material.AIR
                                            }
                                            else {
                                                when (block.type) {
                                                    ores[0] -> oreCount[0]++
                                                    ores[1] -> oreCount[1]++
                                                    ores[2] -> oreCount[2]++
                                                    ores[3] -> oreCount[3]++
                                                    ores[4] -> oreCount[4]++
                                                    ores[5] -> oreCount[5]++
                                                    ores[6] -> oreCount[6]++
                                                    else -> break
                                                }
                                            }
                                        }
                                    }
                                }
                                i++
                            }
                            else {
                                Bukkit.getScheduler().cancelTasks(plugin)
                                sender.sendMessage("청크 분석 완료")
                                giveBook(ores, oreCount, sender)
                            }
                        },0L,0L)
                    }
                }
            }
        }
        return true
    }

    private fun addChunk(origin:Chunk, count:Int, chunks:ArrayList<Chunk>) {
        val world = origin.world
        val chunkX = origin.x
        val chunkZ = origin.z

        for (x in -count..count) {
            for (z in -count..count) {
                chunks.add(world.getChunkAt(chunkX + x, chunkZ + z))
            }
        }
    }

    private fun giveBook(ores:ArrayList<Material>, oreCount:Array<Int>, player:Player) {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta:BookMeta = book.itemMeta as BookMeta
        bookMeta.title = "청크 분석 보고서"
        bookMeta.author = "ChunkAnalyzer"
        bookMeta.addPage(
                    "${ores[0]}: ${oreCount[0]}\n" +
                    "${ores[1]}: ${oreCount[1]}\n" +
                    "${ores[2]}: ${oreCount[2]}\n" +
                    "${ores[3]}: ${oreCount[3]}\n" +
                    "${ores[4]}: ${oreCount[4]}\n" +
                    "${ores[5]}: ${oreCount[5]}\n" +
                    "${ores[6]}: ${oreCount[6]}\n")
        book.itemMeta = bookMeta
        player.inventory.addItem(book)
    }

    private fun isInt(str:String): Boolean {
        try {
            str.toInt()
        }
        catch (e:Exception) {
            return false
        }

        return true
    }
}