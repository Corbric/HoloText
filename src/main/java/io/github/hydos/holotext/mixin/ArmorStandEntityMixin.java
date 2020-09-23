package io.github.hydos.holotext.mixin;

import io.github.hydos.holotext.core.ArmorStandExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin extends LivingEntity implements ArmorStandExtensions {
    @Shadow protected abstract void setNoBasePlate(boolean value);

    @Shadow protected abstract void setNoGravity(boolean value);

    @Shadow protected abstract void setShouldShowName(boolean value);

    private boolean holoText = false;
    private String holoTextId = "";

    private ArmorStandEntityMixin(World world) {
        super(world);
    }

    @Inject(at = @At("TAIL"), method = "serialize")
    public void serializeHoloTextData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("holoText", this.isHoloText());
    }

    @Inject(at = @At("TAIL"), method = "deserialize")
    public void deserializeHoloTextData(CompoundTag tag, CallbackInfo ci) {
        this.setHoloText(tag.getBoolean("holoText"));
    }

    @Override
    public boolean isHoloText() {
        return this.holoText;
    }

    @Override
    public void setHoloText(boolean holoText) {
        this.holoText = holoText;
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setInvulnerable(true);
        this.setNoBasePlate(true);
        this.setShouldShowName(false);
        this.setCustomNameVisible(true);
    }

    @Override
    public String getHoloTextId() {
        return this.holoTextId;
    }

    @Override
    public void setHoloTextId(String holoTextId) {
        this.holoTextId = holoTextId;
    }
}
