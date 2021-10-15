package dev.toliner.glasschest

import alexiil.mc.lib.attributes.Attributes
import com.google.gson.JsonElement
import dev.toliner.glasschest.entity.GlassChestBlockEntity
import dev.toliner.glasschest.gui.GlassChestScreenHandler
import dev.toliner.glasschest.inv.GlassChestItemInv
import dev.toliner.glasschest.item.PortableGlassChestItem
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.blockstate.JBlockModel
import net.devtech.arrp.json.blockstate.JState
import net.devtech.arrp.json.blockstate.JVariant
import net.devtech.arrp.json.lang.JLang
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object GlassChestMod : ModInitializer {

    val group = FabricItemGroupBuilder.build(id("general")) { ItemStack(Items.IRON_PICKAXE) }!!
    internal val resourcePack = RuntimeResourcePack.create("$MOD_ID:runtime")
    internal val localeEn_Us = JLang.lang().apply {
        itemGroup(id("general"), "GlassChest")
    }!!
    internal val localeJa_Jp = JLang.lang().apply {
        itemGroup(id("general"), "GlassChest")
    }

    @JvmStatic
    val recipes = mutableMapOf<Identifier, JsonElement>()
    lateinit var glassChestBlockEntityType: BlockEntityType<GlassChestBlockEntity>
        private set

    @JvmStatic
    val glassChestScreenHandler = ScreenHandlerRegistry.registerExtended(id("glass_chest"), ::GlassChestScreenHandler)!!

    @JvmStatic
    val GLASSCHEST_INV = Attributes.create(GlassChestItemInv::class.java)

    override fun onInitialize() {
        GlassChestBlocks.addGlassChests(1..11)
        GlassChestBlocks.registerGlassChests()
        glassChestBlockEntityType = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            "$MOD_ID:glass_chest_block_entity",
            FabricBlockEntityTypeBuilder.create(::GlassChestBlockEntity).build()
        )

        Registry.register(Registry.ITEM, id("portable_glass_chest"), PortableGlassChestItem(2048))

        resourcePack.addLang(id("en_us"), localeEn_Us)
        resourcePack.addLang(id("ja_jp"), localeJa_Jp)
        RRPCallback.EVENT.register { it.add(resourcePack) }
    }
}