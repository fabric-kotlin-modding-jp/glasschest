package dev.toliner.glasschest.gui

import com.mojang.blaze3d.systems.RenderSystem
import dev.toliner.glasschest.id
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class GlassChestScreen(handler: GlassChestScreenHandler, inventory: PlayerInventory, title: Text) :
    HandledScreen<GlassChestScreenHandler>(handler, inventory, title) {
    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, id("textures/gui/container/glasschest.png"))
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
        val text = "${handler.inventory.itemCount}/${handler.inventory.maxCapacity}"
        textRenderer.draw(
            matrices,
            text,
            ((width - backgroundWidth) / 2 + 136 - textRenderer.getWidth(text)).toFloat(),
            (height - backgroundHeight) / 2 + 60.0f,
            Formatting.BLACK.colorValue!!
        )
    }

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }
}
