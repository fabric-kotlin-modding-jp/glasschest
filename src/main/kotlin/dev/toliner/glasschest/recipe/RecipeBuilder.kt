package dev.toliner.glasschest.recipe

internal class RecipeBuilder(private val ingredients: List<RecipeIngredient<*>>) {
    private var recipe: MutableList<MutableList<RecipeIngredient<*>?>> =
        mutableListOf(mutableListOf(null, null, null), mutableListOf(null, null, null), mutableListOf(null, null, null))

    operator fun set(index: Int, value: List<RecipeIngredient<*>?>) {
        recipe[index] = value.toMutableList()
    }

    fun set(value: MutableList<MutableList<RecipeIngredient<*>?>>) {
        recipe = value
    }

    fun build(): Pair<List<List<Char?>>, Map<Char, RecipeIngredient<*>?>> {
        val (normal, reverse) = ingredients.mapIndexed { index, recipeIngredient ->
            index.toString()[0] to recipeIngredient
        }.let { list -> list.toMap() to list.associate { it.second to it.first } }
        val recipe = recipe.map { list -> list.map { ingredient -> ingredient?.let { reverse.getValue(it) } } }
        return recipe to normal
    }
}