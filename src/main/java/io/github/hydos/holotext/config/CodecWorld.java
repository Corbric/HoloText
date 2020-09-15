package io.github.hydos.holotext.config;

import java.util.Objects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.Dimension;

public interface CodecWorld {
    Codec<ServerWorld> WORLD = Codec.STRING.comapFlatMap(Converter::toWorld, Converter::fromWorld);

    class Converter {
        private static final BiMap<String, Dimension> DIM_STRING_OBJECT_MAP = HashBiMap.create();
        private static final BiMap<Dimension, ServerWorld> DIM_OBJECT_WORLD_MAP = HashBiMap.create();

        private static String fromWorld(ServerWorld world) {
            return DIM_STRING_OBJECT_MAP.inverse().get(DIM_OBJECT_WORLD_MAP.inverse().get(world));
        }

        private static DataResult<ServerWorld> toWorld(String name) {
            return DataResult.success(Objects.requireNonNull(DIM_OBJECT_WORLD_MAP.get(DIM_STRING_OBJECT_MAP.get(name))));
        }

        static {
            for (ServerWorld world : MinecraftServer.getServer().worlds) {
                DIM_STRING_OBJECT_MAP.put(world.dimension.getName(), world.dimension);
                DIM_OBJECT_WORLD_MAP.put(world.dimension, world);
            }
        }
    }
}
