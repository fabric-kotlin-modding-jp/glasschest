package dev.toliner.glasschest

import dev.toliner.glasschest.inv.GlassChestItemInv
import dev.toliner.glasschest.item.PortableGlassChestItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

const val MOD_ID = "glasschest"

fun id(name: String) = Identifier(MOD_ID, name)

fun ItemStack.useInventory(player: PlayerEntity, action: (inventory: GlassChestItemInv) -> ActionResult): Boolean {
    val inventory = getInventory() ?: return false
    val result = action(inventory)
    nbt!!.put(GlassChestItemInv::class.qualifiedName, inventory.toTag())
    player.inventory.markDirty()
    return result.isAccepted
}

private fun ItemStack.getInventory(): GlassChestItemInv? {
    if (isEmpty || !hasNbt() || item !is PortableGlassChestItem) return null
    return GlassChestItemInv(Int.MAX_VALUE).apply { fromTag(nbt!!.getCompound(GlassChestItemInv::class.qualifiedName)) }
}
