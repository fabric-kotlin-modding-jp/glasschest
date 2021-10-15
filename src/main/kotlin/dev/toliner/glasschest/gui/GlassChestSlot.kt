package dev.toliner.glasschest.gui

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.item.compat.InventoryFixedWrapper
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv
import dev.toliner.glasschest.inv.GlassChestItemInv
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class GlassChestSlot(private val inventoryOverride: GlassChestItemInv, index: Int, x: Int, y: Int) :
    Slot(emptyInventory, index, x, y) {
    companion object {
        val emptyInventory = object : InventoryFixedWrapper(FullFixedItemInv(0)) {
            override fun canPlayerUse(player: PlayerEntity?): Boolean = true
        }
    }

    override fun getStack(): ItemStack {
        return when (index) {
            0 -> inventoryOverride.attemptAnyExtraction(inventoryOverride.prototype.maxCount, Simulation.SIMULATE)
            else -> ItemStack.EMPTY
        }
    }

    override fun setStack(stack: ItemStack) {
        if (index == 1) {
            inventoryOverride.insert(stack)
        }
        markDirty()
    }

    override fun insertStack(stack: ItemStack, count: Int): ItemStack {
        val remain = inventoryOverride.attemptInsertion(stack, Simulation.ACTION)
        return stack.apply { this.count - count + remain.count }
    }

    override fun getMaxItemCount(): Int = inventoryOverride.maxCapacity

    override fun takeStack(amount: Int): ItemStack {
        return when (index) {
            0 -> inventoryOverride.attemptAnyExtraction(amount, Simulation.ACTION)
            else -> ItemStack.EMPTY
        }
    }

    override fun canInsert(stack: ItemStack): Boolean {
        return inventoryOverride.wouldPartiallyAccept(stack)
    }
}