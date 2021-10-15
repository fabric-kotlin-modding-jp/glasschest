package dev.toliner.glasschest.entity

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProviderBlockEntity
import alexiil.mc.lib.net.IMsgReadCtx
import alexiil.mc.lib.net.IMsgWriteCtx
import alexiil.mc.lib.net.NetByteBuf
import alexiil.mc.lib.net.NetIdDataK
import alexiil.mc.lib.net.impl.CoreMinecraftNetUtil
import dev.toliner.glasschest.GlassChestMod
import dev.toliner.glasschest.GlassChestNetwork
import dev.toliner.glasschest.gui.GlassChestScreenHandler
import dev.toliner.glasschest.inv.GlassChestItemInv
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos

class GlassChestBlockEntity(
    pos: BlockPos,
    state: BlockState?,
    val inventory: GlassChestItemInv = GlassChestItemInv(2048)
) : BlockEntity(GlassChestMod.glassChestBlockEntityType, pos, state),
    AttributeProviderBlockEntity,
    ExtendedScreenHandlerFactory
{
    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        inventory.fromTag(nbt)
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        inventory.toTag(nbt)
        return nbt
    }

    override fun addAllAttributes(to: AttributeList<*>) {
        to.offer(inventory)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler {
        return GlassChestScreenHandler(syncId, this, inv)
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey)
    }

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeNbt(this.writeNbt(NbtCompound()))
    }

    internal fun sendInvSync() {
        if (world?.isClient ?: return) {
            listOf(CoreMinecraftNetUtil.getClientConnection())
        } else {
            CoreMinecraftNetUtil.getPlayersWatching(world, pos)
        }.forEach { connection ->
            GlassChestNetwork.idGlassChestInvSync.send(connection, this
            ) { obj, buffer, _ -> buffer.writeNbt(obj.writeNbt(NbtCompound())) }
        }
    }

    internal fun receiveInvSync(buf: NetByteBuf, ctx: IMsgReadCtx) {
        readNbt(buf.readNbt()!!)
        markDirty()
    }
}
