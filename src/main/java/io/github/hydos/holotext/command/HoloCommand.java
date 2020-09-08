package io.github.hydos.holotext.command;

import io.github.hydos.holotext.HoloText;
import io.github.hydos.holotext.core.BrigadierCommand;
import io.github.hydos.holotext.core.HoloTextEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;

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
}
