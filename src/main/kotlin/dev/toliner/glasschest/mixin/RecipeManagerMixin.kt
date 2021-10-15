package dev.toliner.glasschest.mixin

import com.google.gson.JsonElement
import dev.toliner.glasschest.GlassChestMod
import net.minecraft.recipe.RecipeManager
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(RecipeManager::class)
@Suppress("unused")
class RecipeManagerMixin {
    @Inject(method = ["apply"], at = [At("HEAD")])
    fun interceptApply(map: MutableMap<Identifier, JsonElement>, resourceManager: ResourceManager, profiler: Profiler, info: CallbackInfo) {
        map += GlassChestMod.recipes
    }
}