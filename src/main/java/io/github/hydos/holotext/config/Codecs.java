package io.github.hydos.holotext.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;

public interface Codecs {
    Codec<BlockPos> BLOCK_POS_CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Codec.INT.fieldOf("x").forGetter(BlockPos::getX),
                Codec.INT.fieldOf("y").forGetter(BlockPos::getY),
                Codec.INT.fieldOf("z").forGetter(BlockPos::getZ)
        ).apply(instance, BlockPos::new);
    });
}
