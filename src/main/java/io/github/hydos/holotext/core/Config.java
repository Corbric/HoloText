package io.github.hydos.holotext.core;

import java.util.List;
import java.util.UUID;

import io.github.legacy_fabric_community.serialization.CommonCodecs;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.Vec3d;

public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Entry.CODEC).fieldOf("entries").forGetter(Config::getEntries)
    ).apply(instance, Config::new));
    private final List<Entry> entries;

    public Config(List<Entry> entries) {
        this.entries = Lists.newArrayList(entries);
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;
        Config config = (Config) o;
        return Objects.equal(this.getEntries(), config.getEntries());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getEntries().toArray());
    }

    public static class Entry {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("name").forGetter(Entry::getName),
                Details.CODEC.fieldOf("details").forGetter(Entry::getDetails)
        ).apply(instance, Entry::new));
        private final String name;
        private final Details details;

        public Entry(String name, Details details) {
            this.name = name;
            this.details = details;
        }


        public String getName() {
            return this.name;
        }

        public Details getDetails() {
            return this.details;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry entry = (Entry) o;
            return Objects.equal(this.getName(), entry.getName()) &&
                    Objects.equal(this.getDetails(), entry.getDetails());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.getName(), this.getDetails());
        }

        public static class Details {
            public static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
            public static final Codec<Details> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    CommonCodecs.VEC_3_D.fieldOf("pos").forGetter(Details::getPos),
                    UUID_CODEC.fieldOf("uuid").forGetter(Details::getUuid),
                    Codec.INT.fieldOf("dimId").forGetter(Details::getDimId)
            ).apply(instance, Details::new));
            private final Vec3d pos;
            private final int dimId;
            private final UUID uuid;

            public Details(Vec3d pos, UUID uuid, int dimId) {
                this.pos = pos;
                this.uuid = uuid;
                this.dimId = dimId;
            }

            public Vec3d getPos() {
                return this.pos;
            }

            public UUID getUuid() {
                return this.uuid;
            }

            public int getDimId() {
                return this.dimId;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Details)) return false;
                var details = (Details) o;
                return this.getDimId() == details.getDimId() &&
                        Objects.equal(this.getPos(), details.getPos());
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(this.getPos(), this.getDimId());
            }
        }
    }
}