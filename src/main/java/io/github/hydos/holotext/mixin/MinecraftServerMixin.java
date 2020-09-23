package io.github.hydos.holotext.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At("RETURN"), method = "createCommandManager")
    public void intercept(CallbackInfoReturnable<CommandManager> info) {
    }
}
