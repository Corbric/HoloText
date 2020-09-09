package io.github.hydos.holotext.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Boxes {
    public static Box of(BlockPos pos, int range) {
        return new Box(
                pos.getX() - range,
                pos.getY() - range,
                pos.getZ() - range,
                pos.getX() + range,
                pos.getY() + range,
                pos.getZ() + range
        );
    }
}
