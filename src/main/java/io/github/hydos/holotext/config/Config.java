package io.github.hydos.holotext.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;

@SuppressWarnings({"Convert2MethodRef", "CodeBlock2Expr"})
public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("enabled").forGetter((config) -> {
            return config.isEnabled();
        }), Codec.list(Entry.CODEC).optionalFieldOf("entries", Lists.newArrayList()).forGetter((config) -> {
            return config.getEntries();
        })).apply(instance, Config::new);
    });

    private final boolean enabled;
    private final List<Entry> entries;

    private Config(boolean enabled, List<Entry> entries) {
        this.enabled = enabled;
        this.entries = entries;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public static class Entry {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codecs.BLOCK_POS_CODEC.fieldOf("pos").forGetter((entry) -> {
                return entry.getPos();
            }), Codec.STRING.fieldOf("text").forGetter((entry) -> {
                return entry.getText();
            }), Codec.STRING.fieldOf("name").forGetter((entry) -> {
                return entry.getName();
            })).apply(instance, Entry::new);
        });

        private final BlockPos pos;
        private final String text;
        private final String name;

        public Entry(BlockPos pos, String text, String name) {
            this.pos = pos;
            this.text = text;
            this.name = name;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public String getText() {
            return this.text;
        }

        public String getName() {
            return this.name;
        }
    }
}
