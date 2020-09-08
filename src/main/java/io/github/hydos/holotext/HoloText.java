package io.github.hydos.holotext;

import java.nio.file.Path;

import blue.endless.jankson.Jankson;
import com.mojang.brigadier.CommandDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import net.fabricmc.loader.api.FabricLoader;

public class HoloText implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("holotext.json5");
	private static final Jankson JANKSON = Jankson.builder().build();
	private static final CommandDispatcher<ServerCommandSource> DISPATCHER = new CommandDispatcher<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Starting HoloText");
	}

	public static CommandDispatcher<ServerCommandSource> getCommandDispatcher() {
		return DISPATCHER;
	}
}
