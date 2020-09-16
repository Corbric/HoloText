package io.github.hydos.holotext.core;

import java.util.List;

import io.github.hydos.holotext.config.Entry;
import com.google.common.collect.Lists;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHoloText extends ArmorStandEntity implements IHoloTextAccess {
    private Entry entry;

    public EntityHoloText(World worldIn) {
        super(worldIn);
    }

    public EntityHoloText(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.entry = Entry.of(new Vec3d(x, y, z), Lists.newArrayList(), (ServerWorld) worldIn);
    }

    @Override
    public EntityHoloText getHoloText() {
        return this;
    }

    @Override
    public List<String> getText() {
        return this.entry.getText();
    }

    @Override
    public void setCustomName(String string) {
        super.setCustomName(string);
        this.setText(Lists.newArrayList(string.split("\n")));
    }

    @Override
    public void setText(List<String> textIn) {
        this.entry.setText(textIn);
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        this.entry.setPosVec(new Vec3d(x, y, z));
    }

    @Override
    public void setPosVec(Vec3d posIn) {
        this.setPos(posIn.x, posIn.y, posIn.z);
    }

    @Override
    public Vec3d getPosVec() {
        return this.getPos();
    }

    @Override
    public ServerWorld getServerWorld() {
        return (ServerWorld) this.world;
    }

    @Override
    public Entry getEntry() {
        return this.entry;
    }
}
