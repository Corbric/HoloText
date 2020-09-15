package io.github.hydos.holotext.core;

public interface IHoloTextAccess extends ITextReader, ITextWriter, IVectorReader, IVectorWriter, IServerWorldAccess {
    EntityHoloText getHoloText();
}
