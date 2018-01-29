package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.PracticeService;

public class MainPane extends BorderPane {

    public MainPane(){
        setTop(createMenu());
        setCenter(createCenter());
    }

    private Node createMenu(){
        MenuBar menuBar = new MenuBar();
        {
            Menu fileMenu = new Menu("ファイル");
            MenuItem endItem = new MenuItem("終了");
            endItem.setOnAction(event -> Platform.exit());
            fileMenu.getItems().addAll(endItem);
            menuBar.getMenus().add(fileMenu);
        }
        {
            Menu selectMenu = new Menu("患者選択");
            MenuItem recentVisitsItem = new MenuItem("最近の診察");
            recentVisitsItem.setOnAction(event -> doRecentVisits());
            selectMenu.getItems().addAll(recentVisitsItem);
            menuBar.getMenus().add(selectMenu);
        }
        return menuBar;
    }

    private Node createCenter(){
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        return root;
    }

    private void doRecentVisits(){
        PracticeService.listRecentVisits(list -> {
            RecentVisitsDialog dialog = new RecentVisitsDialog(list);
            dialog.setCallback(patient -> {
                System.out.println("open: "+patient);
            });
            dialog.show();
        });
    }
}
