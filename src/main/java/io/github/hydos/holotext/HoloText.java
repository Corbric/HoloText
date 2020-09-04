package io.github.hydos.holotext;

import io.github.hydos.holotext.gson.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricCommandRegistry;
import net.swifthq.swiftapi.config.ConfigManager;

public class HoloText implements ModInitializer {

	public Config config;

	@Override
	public void onInitialize() {
		ConfigManager.read("holoText", Config.class);
	}
}
