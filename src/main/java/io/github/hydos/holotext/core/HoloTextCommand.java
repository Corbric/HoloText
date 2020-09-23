package io.github.hydos.holotext.core;

import io.github.hydos.holotext.HoloText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.MinecraftServer;

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
                                                        .then(
                                                                argument("text", StringArgumentType.greedyString())
                                                                        .executes(ctx -> {
                                                                            var source = ctx.getSource();
                                                                            var armorStand = new ArmorStandEntity(source.getWorld(), source.getPos().x, source.getPos().y, source.getPos().z);
                                                                            HoloTexts.create(armorStand, StringArgumentType.getString(ctx, "name"));
                                                                            armorStand.setCustomNameVisible(true);
                                                                            armorStand.setCustomName(StringArgumentType.getString(ctx, "text"));
                                                                            source.getWorld().spawnEntity(armorStand);
                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        )
                        .then(
                                literal("remove")
                                        .then(
                                                argument("name", StringArgumentType.string())
                                                        .executes(ctx -> {
                                                            HoloText.CONFIG.getEntries().removeIf((entry) -> {
                                                                boolean ret = entry.getName().equals(StringArgumentType.getString(ctx, "name"));
                                                                var armorStand = MinecraftServer.getServer().getEntity(entry.getDetails().getUuid());
                                                                if (ret && armorStand != null) {
                                                                    armorStand.getWorld().removeEntity(armorStand);
                                                                }
                                                                return ret;
                                                            });
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
        var full = this.getCommandName() + " " + String.join(" ", args);
        try {
            HoloText.DISPATCHER.execute(full, source);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            throw new CommandException("command.holo.error");
        }
    }
}
