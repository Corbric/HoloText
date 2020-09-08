package io.github.hydos.holotext.command;

import java.io.IOException;

import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.HoloText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class HoloCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("holo")
                        .then(literal("reload")
                                .executes(HoloCommand::reload))
        );
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        try {
            HoloText.reload();
        } catch (IOException | SyntaxError e) {
            HoloText.LOGGER.error("Error reloading config");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }
}
