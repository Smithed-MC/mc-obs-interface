package net.smithed.obsinterface.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.obswebsocket.community.client.model.Scene;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.smithed.obsinterface.Obsinterface;
import net.smithed.obsinterface.obs.ObsClient;

import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ObsCommand {

    private static int listScenes(CommandContext<ServerCommandSource> context) {
        ObsClient client = Obsinterface.getClient();
        ServerCommandSource source = context.getSource();

        if (!client.isConnected()) {
            source.sendFeedback(
                    () -> Text.literal("OBS is not connected"),
                    false
            );
            return 0;
        }

        List<Scene> sceneList = client.getSceneList();

        String response = "Scenes: " + String.join(
                ", ",
                sceneList
                        .stream()
                        .map(Scene::getSceneName)
                        .toList()
        );

        source.sendFeedback(
                () -> Text.literal(response),
                false
        );

        return 1;
    }

    private static int switchScene(CommandContext<ServerCommandSource> context) {
        ObsClient client = Obsinterface.getClient();
        ServerCommandSource source = context.getSource();

        if (!client.isConnected()) {
            source.sendFeedback(
                    () -> Text.literal("OBS is not connected"),
                    false
            );
            return 0;
        }

        String scene = getString(context, "scene");
        client.switchScene(scene);

        return 1;
    }

    private static LiteralArgumentBuilder<ServerCommandSource> getScene() {
        var scene = literal("scene");

        return scene
                .then(literal("list").executes(ObsCommand::listScenes))
                .then(literal("switch")
                        .then(argument("scene", StringArgumentType.greedyString())
                                .executes(ObsCommand::switchScene)
                        )
                );
    }

    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        LiteralArgumentBuilder<ServerCommandSource> root = literal("obs");

        dispatcher.register(root
                .requires(source -> source.hasPermissionLevel(2))
                .then(getScene())
        );
    }
}
