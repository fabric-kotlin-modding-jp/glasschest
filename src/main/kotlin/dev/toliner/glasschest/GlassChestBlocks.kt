package dev.toliner.glasschest

import dev.toliner.glasschest.block.GlassChestBlock
import dev.toliner.glasschest.resource.*
import net.devtech.arrp.json.blockstate.JBlockModel
import net.devtech.arrp.json.blockstate.JState
import net.devtech.arrp.json.blockstate.JVariant
import net.devtech.arrp.json.loot.JCondition
import net.devtech.arrp.json.loot.JEntry
import net.devtech.arrp.json.loot.JLootTable
import net.devtech.arrp.json.loot.JPool
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry
import kotlin.math.pow

object GlassChestBlocks {
    val glassChests: List<GlassChestBlock>
        get() = glassChestsMutable.toList()

    private val glassChestsMutable = mutableListOf<GlassChestBlock>()

    internal fun addGlassChests(range: IntRange) {
        range.forEach(this::addGlassChest)
    }

    private fun addGlassChest(tier: Int) {
        if (tier < 11) {
            glassChestsMutable.add(GlassChestBlock(2.0.pow((11.0 + 2 * (tier - 1))).toInt(), tier))
        } else {
            glassChestsMutable.add(GlassChestBlock(Int.MAX_VALUE, tier))
        }

        val glassChestId = id("glass_chest_$tier")
        GlassChestMod.localeEn_Us.block(glassChestId, "Glass Chest Tier $tier")
        GlassChestMod.resourcePack.addBlockState(glassChestId) {
            variant {
                this[""] = id("block/glass_chest_$tier")
            }
        }
        GlassChestMod.resourcePack.addModel(id("block/glass_chest_$tier"), "block/cube_column") {
            textures {
                `var`("side", "$MOD_ID:block/glass_chest_${tier}_side")
                `var`("end", "$MOD_ID:block/glass_chest_${tier}_end")
            }
        }
        GlassChestMod.resourcePack.addModel(id("item/glass_chest_$tier"), "$MOD_ID:block/glass_chest_$tier")

        GlassChestMod.resourcePack.addLootTable(glassChestId, "minecraft:block") {
            pool {
                rolls(1)
                entry {
                    type("minecraft:item")
                    name(glassChestId.toString())
                }
                condition("minecraft:survives_explosion")
            }
        }
    }

    internal fun registerGlassChests() {
        glassChests.forEachIndexed { index, glassChestBlock ->
            Registry.register(Registry.BLOCK, id("glass_chest_${index + 1}"), glassChestBlock)
            Registry.register(
                Registry.ITEM,
                id("glass_chest_${index + 1}"),
                BlockItem(glassChestBlock, FabricItemSettings().group(GlassChestMod.group))
            )
        }
    }
}