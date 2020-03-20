package jp.chang.myclinic.reception.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.stage.Window;
import jp.chang.myclinic.reception.tracker.WebsocketClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Remote {

    private static Logger logger = LoggerFactory.getLogger(Remote.class);
    private String url;
    private WebsocketClient websocketClient;
    private ObjectMapper mapper = new ObjectMapper();

    public Remote(String url) {
        this.url = url;
    }

    private static class SelectorActionCommand {
        public String command;
        public String selector;
    }

    public void start(){
        this.websocketClient = new WebsocketClient(url){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("Remote service connected.");
                Map<String, String> message = new HashMap<>();
                message.put("event", "app-started");
                try {
                    webSocket.send(mapper.writeValueAsString(message));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onNewMessage(String message){
                System.out.println("Remote request received: " + message);
                try {
                    Map<String, Object> cmd = mapper.readValue(message, new TypeReference<Map<String, Object>>(){});
                    Object commandObj = cmd.get("command");
                    if( commandObj instanceof String){
                        String command = (String)commandObj;
                        switch(command){
                            case "click":{
                                SelectorActionCommand clickCommand = mapper.readValue(message, SelectorActionCommand.class);
                                doClick(clickCommand);
                                return;
                            }
                            default: {
                                System.out.println("Cannot handle command: " + message);
                            }
                        }
                    } else {
                        System.out.println("Cannot find command: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    public void shutdown(){
        if( this.websocketClient != null ){
            this.websocketClient.shutdown();
        }
    }

    private Map<String, Object> createEvent(String type){
        Map<String, Object> dict = new HashMap<>();
        dict.put("event", type);
        return dict;
    }

    private void sendEvent(Map<String, Object> event){
        try {
            this.websocketClient.sendMessage(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void onWindowCreated(String className){
        Map<String, Object> event = createEvent("window-created");
        event.put("class-name", className);
        sendEvent(event);
    }

    private String[] subSelectors(String[] selectors){
        String[] sub = new String[selectors.length - 1];
        System.arraycopy(selectors, 1, sub, 0, selectors.length - 1);
        return sub;
    }

    private Object resolveComponent(String[] selectors){
        if(selectors.length > 0){
            String winClass = selectors[0];
            for(Window w: javafx.stage.Window.getWindows()){
                if( w.getClass().getSimpleName().equals(winClass) ){
                    if(selectors.length > 1) {
                        ComponentFinder cf = (ComponentFinder) w;
                        return cf.findComponent(subSelectors(selectors));
                    } else {
                        return w;
                    }
                }
            }
        }
        return null;
    }

    private Object findTargetComponent(String selector){
        return resolveComponent(selector.split("/"));
    }

    private void doClick(SelectorActionCommand cmd){
        Button b = (Button)findTargetComponent(cmd.selector);
        b.fire();
    }

}
