package dev.toliner.glasschest.gui

import alexiil.mc.lib.attributes.Simulation
import dev.toliner.glasschest.inv.GlassChestItemInv
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import kotlin.math.min

class PortableGlassChestGuiDescription(
    val inventory: GlassChestItemInv,
    val inventoryItem: ItemStack,
    val playerEntity: PlayerEntity
) : LightweightGuiDescription() {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.apply {
            setSize(265, 240)
            insets = Insets.ROOT_PANEL
            val itemSlot = WItemSlot.of(DummyInventory(inventory.attemptAnyExtraction(64, Simulation.SIMULATE)), 0)
            itemSlot.addChangeListener { _, inventory, _, stack ->
                val dummyInventory = inventory as DummyInventory

            }
        }

    }

    class DummyInventory(initialStack: ItemStack) : Inventory {
        var stack: ItemStack = initialStack
            private set
        var lastCount = stack.count

        override fun clear() {
            stack = ItemStack.EMPTY
        }

        override fun size(): Int = 1

        override fun isEmpty(): Boolean = stack.isEmpty

        override fun getStack(slot: Int): ItemStack = when (slot) {
            0 -> stack
            else -> ItemStack.EMPTY
        }

        override fun removeStack(slot: Int, amount: Int): ItemStack {
            if (slot != 0) return ItemStack.EMPTY
            val actualAmount = min(stack.count, amount)
            stack.count -= actualAmount
            return stack.copy().apply { count = actualAmount }
        }

        override fun removeStack(slot: Int): ItemStack {
            if (slot != 0) return ItemStack.EMPTY
            val ret = stack
            stack = ItemStack.EMPTY
            return ret
        }

        override fun setStack(slot: Int, stack: ItemStack) {
            if (slot != 0) return
            this.stack = stack
        }

        override fun markDirty() {

        }

        override fun canPlayerUse(player: PlayerEntity?): Boolean = true
    }
}