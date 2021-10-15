package dev.toliner.glasschest.item

import dev.toliner.glasschest.GlassChestMod
import dev.toliner.glasschest.inv.GlassChestItemInv
import dev.toliner.glasschest.useInventory
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class PortableGlassChestItem(private val capacity: Int): Item(FabricItemSettings().apply {
    group(GlassChestMod.group)
    fireproof()
    maxCount(1)
}) {
    override fun onCraft(stack: ItemStack, world: World?, player: PlayerEntity?) {
        val nbt = stack.orCreateNbt
        val inventory = GlassChestItemInv(capacity)
        nbt.put(GlassChestItemInv::class.qualifiedName, inventory.toTag())
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)

        user.sendMessage(Text.of("use"), false)
        return TypedActionResult.pass(stack)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val result = context.stack.useInventory(context.player ?: return ActionResult.PASS) { inventory ->
            if (!inventory.isEmpty && inventory.prototype.item is BlockItem) {
                val result = (inventory.prototype.item as BlockItem).place(ItemPlacementContext(context))
                if (result.isAccepted) {
                    inventory.extract(1)
                }
                result
            } else ActionResult.PASS
        }
        return if (result) ActionResult.SUCCESS else ActionResult.PASS
    }
}
