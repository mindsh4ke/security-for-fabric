package net.mindshake.securityforfabric.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mindshake.securityforfabric.Main;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TurretScreen extends HandledScreen<TurretScreenHandler> {

    private final Identifier TEXTURE = new Identifier(Main.MODID, "textures/gui/turret_gui.png");

    CyclingButtonWidget<Boolean> attackOtherPlayersButton;

    public TurretScreen(TurretScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.attackOtherPlayersButton = addDrawableChild(
CyclingButtonWidget.onOffBuilder(false).narration(button -> ScreenTexts.joinSentences(button.getGenericNarrationMessage(), new TranslatableText("selectWorld.allowCommands.info"))).build(x, y, 150, 20, new TranslatableText("selectWorld.allowCommands"), (button, cheatsEnabled) -> {

}));

    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
