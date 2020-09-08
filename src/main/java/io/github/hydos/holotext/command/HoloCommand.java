package io.github.hydos.holotext.command;

import java.io.IOException;

import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.HoloText;
import io.github.hydos.holotext.config.Config;
import io.github.hydos.holotext.mixin.ArmorStandEntityAccessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityDispatcher;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import static net.fabricmc.fabric.api.command.v1.CommandManager.argument;
import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class HoloCommand extends AbstractCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("holo")
                        .then(
                                literal("reload")
                                .executes(HoloCommand::reload)
                        )
                        .then(
                                literal("add")
                                .then(
                                        argument("x", IntegerArgumentType.integer())
                                        .then(
                                                argument("y", IntegerArgumentType.integer())
                                                .then(
                                                        argument("z", IntegerArgumentType.integer())
                                                        .then(
                                                                argument("text", StringArgumentType.word())
                                                                .then(
                                                                        argument("name", StringArgumentType.string())
                                                                        .executes(HoloCommand::add)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        try {
            HoloText.deserialize();
        } catch (IOException | SyntaxError e) {
            HoloText.LOGGER.error("Error reloading config");
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    private static int add(CommandContext<ServerCommandSource> ctx) {
        int x = IntegerArgumentType.getInteger(ctx, "x");
        int y = IntegerArgumentType.getInteger(ctx, "y");
        int z = IntegerArgumentType.getInteger(ctx, "z");
        String text = StringArgumentType.getString(ctx, "text");
        String name = StringArgumentType.getString(ctx, "name");
        HoloText.getConfig().getEntries().add(new Config.Entry(new BlockPos(x, y, z), text, name));
        try {
            HoloText.serialize();
        } catch (IOException e) {
            HoloText.LOGGER.error("Error serializing config!");
            e.printStackTrace();
            return 0;
        }
        ArmorStandEntity armorStandEntity = (ArmorStandEntity) EntityDispatcher.createInstanceFromName("ArmorStand", ctx.getSource().getWorld());
        ctx.getSource().getWorld().spawnEntity(armorStandEntity);
        armorStandEntity.setPos(x, y, z);
        ((ArmorStandEntityAccessor) armorStandEntity).invokeMethod_7707(true);
        armorStandEntity.setCustomName(text);
        armorStandEntity.setCustomNameVisible(true);
        armorStandEntity.setInvisible(true);
        armorStandEntity.setSilent(true);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> ctx) {
        String name = StringArgumentType.getString(ctx, "name");
        return 1;
    }

    public HoloCommand() {
        HoloCommand.register(HoloText.getCommandDispatcher());
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "holo";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "command.holo.usage";
    }

    @Override
    public void execute(CommandSource commandSource, String[] args) throws CommandException {
        if (!commandSource.getWorld().isClient) {
            return;
        }
        String command = this.getCommandName() + " " + String.join(" ", args);
        System.out.println(command);
        ServerCommandSource source = new ServerCommandSource(commandSource, commandSource.getPos(), (ServerWorld) commandSource.getWorld(), commandSource.getEntity(), 2);
        try {
            HoloText.getCommandDispatcher().execute(command, source);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            throw new CommandException("command.holo.error");
        }
    }
}
