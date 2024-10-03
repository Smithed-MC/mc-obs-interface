package net.smithed.obsinterface;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "obs-interface-config", wrapperName = "ObsConfig")
public class ObsConfigModel {
    public String host = "localhost";
    public int port = 4455;
    public String password = null;
}
