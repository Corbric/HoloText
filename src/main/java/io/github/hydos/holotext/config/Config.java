package io.github.hydos.holotext.config;

import java.util.Map;

import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, Entry.CODEC).fieldOf("entries").forGetter(Config::getEntryMap)
    ).apply(instance, Config::new));
    private final HashBiMap<String, Entry> entryMap;

    private Config(Map<String, Entry> entryMap) {
        // We can't trust Mojang because they have ImmutableMaps
        this.entryMap = HashBiMap.create(entryMap);
    }

    public HashBiMap<String, Entry> getEntryMap() {
        return this.entryMap;
    }
}
