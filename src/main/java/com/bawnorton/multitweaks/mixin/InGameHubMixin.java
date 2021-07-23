package com.bawnorton.multitweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bawnorton.multitweaks.Global.*;


@Mixin(InGameHud.class)
public abstract class InGameHubMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledHeight;

    @Shadow
    protected abstract void renderScoreboardSidebar(MatrixStack matrices, ScoreboardObjective objective);

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matricies, float tickDelta, CallbackInfo ci) {
        if (renderChatType) {
            this.scaledHeight = this.client.getWindow().getScaledHeight();
            int colour = currentChat.contains("Global") ? 16777215 : currentChat.contains("Kingdom") ? 16777045 : currentChat.contains("Visit") ? 11141290 : 43690;
            DrawableHelper.drawCenteredString(matricies, client.textRenderer, currentChat, currentChat.length() * 3, this.scaledHeight - 30, colour);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderScoreboardSidebar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"))
    private void ignoreCall(InGameHud hud, MatrixStack matrices, ScoreboardObjective objective) {
        if (showScoreboard) {
            renderScoreboardSidebar(matrices, objective);
        }
    }
}