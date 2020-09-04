package io.github.hydos.holotext.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config {

    @SerializedName("holoText")
    public List<HolographicText> holoText;
}
