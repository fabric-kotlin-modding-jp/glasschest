package dev.toliner.glasschest.inv

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.item.GroupedItemInv
import alexiil.mc.lib.attributes.item.GroupedItemInvView
import alexiil.mc.lib.attributes.item.ItemStackCollections
import alexiil.mc.lib.attributes.item.ItemStackUtil
import alexiil.mc.lib.attributes.item.filter.ItemFilter
import alexiil.mc.lib.attributes.misc.Saveable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import kotlin.math.min

class GlassChestItemInv(maxCapacity: Int) : GroupedItemInv, Saveable {
    var maxCapacity: Int = maxCapacity
        private set
    var itemCount: Int = 0
        private set
    var prototype: ItemStack = ItemStack.EMPTY
        private set

    override fun getStoredStacks(): MutableSet<ItemStack> {
        return ItemStackCollections.set().apply { add(prototype.copy().apply { count = itemCount }) }
    }

    override fun getTotalCapacity(): Int = maxCapacity

    override fun getStatistics(filter: ItemFilter): GroupedItemInvView.ItemInvStatistic {
        return if (!filter.matches(prototype)) {
            GroupedItemInvView.ItemInvStatistic(
                filter,
                0,
                0,
                0
            )
        } else {
            GroupedItemInvView.ItemInvStatistic(
                filter,
                itemCount,
                totalCapacity - itemCount,
                totalCapacity
            )
        }
    }

    override fun attemptInsertion(stack: ItemStack, simulation: Simulation): ItemStack {
        if (!(prototype.isEmpty || ItemStack.canCombine(stack, prototype))) {
            return stack
        }
        val insertCount = min(stack.count, totalCapacity - itemCount)
        if (simulation.isAction) {
            itemCount += insertCount
        }
        if (insertCount > 0 && prototype.isEmpty) {
            prototype = stack.copy()
        }
        return stack.apply { count -= insertCount }
    }

    override fun attemptExtraction(filter: ItemFilter, amount: Int, simulation: Simulation): ItemStack {
        if (!filter.matches(prototype)) {
            return ItemStack.EMPTY
        }
        val extractCount = min(min(amount, itemCount), prototype.maxCount)
        val ret = prototype.copy().apply {
            count = extractCount
        }
        if (simulation.isAction) {
            itemCount -= extractCount
            if (itemCount == 0) {
                prototype = ItemStack.EMPTY
            }
        }
        return ret
    }

    override fun toTag(tag: NbtCompound): NbtCompound {
        val data = NbtCompound()
        data.put("prototype", prototype.writeNbt(NbtCompound()))
        data.putInt("count", itemCount)
        data.putInt("capacity", maxCapacity)
        tag.put(GlassChestItemInv::class.qualifiedName, data)
        return tag
    }

    override fun fromTag(tag: NbtCompound) {
        val data = tag.getCompound(GlassChestItemInv::class.qualifiedName) ?: return
        this.prototype = ItemStack.fromNbt(data.getCompound("prototype"))
        this.itemCount = data.getInt("count")
        this.maxCapacity = data.getInt("capacity")
    }

    override fun isEmpty(): Boolean = prototype.isEmpty

    fun clear() {
        prototype = ItemStack.EMPTY
        itemCount = 0
    }
}
