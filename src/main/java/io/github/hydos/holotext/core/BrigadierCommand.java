package io.github.hydos.holotext.core;

import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.CommonI18n;

import net.fabricmc.fabric.api.command.v1.ServerCommandSource;

/**
 * A Command implementation that uses Brigadier to execute
 */
public abstract class BrigadierCommand extends AbstractCommand {
    protected final String commandName;
    protected Predicate<Entity> entityPredicate = entity -> true;
    protected final CommandDispatcher<ServerCommandSource> dispatcher = new CommandDispatcher<>();

    public BrigadierCommand(String name) {
        this.commandName = name;
        this.initCommands();
    }

    public BrigadierCommand(String name, Predicate<Entity> entityPredicate) {
        this(name);
        this.entityPredicate = entityPredicate;
    }

    protected abstract void initCommands();

    protected abstract String getErrorTranslationKey();

    @Override
    public final void execute(CommandSource source, String[] args) throws CommandException {
        if (source.getWorld().isClient) {
            return;
        } else if (!this.entityPredicate.test(source.getEntity())) {
            throw new CommandException(CommonI18n.translate("command." + this.getCommandName() + ".error.incorrectEntity"));
        }
        ServerCommandSource serverCommandSource = new ServerCommandSource(source, source.getPos(), (ServerWorld) source.getWorld(), source.getEntity(), this.getPermissionLevel());
        try {
            this.dispatcher.execute(String.join(" ", args), serverCommandSource);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            throw new CommandException(this.getErrorTranslationKey());
        }
    }

    @Override
    public String getCommandName() {
        return this.commandName;
    }
}
