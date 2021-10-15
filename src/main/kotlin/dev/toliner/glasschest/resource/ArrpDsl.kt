package dev.toliner.glasschest.resource

import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.blockstate.JBlockModel
import net.devtech.arrp.json.blockstate.JState
import net.devtech.arrp.json.blockstate.JVariant
import net.devtech.arrp.json.loot.JCondition
import net.devtech.arrp.json.loot.JEntry
import net.devtech.arrp.json.loot.JLootTable
import net.devtech.arrp.json.loot.JPool
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.minecraft.util.Identifier

inline fun RuntimeResourcePack.addModel(path: Identifier, parent: String, configure: JModel.() -> Unit = {}) {
    addModel(JModel.model(parent).apply(configure), path)
}

inline fun RuntimeResourcePack.addModel(path: Identifier, parent: Identifier, configure: JModel.() -> Unit = {}) {
    addModel(path, parent.toString(), configure)
}

inline fun JModel.textures(configure: JTextures.() -> Unit): JModel {
    return textures(JTextures().apply(configure))
}

inline fun RuntimeResourcePack.addLootTable(id: Identifier, type: String, configure: JLootTable.() -> Unit = {}) {
    addLootTable(id, JLootTable(type).apply(configure))
}

inline fun JLootTable.pool(configure: JPool.() -> Unit): JLootTable {
    return pool(JPool().apply(configure))
}

inline fun JPool.entry(configure: JEntry.() -> Unit): JPool {
    return entry(JEntry().apply(configure))
}

inline fun JPool.condition(condition: String, configure: JCondition.() -> Unit = {}): JPool {
    return condition(JCondition(condition).apply(configure))
}

inline fun RuntimeResourcePack.addBlockState(path: Identifier, configure: JState.() -> Unit) {
    addBlockState(JState().apply(configure), path)
}

inline fun JState.variant(configure: JVariant.() -> Unit) {
    add(JVariant().apply(configure))
}

operator fun JVariant.set(key: String, modelId: Identifier) {
    put(key, JBlockModel(modelId))
}

fun JVariant.blockModel(id: Identifier, configure: JBlockModel.() -> Unit): JBlockModel {
    return JBlockModel(id).apply(configure)
}
