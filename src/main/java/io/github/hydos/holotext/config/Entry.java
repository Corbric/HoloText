package io.github.hydos.holotext.config;

import java.util.List;

import io.github.hydos.holotext.core.IServerWorldAccess;
import io.github.hydos.holotext.core.ITextReader;
import io.github.hydos.holotext.core.ITextWriter;
import io.github.hydos.holotext.core.IVectorReader;
import io.github.hydos.holotext.core.IVectorWriter;
import io.github.legacy_fabric_community.serialization.CommonCodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class Entry implements IVectorReader, IServerWorldAccess, ITextReader, IVectorWriter, ITextWriter {
    public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CommonCodecs.VEC_3_D.fieldOf("pos").forGetter(Entry::getPosVec),
            Codec.list(Codec.STRING).fieldOf("text").forGetter(Entry::getText),
            CodecWorld.WORLD.fieldOf("world").forGetter(Entry::getServerWorld)
    ).apply(instance, Entry::of));

    private Vec3d pos;
    private List<String> text;
    private final ServerWorld world;

    private Entry(Vec3d posIn, List<String> textIn, ServerWorld worldIn) {
        this.pos = posIn;
        this.text = textIn;
        this.world = worldIn;
    }

    @Override
    public Vec3d getPosVec() {
        return this.pos;
    }

    @Override
    public List<String> getText() {
        return this.text;
    }

    @Override
    public ServerWorld getServerWorld() {
        return this.world;
    }

    @Override
    public void setPosVec(Vec3d posIn) {
        this.pos = posIn;
    }

    @Override
    public void setText(List<String> textIn) {
        this.text = textIn;
    }

    public static Entry of(Vec3d posIn, List<String> textIn, ServerWorld worldIn) {
        return new Entry(posIn, textIn, worldIn);
    }
}
