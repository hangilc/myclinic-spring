package jp.chang.myclinic.tracker;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TrackerWebsocket {

    private static Logger logger = LoggerFactory.getLogger(TrackerWebsocket.class);
    private ScheduledExecutorService timerExecutor;
    private Request request;
    private OkHttpClient client;
    private WebSocket websocket;
    private Consumer<String> messageHandler;
    private boolean isShutDown = false;

    public TrackerWebsocket(String url, Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
        this.timerExecutor = Executors.newSingleThreadScheduledExecutor();
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        this.request = new Request.Builder()
                .url(url)
                .build();
    }

    public void start(Runnable cb) {
        TrackerWebsocket self = this;
        if (websocket == null) {
            client.newWebSocket(request, new WebSocketListener() {

                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    synchronized (self) {
                        if (!isShutDown && TrackerWebsocket.this.websocket == null) {
                            TrackerWebsocket.this.websocket = webSocket;
                        } else {
                            webSocket.cancel();
                        }
                    }
                    if (TrackerWebsocket.this.websocket == webSocket) {
                        logger.info("Tracker connected.");
                        cb.run();
                        webSocket.send("hello");
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    if (!(timerExecutor.isShutdown() || timerExecutor.isTerminated())) {
                        logger.error("Reconnecting websocket 5 seconds later.");
                        timerExecutor.schedule(() -> TrackerWebsocket.this.start(cb), 5, TimeUnit.SECONDS);
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    messageHandler.accept(text);
                }

            });
        }
    }

    public void stop() {
        synchronized (this) {
            if (this.websocket != null) {
                websocket.close(1000, "shutdown");
                this.websocket = null;
            }
        }
    }

    public void shutdown(){
        synchronized(this){
            this.isShutDown = true;
        }
        timerExecutor.shutdownNow();
        synchronized(this) {
            if (websocket != null) {
                websocket.cancel();
            }
        }
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

    public boolean isConnected(){
        return websocket != null;
    }

}
