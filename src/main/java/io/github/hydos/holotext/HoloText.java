package io.github.hydos.holotext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.legacy_fabric_community.serialization.json.JanksonOps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class HoloText implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
//	public static Config CONFIG;
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("holotext.json5");
	private static final Jankson JANKSON = Jankson.builder().build();
	private static final Consumer<String> PRINT_TO_STDERR = System.err::println;

	@Override
	public void onInitialize() {
		LOGGER.info("Starting HoloText");
		check();
		deserialize();
	}

	public static void serialize() {
//		try {
//			Files.write(CONFIG_PATH, Config.CODEC.encode(CONFIG, JanksonOps.INSTANCE, JanksonOps.INSTANCE.empty()).getOrThrow(false, PRINT_TO_STDERR).toJson(true, true).getBytes(StandardCharsets.UTF_8));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public static void deserialize() {
//		try {
//			CONFIG = Config.CODEC.decode(JanksonOps.INSTANCE, JANKSON.load(CONFIG_PATH.toFile())).getOrThrow(false, PRINT_TO_STDERR).getFirst();
//		} catch (SyntaxError | IOException e) {
//			e.printStackTrace();
//		}
	}

	public static void check() {
		try {
			if (Files.isDirectory(CONFIG_PATH)) {
				Files.deleteIfExists(CONFIG_PATH);
			}
			if (!Files.exists(CONFIG_PATH)) {
				Files.createFile(CONFIG_PATH);
				JsonObject e = new JsonObject();
				e.put("entries", new JsonObject(), "The data pertaining to each holotext will be stored in this file");
				// you can't stop me ;P
				Files.writeString(CONFIG_PATH, e.toJson(true, true));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
