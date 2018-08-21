package jp.chang.myclinic.scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.wia.Wia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("スキャナー");
        Wia.CoInitialize();
        MainPane mainPane = new MainPane();
        stage.setScene(new Scene(mainPane));
        stage.showingProperty().addListener((obs, oldValue, newValue) -> {
            if( !newValue ){
                onClosing();
            }
        });
        stage.show();
    }

    private void onClosing() {
        try {
            if( Globals.regularDocSavingDirHint != null ) {
                ScannerSetting.INSTANCE.setRegularDocSavingDirHint(Globals.regularDocSavingDirHint);
            }
        } catch(Exception ex){
            logger.error("Error on onClosing: {}", ex);
        }
    }
}