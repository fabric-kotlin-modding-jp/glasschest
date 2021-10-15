package dev.toliner.glasschest.client

import dev.toliner.glasschest.GlassChestMod
import dev.toliner.glasschest.gui.GlassChestScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

object GlassChestModClient: ClientModInitializer {
    override fun onInitializeClient() {
        ScreenRegistry.register(GlassChestMod.glassChestScreenHandler, ::GlassChestScreen)
    }
}