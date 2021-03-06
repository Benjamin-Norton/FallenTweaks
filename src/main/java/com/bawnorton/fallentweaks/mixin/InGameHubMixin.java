package com.bawnorton.fallentweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.client.model.Texture;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bawnorton.fallentweaks.Global.*;


@Mixin(InGameHud.class)
public abstract class InGameHubMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledHeight;
    @Shadow
    private int scaledWidth;

    @Shadow
    protected abstract void renderScoreboardSidebar(MatrixStack matrices, ScoreboardObjective objective);

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matricies, float tickDelta, CallbackInfo ci) {
        if (renderChatType) {
            this.scaledHeight = this.client.getWindow().getScaledHeight();
            int colour = currentChat.contains("Global") ? 16777215 : currentChat.contains("Kingdom") ? 16777045 : currentChat.contains("Visit") ? 11141290 : currentChat.contains("Staff") ? 16733695 : 43690;
            DrawableHelper.drawCenteredString(matricies, client.textRenderer, currentChat, currentChat.length() * 3, this.scaledHeight - 30, colour);
        }
        if (renderBarracksTime) {
            this.scaledHeight = this.client.getWindow().getScaledHeight();
            this.scaledWidth = this.client.getWindow().getScaledWidth();
            int colour = 16733525;
            double time = (int) (trainTime / 6.0) / 10.0;
            DrawableHelper.drawCenteredString(matricies, client.textRenderer, "Barracks Train Time: " + time + " min(s)", this.scaledWidth - 90, this.scaledHeight - 30, colour);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderScoreboardSidebar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    private void ignoreCall(InGameHud hud, MatrixStack matrices, ScoreboardObjective objective) {
        if (showScoreboard) {
            renderScoreboardSidebar(matrices, objective);
        }
    }
}
