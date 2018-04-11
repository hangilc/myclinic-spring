package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DrawerPreviewDialogEx<Tag> extends Stage {

    private static Logger logger = LoggerFactory.getLogger(DrawerPreviewDialogEx.class);
    private static final String NO_PRINTER_SETTING = "(手動選択)";
    private ChoiceBox<String> settingChoice;
    private DrawerCanvas drawerCanvas;
    private PrinterEnv printerEnv;
    private List<TaggedPage<Tag>> pages = Collections.emptyList();
    private PageNav pageNav;
    private Parent root;

    public DrawerPreviewDialogEx(double width, double height, double scaleFactor) {
        BorderPane main = new BorderPane();
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
        root.getChildren().addAll(
                createCommands(),
                createCanvas(width, height, scaleFactor)
        );
        main.setTop(createMenu());
        main.setCenter(root);
        setScene(new Scene(main));
        this.root = root;
    }

    private void addStyleClass(String name){
        root.getStyleClass().add(name);
    }

    public void addStylesheet(String path){
        root.getStylesheets().add(path);
    }

    public void setPages(List<TaggedPage<Tag>> pages){
        this.pages = pages;
        pageNav.set(pages.size());
        pageNav.trigger();
    }

    private void showPage(int page){
        if( page >= 0 && page < pages.size() ){
            List<Op> ops = pages.get(page).getOps();
            drawerCanvas.setOps(ops);
        }
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button printButton = new Button("印刷");
        printButton.setOnAction(evt -> doPrint());
        settingChoice = new ChoiceBox<>();
        settingChoice.getStyleClass().add("printer-setting-choice");
        updateSettingChoice();
        setPrinterSettingName(getDefaultPrinterSettingName());
        if( printerEnv == null ){
            settingChoice.setManaged(false);
            settingChoice.setVisible(false);
        }
        this.pageNav = new PageNav(){
            @Override
            protected void onPage(int page) {
                showPage(page);
            }
        };
        hbox.getChildren().addAll(printButton, settingChoice, pageNav);
        return hbox;
    }

    private Node createCanvas(double width, double height, double scaleFactor) {
        drawerCanvas = new DrawerCanvas();
        drawerCanvas.setContentSize(width, height);
        drawerCanvas.setScaleFactor(scaleFactor);
        return new ScrollPane(drawerCanvas);
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        return mbar;
    }

    protected String getDefaultPrinterSettingName() {
        return null;
    }

    protected void setDefaultPrinterSettingName(String newName){
    }

    private void updateSettingChoice(){

    }

    private void setPrinterSettingName(String name){

    }

    private void doPrint(){

    }
}
