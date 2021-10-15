package dev.toliner.glasschest

import alexiil.mc.lib.net.impl.McNetworkStack
import dev.toliner.glasschest.entity.GlassChestBlockEntity

object GlassChestNetwork {
    val glassChestNet = McNetworkStack.BLOCK_ENTITY.subType(GlassChestBlockEntity::class.java, id("glass_chest").toString())!!
    val idGlassChestInvSync = glassChestNet.idData("sync_inv").setReceiver(GlassChestBlockEntity::receiveInvSync)!!
}