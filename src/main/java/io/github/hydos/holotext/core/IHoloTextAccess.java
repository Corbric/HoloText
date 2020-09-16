package io.github.hydos.holotext.core;

import io.github.hydos.holotext.config.Entry;

public interface IHoloTextAccess extends ITextReader, ITextWriter, IVectorReader, IVectorWriter, IServerWorldAccess {
    EntityHoloText getHoloText();

    Entry getEntry();

    default void doNothing() {

    }
}
