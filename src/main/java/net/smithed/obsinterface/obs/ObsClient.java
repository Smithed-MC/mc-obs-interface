package net.smithed.obsinterface.obs;

import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.OBSRemoteControllerBuilder;
import io.obswebsocket.community.client.model.Scene;

import java.util.List;

public class ObsClient {
    private final OBSRemoteController controller;
    private boolean connected;

    public ObsClient(String host, int port, String password) {
        controller = getBuilder(host, port).password(password).build();
    }

    public ObsClient(String host, int port) {
        controller = getBuilder(host, port).build();
    }

    private OBSRemoteControllerBuilder getBuilder(String host, int port) {
        return OBSRemoteController.builder()
                .host(host)
                .port(port)
                .connectionTimeout(3)
                .lifecycle()
                .onReady(this::onReady)
                .onDisconnect(this::onDisconnect)
                .and();
    }

    private void onReady() {
        connected = true;
    }

    private void onDisconnect() {
        connected = false;
    }

    public void connect() {
        controller.connect();
    }

    public void disconnect() {
        controller.disconnect();
    }

    public boolean isConnected() {
        return connected;
    }

    public List<Scene> getSceneList() {
        return controller.getSceneList(1000L).getScenes();
    }

    public void switchScene(String name) {
       controller.setCurrentProgramScene(name, 1000L);
    }
}
