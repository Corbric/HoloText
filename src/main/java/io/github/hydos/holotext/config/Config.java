package io.github.hydos.holotext.config;

import java.util.List;

import io.github.hydos.holotext.core.HoloTextEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@SuppressWarnings("CodeBlock2Expr")
public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.list(HoloTextEntry.CODEC).fieldOf("entries").forGetter((config) -> {
            return config.entryList;
        })).apply(instance, Config::new);
    });

    private final List<HoloTextEntry> entryList;

    private Config(List<HoloTextEntry> entryList) {
        this.entryList = entryList;
    }

    public List<HoloTextEntry> getEntryList() {
        return this.entryList;
    }
}
