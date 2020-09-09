package io.github.hydos.holotext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.config.Config;
import io.github.hydos.holotext.core.HoloTextEntry;
import io.github.legacy_fabric_community.serialization.json.JanksonOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.fabricmc.loader.api.FabricLoader;

public class HoloText implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("holotext.json5");
	private static final Jankson JANKSON = Jankson.builder().build();
	private static Config config;

	@Override
	public void onInitialize() {
		LOGGER.info("Starting HoloText");
		try {
			deserialize();
		} catch (IOException | SyntaxError e) {
			LOGGER.error("Error deserializing config!");
			e.printStackTrace();
		}
		UseEntityCallback.EVENT.register((playerEntity, world, entity, hitResult) ->{
			if (entity instanceof ArmorStandEntity && config.getEntryList().stream().map(HoloTextEntry::getUuidAsString).filter((str) -> str == null || str.equals("")).collect(Collectors.toSet()).contains(entity.getUuid().toString())) {
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity2, serverWorld) -> {
			if (entity2 instanceof ArmorStandEntity) {
				ArmorStandEntity entity = (ArmorStandEntity) entity2;
				boolean flag = entity.isInvisible();
				flag = flag && entity.isCustomNameVisible();
				flag = flag && entity.hasCustomName();
				flag = flag && entity.isInvulnerableTo(DamageSource.GENERIC);
				flag = flag && entity.method_7712();
				if (flag) {
					serverWorld.removeEntity(entity);
				}
			}
		});
	}

	public static void deserialize() throws IOException, SyntaxError {
		if (!Files.exists(CONFIG_PATH)) {
			Files.createFile(CONFIG_PATH);
			Files.write(CONFIG_PATH, "{\n\"entries\":\n\t[]\n}".getBytes(StandardCharsets.UTF_8));
		}
		JsonObject object = JANKSON.load(CONFIG_PATH.toFile());
		DataResult<Pair<Config, JsonElement>> configDataResult = Config.CODEC.decode(JanksonOps.INSTANCE, object);
		config = configDataResult.getOrThrow(false, System.err::println).getFirst();
	}

	public static void serialize() throws IOException {
		DataResult<JsonElement> result = Config.CODEC.encodeStart(JanksonOps.INSTANCE, config);
		JsonElement json = result.getOrThrow(false, System.err::println);
		Files.write(CONFIG_PATH, json.toJson(true, true).getBytes(StandardCharsets.UTF_8));
	}

	public static Config getConfig() {
		return config;
	}
}
