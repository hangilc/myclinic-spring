package jp.chang.myclinic.hotline.tracker;

import javafx.application.Platform;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class WebsocketClient extends WebSocketListener {

    private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);

    private ScheduledExecutorService timerExecutor;
    private Request request;
    private OkHttpClient client;
    private WebSocket websocket;

    WebsocketClient(String url) {
        this.timerExecutor = Executors.newSingleThreadScheduledExecutor();
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        this.request = new Request.Builder()
                .url(url)
                .build();
        this.websocket = client.newWebSocket(request, this);
    }

    public void sendMessage(String text){
        websocket.send(text);
    }

    protected void onNewMessage(String text){

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        logger.info("message: {}", text);
        onNewMessage(text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        System.out.println("WebSocket closed.");
    }

    protected void onError(String message){
        logger.error(message);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        websocket.close(1000, "shutdown");
        if( !(timerExecutor.isShutdown() || timerExecutor.isTerminated()) ) {
            logger.error("Reconnecting websocket 5 seconds later.");
            Platform.runLater(() -> onError("通信エラー (５秒後に再試行)"));
            timerExecutor.schedule(() -> {
                websocket = client.newWebSocket(request, this);
            }, 5, TimeUnit.SECONDS);
        }
    }

    public void shutdown(){
        timerExecutor.shutdownNow();
        websocket.cancel();
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
