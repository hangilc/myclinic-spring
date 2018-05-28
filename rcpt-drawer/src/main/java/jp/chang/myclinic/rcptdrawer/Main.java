package jp.chang.myclinic.rcptdrawer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("レセプト印刷");
        MainRoot root = new MainRoot();
        BorderPane pane = new BorderPane();
        pane.setTop(createMenuBar());
        pane.setCenter(root);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(createFileMenu());
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("ファイル");
        {
            MenuItem item = new MenuItem("開く");
            item.setOnAction(evt -> doOpenFile());
            menu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem("終了");
            item.setOnAction(evt -> Platform.exit());
            menu.getItems().add(item);
        }
        return menu;
    }

    private void doOpenFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
                    while( true ){
                        String line = in.readLine();
                        if( line == null ){
                            break;
                        }
                        System.out.println(line);
                    }
                }
            } catch (IOException ex) {
                logger.error("Failed to open file.", ex);
                GuiUtil.alertError("ファイルを開けませんでした。");
            }
        }
    }

}

