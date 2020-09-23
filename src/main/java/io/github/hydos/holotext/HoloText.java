package io.github.hydos.holotext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.core.ArmorStandExtensions;
import io.github.hydos.holotext.core.Config;
import io.github.hydos.holotext.core.HoloTextCommand;
import io.github.legacy_fabric_community.serialization.json.JanksonOps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.loader.api.FabricLoader;

public class HoloText implements ModInitializer, ServerEntityEvents.Load {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
	public static final HoloTextCommand HOLOTEXT_CMD = new HoloTextCommand();
	public static Config CONFIG;
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("holotext.json5");
	private static final Jankson JANKSON = Jankson.builder().build();
	private static final Consumer<String> PRINT_TO_STDERR = System.err::println;

	@Override
	public void onInitialize() {
		LOGGER.info("Starting HoloText");
		check();
		deserialize();
		ServerEntityEvents.ENTITY_LOAD.register(this);
		HOLOTEXT_CMD.register(DISPATCHER);
	}

	public static void serialize() {
		try {
			Files.write(CONFIG_PATH, Config.CODEC.encode(CONFIG, JanksonOps.INSTANCE, JanksonOps.INSTANCE.empty()).getOrThrow(false, PRINT_TO_STDERR).toJson(true, true).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deserialize() {
		try {
			CONFIG = Config.CODEC.decode(JanksonOps.INSTANCE, JANKSON.load(CONFIG_PATH.toFile())).getOrThrow(false, PRINT_TO_STDERR).getFirst();
		} catch (SyntaxError | IOException e) {
			e.printStackTrace();
		}
	}

	public static void check() {
		try {
			if (Files.isDirectory(CONFIG_PATH)) {
				Files.deleteIfExists(CONFIG_PATH);
			}
			if (!Files.exists(CONFIG_PATH)) {
				Files.createFile(CONFIG_PATH);
				var e = new JsonObject();
				e.put("entries", new JsonObject(), "The data pertaining to each holotext will be stored in this file");
				Files.writeString(CONFIG_PATH, e.toJson(true, true));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoad(Entity entity, ServerWorld serverWorld) {
		LOGGER.info("an entity loaded");
		if (entity instanceof ArmorStandExtensions) {
			if (!CONFIG.getEntries().stream().map(entry -> entry.getDetails().getUuid().toString()).collect(Collectors.toList()).contains(entity.getUuid().toString())) {
				CONFIG.getEntries().removeIf(entry -> entry.getDetails().getUuid().toString().equals(entity.getUuid().toString()));
			}
		}
	}

	public static LiteralArgumentBuilder<CommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
		return RequiredArgumentBuilder.argument(name, type);
	}
}
