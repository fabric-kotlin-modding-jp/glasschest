package dev.toliner.glasschest.gui

import alexiil.mc.lib.attributes.item.ItemInvUtil
import alexiil.mc.lib.attributes.item.compat.FixedInventoryVanillaWrapper
import dev.toliner.glasschest.GlassChestMod
import dev.toliner.glasschest.entity.GlassChestBlockEntity
import dev.toliner.glasschest.inv.GlassChestItemInv
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.math.BlockPos

class GlassChestScreenHandler(syncId: Int, private val entity: GlassChestBlockEntity, playerInventory: PlayerInventory) :
    ScreenHandler(GlassChestMod.glassChestScreenHandler, syncId) {
    constructor(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf) : this(
        syncId,
        buf.readNbt()?.let { nbt ->
            playerInventory.player.world.getBlockEntity(
                BlockPos(
                    nbt.getInt("x"),
                    nbt.getInt("y"),
                    nbt.getInt("z")
                )
            )?.apply { readNbt(nbt) } as? GlassChestBlockEntity
        }!!,
        playerInventory,
    )

    val inventory = entity.inventory

    init {
        addSlot(GlassChestSlot(inventory, 1, 51, 29))
        addSlot(GlassChestSlot(inventory, 0, 115, 29))
        repeat(3) { y ->
            repeat(9) { x ->
                addSlot(Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18))
            }
        }
        repeat(9) {
            addSlot(Slot(playerInventory, it, 8 + it * 18, 142))
        }
    }

    private val playerInventory = FixedInventoryVanillaWrapper(playerInventory)

    override fun canUse(player: PlayerEntity?): Boolean = true

    override fun transferSlot(player: PlayerEntity?, index: Int): ItemStack {
        when (val slot = slots[index]) {
            is GlassChestSlot -> {
                if (slot.index == 0) {
                    ItemInvUtil.move(inventory, playerInventory.groupedInv, inventory.prototype.maxCount)

                }
            }
            else -> {
                slot.stack = inventory.insert(slot.stack)
            }
        }
        playerInventory.markDirty()
        entity.sendInvSync()
        return ItemStack.EMPTY
    }
}
