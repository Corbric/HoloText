package io.github.hydos.holotext.command;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.holotext.HoloText;
import io.github.hydos.holotext.core.BrigadierCommand;
import io.github.hydos.holotext.core.HoloTextEntry;
import io.github.hydos.holotext.util.Boxes;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import static net.fabricmc.fabric.api.command.v1.CommandManager.argument;
import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class HoloCommand extends BrigadierCommand {
    public HoloCommand() {
        super("holo", entity -> entity instanceof PlayerEntity);
    }

    @Override
    protected void initCommands() {
        this.register(literal("add").then(
                argument("text", StringArgumentType.greedyString())
                        .executes(HoloCommand::add)
                )
        );
        this.register(literal("locate").executes(HoloCommand::locate));
        this.register(literal("remove").then(
                argument("uuid", StringArgumentType.string())
                        .executes(HoloCommand::remove)
        ));
        this.register(literal("save").executes(HoloCommand::save));
        this.register(literal("load").executes(HoloCommand::load));
    }

    @Override
    protected String getErrorTranslationKey() {
        return "command.holo.error";
    }

    @Override
    public String getCommandName() {
        return "holo";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "command.holo.usage";
    }

    private void register(LiteralArgumentBuilder<ServerCommandSource> command) {
        this.dispatcher.register(command);
    }

    private static int add(CommandContext<ServerCommandSource> ctx) {
        PlayerEntity source = (PlayerEntity) ctx.getSource().getEntity();
        HoloTextEntry entry = new HoloTextEntry(source.getPos(), StringArgumentType.getString(ctx, "text"), source.getWorld());
        if (entry.create()) {
            source.sendMessage(new LiteralText("Added Holo text!"));
            HoloText.getConfig().getEntryList().add(entry);
            serialize();
            return 1;
        }
        source.sendMessage(new LiteralText("Error spawning armor stand. Check the console for more details."));
        new AssertionError().printStackTrace();
        return 0;
    }

    private static int locate(CommandContext<ServerCommandSource> ctx) {
        List<Entity> entities = ctx.getSource().getWorld().getEntitiesIn(ctx.getSource().getEntity(), Boxes.of(ctx.getSource().getEntity().getBlockPos(), 10), (entity) -> entity instanceof ArmorStandEntity);
        entities.removeIf((entity) -> !(HoloText.getConfig().getEntryList().stream().map(HoloTextEntry::getUuid).collect(Collectors.toList()).contains(entity.getUuid())));
        if (entities.isEmpty()) {
            ctx.getSource().sendFeedback(new LiteralText("§cCould not find any holo texts within ten blocks"));
            return 0;
        } else {
            ctx.getSource().sendFeedback(new LiteralText(String.format("Found %d nearby holo texts", entities.size())));
            int[] a = new int[1];
            entities.forEach((entity) -> {
                a[0]++;
                ctx.getSource().sendFeedback(new LiteralText(String.format("%d. Position: %d %d %d \\n UUID: %s", a[0], entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ(), entity.getUuid().toString())));
            });
        }
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> ctx) {
        String uuid = StringArgumentType.getString(ctx, "uuid");
        HoloText.getConfig().getEntryList().removeIf((entity) -> entity.getUuidAsString().equals(uuid));
        serialize();
        return 1;
    }

    private static int save(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        serialize();
        return 1;
    }

    private static int load(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        deserialize();
        return 1;
    }

    private static void serialize() {
        try {
            HoloText.serialize();
        } catch (IOException e) {
            HoloText.LOGGER.error("Error serializing config!");
            e.printStackTrace();
        }
    }

    private static void deserialize() {
        try {
            HoloText.deserialize();
        } catch (IOException | SyntaxError e) {
            HoloText.LOGGER.error("Error deserializing config!");
            e.printStackTrace();
        }
    }
}
