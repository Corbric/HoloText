package io.github.hydos.holotext.core;

import net.minecraft.entity.decoration.ArmorStandEntity;

public final class HoloTexts {
    private HoloTexts() {
    }

    public static void create(ArmorStandEntity entity, String name) {
        if (entity instanceof ArmorStandExtensions) {
            ((ArmorStandExtensions) entity).setHoloText(true);
            ((ArmorStandExtensions) entity).setHoloTextId(name);
            return;
        }
        throw new IllegalCallerException("Id10t", new IllegalStateException(new RuntimeException(new IllegalThreadStateException())));
    }
}
