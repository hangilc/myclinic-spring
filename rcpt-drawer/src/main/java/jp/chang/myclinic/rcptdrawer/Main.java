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
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class Main extends Application {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    private Stage stage;
    private MainRoot mainRoot;

    @Override
    public void start(Stage stage) throws Exception {
        Parameters params = getParameters();
        File file = null;
        if( params.getUnnamed().size() > 0 ){
            file = new File(params.getUnnamed().get(0));
        }
        this.stage = stage;
        stage.setTitle("レセプト印刷");
        this.mainRoot = new MainRoot();
        BorderPane pane = new BorderPane();
        pane.setTop(createMenuBar());
        pane.setCenter(mainRoot);
        stage.setScene(new Scene(pane));
        stage.show();
        if( file != null ){
            loadFile(file);
        }
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
            item.setOnAction(evt -> {
                File file = chooseFile();
                if( file != null ){
                    loadFile(file);
                }
            });
            menu.getItems().add(item);
        }
        {
            MenuItem item = new MenuItem("終了");
            item.setOnAction(evt -> Platform.exit());
            menu.getItems().add(item);
        }
        return menu;
    }

    private static Pattern sep = Pattern.compile("\\s+");


    private void loadFile(File file){
        List<List<List<Op>>> rcptPages = new ArrayList<>();
        RcptDrawer rcptDrawer = new RcptDrawer();
        class Local {
            RcptDataDispatcher dispatcher = null;
            DispatchHook hook = null;
        }
        Local local = new Local();
        openFile(file, (cmd, arg) -> {
            if( "rcpt_begin".equals(cmd) ){
                local.hook = new StoringDispatchHook();
                local.dispatcher = new RcptDataDispatcher(rcptDrawer, local.hook);
            } else if( "rcpt_end".equals(cmd) ){
                rcptPages.add(rcptDrawer.getPages());
                rcptDrawer.clear();
                local.dispatcher = null;
            } else {
                local.dispatcher.dispatch(cmd, arg);
            }
        });
        mainRoot.setRcptPages(rcptPages);
    }

    private void openFile(File file, BiConsumer<String, String> cb){
        try {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
                while( true ){
                    String line = in.readLine();
                    if( line == null ){
                        break;
                    }
                    String[] toks = sep.split(line.trim(), 2);
                    cb.accept(toks[0], toks.length >= 2 ? toks[1] : null);
                }
            }
        } catch (IOException ex) {
            logger.error("Failed to open file.", ex);
            GuiUtil.alertError("ファイルを開けませんでした。");
        }
    }

    private File chooseFile(){
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

//    private void doOpenFile() {
//        List<List<List<Op>>> rcptPages = new ArrayList<>();
//        class Local {
//            RcptDrawer rcptDrawer = null;
//            RcptDataDispatcher dispatcher = null;
//            DispatchHook hook = null;
//        }
//        Local local = new Local();
//        chooseFile((cmd, arg) -> {
//            if( "rcpt_begin".equals(cmd) ){
//                if( local.rcptDrawer != null ){
//                    System.err.println("Internal error.");
//                    System.exit(1);
//                }
//                local.rcptDrawer = new RcptDrawer();
//                local.hook = new StoringDispatchHook();
//                local.dispatcher = new RcptDataDispatcher(local.rcptDrawer, local.hook);
//            } else if( "rcpt_end".equals(cmd) ){
//                rcptPages.add(local.rcptDrawer.getPages());
//                local.rcptDrawer = null;
//                local.dispatcher = null;
//            } else {
//                local.dispatcher.dispatch(cmd, arg);
//            }
//        });
//        mainRoot.setRcptPages(rcptPages);
//    }

//    private void chooseFile(BiConsumer<String, String> cb){
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            try {
//                try (BufferedReader in = new BufferedReader(
//                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
//                    while( true ){
//                        String line = in.readLine();
//                        if( line == null ){
//                            break;
//                        }
//                        String[] toks = sep.split(line.trim(), 2);
//                        cb.accept(toks[0], toks.length >= 2 ? toks[1] : null);
//                    }
//                }
//            } catch (IOException ex) {
//                logger.error("Failed to open file.", ex);
//                GuiUtil.alertError("ファイルを開けませんでした。");
//            }
//        }
//    }

}

