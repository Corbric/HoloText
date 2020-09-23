package io.github.hydos.holotext.core;

import io.github.hydos.holotext.HoloText;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;

public final class HoloTexts {
    private HoloTexts() {
    }

    public static void create(ArmorStandEntity entity, String name) {
        if (entity instanceof ArmorStandExtensions) {
            ((ArmorStandExtensions) entity).setHoloText(true);
            ((ArmorStandExtensions) entity).setHoloTextId(name);
            HoloText.CONFIG.getEntries().add(new Config.Entry(name, new Config.Entry.Details(entity.getPos(), entity.getWorld().dimension.getType())));
            HoloText.serialize();
            return;
        }
        throw new IllegalCallerException("Id10t", new IllegalStateException(new RuntimeException(new IllegalThreadStateException())));
    }

    public static boolean check(ArmorStandEntity entity) {
        if (entity instanceof ArmorStandExtensions) {
            return ((ArmorStandExtensions) entity).isHoloText();
        }
        throw new IllegalCallerException("Id10t", new IllegalStateException(new RuntimeException(new IllegalThreadStateException())));
    }

    public static String getName(Entity entity) {
        if (entity instanceof ArmorStandExtensions) {
            return ((ArmorStandExtensions) entity).getHoloTextId();
        }
        throw new IllegalCallerException("Id10t", new IllegalStateException(new RuntimeException(new IllegalThreadStateException())));
    }
}
