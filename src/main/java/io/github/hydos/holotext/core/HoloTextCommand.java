package io.github.hydos.holotext.core;

import io.github.hydos.holotext.HoloText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.decoration.ArmorStandEntity;

import static io.github.hydos.holotext.HoloText.argument;
import static io.github.hydos.holotext.HoloText.literal;

public class HoloTextCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "holotext";
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                literal(this.getCommandName())
                        .then(
                                literal("add")
                                        .then(
                                                argument("name", StringArgumentType.string())
                                                        .executes(ctx -> {
                                                            CommandSource s = ctx.getSource();
                                                            ArmorStandEntity entity = new ArmorStandEntity(s.getWorld(), s.getPos().x, s.getPos().y, s.getPos().z);
                                                            HoloTexts.create(entity, StringArgumentType.getString(ctx, "name"));
                                                            s.getWorld().spawnEntity(entity);
                                                            return 1;
                                                        })
                                        )
                        )
        );
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return String.join("\n", HoloText.DISPATCHER.getAllUsage(HoloText.DISPATCHER.getRoot(), source, false));
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        String v = this.getCommandName() + " " + String.join(" ", args);
        try {
            HoloText.DISPATCHER.execute(v, source);
        } catch (CommandSyntaxException e) {
            throw new CommandException("command.holo.error");
        }
    }
}
