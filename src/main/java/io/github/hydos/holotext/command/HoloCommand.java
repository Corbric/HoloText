package io.github.hydos.holotext.command;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import io.github.hydos.holotext.HoloText;
import io.github.hydos.holotext.core.BrigadierCommand;
import io.github.hydos.holotext.core.HoloTextEntry;
import io.github.hydos.holotext.util.Boxes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.CommonI18n;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;
import static net.fabricmc.fabric.api.command.v1.CommandManager.argument;
import static net.fabricmc.fabric.api.command.v1.CommandManager.literal;

public class HoloCommand extends BrigadierCommand {
    private final CommandDispatcher<ServerCommandSource> dispatcher = new CommandDispatcher<>();

    public HoloCommand() {
        super("holo", entity -> entity instanceof PlayerEntity);
    }

    @Override
    protected void initCommands() {
        this.register(literal("add").then(
                argument("text", StringArgumentType.word())
                        .executes(this::add)
                )
        );
        this.register(literal("locate").executes(this::locate));
        this.register(literal("remove").then(
                argument("uuid", StringArgumentType.string())
                        .executes(this::remove)
        ));
    }

    @Override
    protected CommandDispatcher<ServerCommandSource> getDispatcher() {
        return this.dispatcher;
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

    private int add(CommandContext<ServerCommandSource> ctx) {
        PlayerEntity source = (PlayerEntity) ctx.getSource().getEntity();
        HoloTextEntry entry = new HoloTextEntry(source.getPos(), StringArgumentType.getString(ctx, "text"), source.getWorld());
        if (entry.create()) {
            return 1;
        }
        source.sendMessage(new TranslatableText("comand.holo.add.error"));
        return 0;
    }

    private int locate(CommandContext<ServerCommandSource> ctx) {
        List<Entity> entities = ctx.getSource().getWorld().getEntitiesIn(ctx.getSource().getEntity(), Boxes.of(ctx.getSource().getEntity().getBlockPos(), 10), (entity) -> entity instanceof ArmorStandEntity);
        entities.removeIf((entity) -> !(HoloText.getConfig().getEntryList().stream().map(HoloTextEntry::getUuid).collect(Collectors.toList()).contains(entity.getUuid())));
        if (entities.isEmpty()) {
            ctx.getSource().sendFeedback(new LiteralText(CommonI18n.translate("command.holo.locate.notfound")));
            return 0;
        } else {
            ctx.getSource().sendFeedback(new LiteralText(CommonI18n.translate("command.holo.locate.found", entities.size())));
            int[] a = new int[1];
            entities.forEach((entity) -> {
                a[0]++;
                ctx.getSource().sendFeedback(new LiteralText(CommonI18n.translate("command.holo.locate.found.entry", a[0], entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ(), entity.getUuid().toString())));
            });
        }
        return 1;
    }

    private int remove(CommandContext<ServerCommandSource> ctx) {
        String uuid = StringArgumentType.getString(ctx, "uuid");
        HoloText.getConfig().getEntryList().removeIf((entity) -> entity.getUuidAsString().equals(uuid));
        try {
            HoloText.serialize();
        } catch (IOException e) {
            HoloText.LOGGER.error("Error serializing config!");
            e.printStackTrace();
        }
        return 1;
    }
}
