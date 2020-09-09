package io.github.hydos.holotext.core;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.github.hydos.holotext.mixin.ArmorStandEntityAccessor;
import io.github.legacy_fabric_community.serialization.CommonCodecs;
import javax.annotation.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EntityDispatcher;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HoloTextEntry {
    public static final Codec<HoloTextEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CommonCodecs.VEC_3_D.fieldOf("position").forGetter(HoloTextEntry::getPosition),
            Codec.STRING.fieldOf("text").forGetter(HoloTextEntry::getText),
            TempCodecs.WORLD.fieldOf("world").forGetter(HoloTextEntry::getWorld),
            Codec.STRING.fieldOf("uuid").forGetter(HoloTextEntry::getUuidAsString)
    ).apply(instance, HoloTextEntry::of));

    private final Vec3d position;
    private final String text;
    private final World world;
    @Nullable
    private UUID uuid;

    private static HoloTextEntry of(Vec3d position, String text, World world, String uuid) {
        return new HoloTextEntry(position, text, world, UUID.fromString(uuid));
    }

    public HoloTextEntry(Vec3d position, String text, World world, @Nullable UUID uuid) {
        this.position = position;
        this.text = text;
        this.world = world;
        this.uuid = uuid;
    }

    public HoloTextEntry(Vec3d position, String text, World world) {
        this(position, text, world, null);
    }

    public Vec3d getPosition() {
        return this.position;
    }

    public String getText() {
        return this.text;
    }

    @Nullable
    public UUID getUuid() {
        return this.uuid;
    }

    public String getUuidAsString() {
        return this.getUuid() == null ? "" : this.getUuid().toString();
    }

    public World getWorld() {
        return this.world;
    }

    protected void setUuid(@Nullable UUID uuid) {
        this.uuid = uuid;
    }

    public boolean create() {
        if (this.uuid == null) {
            ArmorStandEntity entity = (ArmorStandEntity) EntityDispatcher.createInstanceFromName("ArmorStand", this.getWorld());
            this.update(entity);
            return this.getWorld().spawnEntity(entity);
        } else {
            AtomicReference<ArmorStandEntity> entityRef = new AtomicReference<>();
            this.getWorld().loadedEntities.forEach((entity1) -> {
                if (entity1.getUuid().equals(this.getUuid()) && entity1 instanceof ArmorStandEntity) {
                    entityRef.set((ArmorStandEntity) entity1);
                }
            });
            if (entityRef.get() != null) {
                ArmorStandEntity entity = entityRef.get();
                this.update(entity);
            } else {
                throw new IllegalStateException("An armor stand without a UUID was found! Please manually remove this from the config: " + this.uuid.toString());
            }
            return false;
        }
    }

    private void update(ArmorStandEntity entity) {
        entity.setInvisible(true);
        entity.setPos(this.getPosition().x, this.getPosition().y, this.getPosition().z);
        entity.setCustomNameVisible(true);
        entity.setCustomName(this.getText());
        entity.setInvulnerable(true);
        ((ArmorStandEntityAccessor) entity).setNoGravity(true);
    }
}
