package net.smithed.obsinterface;

import io.wispforest.owo.config.annotation.Config;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.smithed.obsinterface.commands.ObsCommand;
import net.smithed.obsinterface.obs.ObsClient;
import net.smithed.obsinterface.ObsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

public class Obsinterface implements ModInitializer {
    public static final String MOD_ID = "obsinterface";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final ObsConfig CONFIG = ObsConfig.createAndLoad();

    private static ObsClient client;


    public static ObsClient getClient() {
        return client;
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistrationCallback.EVENT.register(ObsCommand::register);

        // TODO: read from config file
        String host = CONFIG.host();
        int port = CONFIG.port();
        String password = CONFIG.password();

        if (password != null)
            client = new ObsClient(host, port, password);
        else
            client = new ObsClient(host, port);

        client.connect();
    }
}