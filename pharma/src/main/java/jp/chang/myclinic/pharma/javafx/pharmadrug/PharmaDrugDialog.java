package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PharmaDrugDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PharmaDrugDialog.class);

    public PharmaDrugDialog(List<PharmaDrugNameDTO> pharmaDrugNames) {
        setTitle("薬剤情報管理");
        Parent root = new PharmaDrugRoot(pharmaDrugNames);
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
        NewPharmaDrugDialog newPharmaDrugDialog = new NewPharmaDrugDialog();
        newPharmaDrugDialog.show();
    }

}
