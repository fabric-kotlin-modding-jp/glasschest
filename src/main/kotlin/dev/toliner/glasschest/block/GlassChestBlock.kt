package dev.toliner.glasschest.block

import alexiil.mc.lib.attributes.item.entity.ItemEntityAttributeUtil
import dev.toliner.glasschest.entity.GlassChestBlockEntity
import dev.toliner.glasschest.id
import dev.toliner.glasschest.inv.GlassChestItemInv
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.min

class GlassChestBlock(private val capacity: Int, tier: Int) :
    BlockWithEntity(
        FabricBlockSettings.of(Material.STONE).apply {
            sounds(BlockSoundGroup.GLASS)
            hardness(log2(capacity.toFloat()) - 10.5f)
            resistance(8.0f)
            allowsSpawning { _, _, _, _ -> false }
            breakByTool(FabricToolTags.PICKAXES, 1)
            drops(id("glass_chest_$tier"))
        }
    ) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GlassChestBlockEntity(pos, state, GlassChestItemInv(capacity))
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS
        val screenHandlerFactory = state.createScreenHandlerFactory(world, pos)
        if (screenHandlerFactory != null) {
            player.openHandledScreen(screenHandlerFactory)
        }
        return ActionResult.SUCCESS
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, options)
        tooltip.add(LiteralText("Capacity: $capacity").formatted(Formatting.GRAY))
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        super.onBreak(world, pos, state, player)
        val entity = world.getBlockEntity(pos) as? GlassChestBlockEntity ?: return
        val inventory = entity.inventory
        val dropper = ItemEntityAttributeUtil.createItemEntityDropper(world, pos)
        repeat(min(32, ceil(inventory.itemCount.toDouble() / inventory.prototype.maxCount.toDouble()).toInt())) {
            dropper.insert(inventory.extract(inventory.prototype.maxCount))
        }
    }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL
}
