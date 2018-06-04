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

// TODO: automatic syncing
// TODO: open patient's history by clicking title
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
        if (params.getUnnamed().size() > 0) {
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
        if (file != null) {
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
                if (file != null) {
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


    private void loadFile(File file) {
        List<PageTag> pageTags = new ArrayList<>();
        RcptDrawer rcptDrawer = new RcptDrawer();
        RcptDataDispatcher dispatcher = new RcptDataDispatcher(rcptDrawer);
        openFile(file, (cmd, arg) -> {
            //noinspection StatementWithEmptyBody
            if ("rcpt_begin".equals(cmd)) {
                // nop
            } else if ("rcpt_end".equals(cmd)) {
                int patientId = rcptDrawer.getPatientId();
                List<List<Op>> pages = rcptDrawer.getPages();
                for (List<Op> page : pages) {
                    PageTag pageTag = new PageTag(page, patientId);
                    pageTags.add(pageTag);
                }
                rcptDrawer.clear();
            } else {
                dispatcher.dispatch(cmd, arg);
            }
        });
        mainRoot.setRcptPages(pageTags);
    }

    private void openFile(File file, BiConsumer<String, String> cb) {
        try {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
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

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

}

