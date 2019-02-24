package jp.chang.myclinic.practice;

import javafx.stage.Window;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.javafx.MainPane;
import jp.chang.myclinic.practice.javafx.text.TextLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class Globals {

    private static Logger logger = LoggerFactory.getLogger(Globals.class);
    private static Globals INSTANCE = new Globals();
    private MainPane mainPane;

    public static Globals getInstance(){
        return INSTANCE;
    }

    private Globals() {

    }

    public MainPane getMainPane() {
        return mainPane;
    }

    void setMainPane(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    // Window /////////////////////////////////////////////////////
    private Integer nextWindowId = 1;

    public <T extends Window> T findNewWindow(Class<T> windowClass){
        for(Window win: Window.getWindows()){
            if( windowClass.isInstance(win) && win.getUserData() == null ){
                win.setUserData(nextWindowId++);
                return windowClass.cast(win);
            }
        }
        return null;
    }

    public <T extends Window> T findWindowById(int id, Class<T> windowClass){
        for(Window win: Window.getWindows()){
            if( windowClass.isInstance(win) && win.getUserData() != null ){
                Object data = win.getUserData();
                if( data instanceof Integer ){
                    Integer winId = (Integer)data;
                    if( winId.equals(id) ){
                        return windowClass.cast(win);
                    }
                }
            }
        }
        return null;
    }

    private Service.ServerAPI api = Service.api;

    private TextLib defaultTextLib = new TextLib(){
        @Override
        public CompletableFuture<Integer> enterText(TextDTO text) {
            return api.enterText(text);
        }
    };

    public TextLib getTextLib(){
        return defaultTextLib;
    }

}
