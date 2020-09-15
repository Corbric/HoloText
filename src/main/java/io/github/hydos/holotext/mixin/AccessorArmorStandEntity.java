package io.github.hydos.holotext.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.decoration.ArmorStandEntity;

@Mixin(ArmorStandEntity.class)
public interface AccessorArmorStandEntity {
    @Invoker("method_7707")
    void setNoGravity(boolean value);
}
