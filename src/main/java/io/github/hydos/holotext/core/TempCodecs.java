package io.github.hydos.holotext.core;

import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public interface TempCodecs {
    Codec<World> WORLD = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.INT.fieldOf("id").forGetter((world) -> world.dimension.getType())).apply(instance, (id) -> {
            for (World world : MinecraftServer.getServer().worlds) {
                if (world.dimension.getType() == id) {
                    return world;
                }
            }
            throw new IllegalStateException("Could not find world with id: " + id);
        });
    });
}
