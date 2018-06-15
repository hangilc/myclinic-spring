package jp.chang.myclinic.recordbrowser.tracking;

import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WebsocketClient extends WebSocketListener {

    //private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);

    private OkHttpClient client;
    private WebSocket websocket;

    public WebsocketClient(String url) {
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        websocket = client.newWebSocket(request, this);
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

    protected void onFail(){

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println("WebSocket failed.");
    }

    public void cancel(){
        websocket.cancel();
    }

    public void shutdown(){
        client.dispatcher().executorService().shutdown();
    }

}
