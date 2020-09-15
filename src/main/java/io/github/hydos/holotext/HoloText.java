package io.github.hydos.holotext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.config.Entry;
import io.github.legacy_fabric_community.serialization.json.JanksonOps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class HoloText implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("holotext.json5");
	private static final Jankson JANKSON = Jankson.builder().build();
	private static final Codec<List<Entry>> ENTRIES_CODEC = Codec.list(Entry.CODEC);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting HoloText");
	}

	public static void serialize(final List<Entry> entriesIn) {
		try {
			if (!Files.exists(CONFIG_PATH)) {
				Files.createFile(CONFIG_PATH);
			}
			Files.write(CONFIG_PATH,  EntryHolder.CODEC.encodeStart(JanksonOps.INSTANCE, new EntryHolder(entriesIn)).getOrThrow(false, System.err::println).toJson(true, true).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Entry> deserialize() {
		try {
			return EntryHolder.CODEC.decode(JanksonOps.INSTANCE, JANKSON.load(CONFIG_PATH.toFile())).getOrThrow(false, System.err::println).getFirst().entries;
		} catch (IOException | SyntaxError e) {
			e.printStackTrace();
		}
		return ImmutableList.of();
	}

	public static class EntryHolder {
		public static final Codec<EntryHolder> CODEC = RecordCodecBuilder.create((instance) -> instance.group(ENTRIES_CODEC.optionalFieldOf("entries", ImmutableList.of()).forGetter(entryHolder -> entryHolder.entries)).apply(instance, EntryHolder::new));
		public final List<Entry> entries;

		private EntryHolder(List<Entry> entriesIn) {
			this.entries = entriesIn;
		}
	}
}
