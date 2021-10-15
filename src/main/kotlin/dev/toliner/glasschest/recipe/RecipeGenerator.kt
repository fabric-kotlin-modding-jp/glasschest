package dev.toliner.glasschest.recipe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.toliner.glasschest.GlassChestMod
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

internal object RecipeGenerator {
    fun addShapedRecipe(id: Identifier, ingredients: List<RecipeIngredient<*>>, output: Item, configure: RecipeBuilder.(List<RecipeIngredient<*>>) -> Unit) {
        val (pattern, patternMap) = RecipeBuilder(ingredients).apply { configure(ingredients) }.build()
        val patternList = patternMap.toList().filter { it.second != null } as  List<Pair<Char, RecipeIngredient<*>>>
        val json = createShapedRecipeJson(
            patternList.map { it.first },
            patternList.map { it.second.id },
            patternList.map { it.second.type },
            pattern.map { list -> list.joinToString(separator = "") { it?.toString() ?: " " } },
            Registry.ITEM.getId(output)
        )
        GlassChestMod.recipes += id to json
    }

    private fun createShapedRecipeJson(keys: List<Char>, items: List<Identifier>, type: List<String>, pattern: List<String>, out: Identifier): JsonObject =
        JsonObject().apply {
            addProperty("type", "minecraft:crafting_shaped")
            add("pattern", JsonArray().apply {
                for (i in 0..2) {
                    add(pattern[i])
                }
            })
            add("key", keys.indices.map {
                keys[it] to JsonObject().apply { addProperty(type[it], items[it].toString()) }
            }.fold(JsonObject()) { r, t ->
                r.apply { add(t.first.toString(), t.second)}
            })
            add("result", JsonObject().apply {
                addProperty("item", out.toString())
                addProperty("count", 1)
            })
        }
}