package io.github.hydos.holotext.core.command;

import java.util.Objects;

import io.github.hydos.holotext.HoloText;
import io.github.hydos.holotext.core.EntityHoloText;
import com.google.common.collect.Lists;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

public class CommandHolo extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "holo";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "command.holo.usage";
    }

    @Override
    public void execute(CommandSource sourceIn, String[] argsIn) throws CommandException {
        try {
            if (sourceIn.getEntity() instanceof PlayerEntity) {
                String theArg = argsIn[0];
                if (theArg == null) {
                    throw new CommandException("command.holo.error");
                } else {
                    if (theArg.equals("create")) {
                        String name = argsIn[1];
                        if (name == null) {
                            throw new CommandException("command.holo.error");
                        } else {
                            EntityHoloText entity = new EntityHoloText(sourceIn.getWorld());
                            entity.setPosVec(sourceIn.getPos());
                            String[] otherArgs = new String[argsIn.length - 2];
                            System.arraycopy(argsIn, 2, otherArgs, 0, otherArgs.length);
                            entity.setText(Lists.newArrayList(otherArgs));
                            sourceIn.getWorld().spawnEntity(entity);
                            sourceIn.sendMessage(new LiteralText("Successfully added HoloText at position " + entity.getPos().toString()));
                            HoloText.CONFIG.getEntryMap().put(name, entity.getEntry());
                            HoloText.serialize();
                        }
                    } else if (theArg.equals("remove")) {
                        String name = argsIn[1];
                        if (name == null) {
                            throw new CommandException("command.holo.error");
                        } else {
                            if (!HoloText.CONFIG.getEntryMap().containsKey(name)) {
                                throw new CommandException("command.holo.error");
                            } else {
                                if (sourceIn.getWorld().isLoaded(new BlockPos(Objects.requireNonNull(HoloText.CONFIG.getEntryMap().get(name)).getPosVec()), true)) {
                                    sourceIn.getWorld().entities.removeIf((entity) -> entity instanceof ArmorStandEntity && entity.getPos().equals(Objects.requireNonNull(HoloText.CONFIG.getEntryMap().get(name)).getPosVec()));
                                }
                                HoloText.CONFIG.getEntryMap().remove(name);
                                HoloText.serialize();
                            }
                        }
                    }
                }
            } else {
                throw new CommandException("command.holo.error.incorrectEntity");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandException("command.holo.error");
        }
    }

    public static void haveYouReadThisMethodYet() {

    }
}
