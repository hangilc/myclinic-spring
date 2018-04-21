package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PharmaDrugDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PharmaDrugDialog.class);
    private PharmaDrugRoot root;

    public PharmaDrugDialog() {
        setTitle("薬剤情報管理");
        root = new PharmaDrugRoot();
        root.getStylesheets().add("Pharma.css");
        root.getStyleClass().add("pharma-drug-dialog");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenu());
        borderPane.setCenter(root);
        setScene(new Scene(borderPane));
    }

    private Node createMenu(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("アクション");
            {
                MenuItem item = new MenuItem("新規薬剤情報");
                item.setOnAction(evt -> doNew());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private void doNew(){
        NewPharmaDrugDialog newPharmaDrugDialog = new NewPharmaDrugDialog(){
            @Override
            protected void onEnter(int iyakuhincode) {
                root.reloadList();
            }
        };
        newPharmaDrugDialog.show();
    }

}
