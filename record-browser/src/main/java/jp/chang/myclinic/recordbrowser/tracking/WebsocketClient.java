package jp.chang.myclinic.recordbrowser.tracking;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebsocketClient extends WebSocketListener {

    private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);

    private ScheduledExecutorService timerExecutor;
    private Request request;
    private OkHttpClient client;
    private WebSocket websocket;

    public WebsocketClient(String url, ScheduledExecutorService timerExecutor) {
        this.timerExecutor = timerExecutor;
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        this.request = new Request.Builder()
                .url(url)
                .build();
        this.websocket = client.newWebSocket(request, this);
    }

    protected void onNewMessage(String text){

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        onNewMessage(text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        System.out.println("WebSocket closed.");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        websocket.close(1000, "shutdown");
        if( !(timerExecutor.isShutdown() || timerExecutor.isTerminated()) ) {
            logger.error("Reconnecting websocket 5 seconds later.");
            timerExecutor.schedule(() -> {
                websocket = client.newWebSocket(request, this);
            }, 5, TimeUnit.SECONDS);
        }
    }

    public void sendMessage(String message){
        websocket.send(message);
    }

    public void shutdown(){
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
