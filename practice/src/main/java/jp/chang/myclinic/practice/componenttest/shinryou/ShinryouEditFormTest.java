package jp.chang.myclinic.practice.componenttest.shinryou;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.mockdata.SampleShinryouMaster;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEditInput;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouEditForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ShinryouEditFormTest extends ComponentTestBase {

    public ShinryouEditFormTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private ShinryouEditForm createForm(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        ShinryouEditForm form = new ShinryouEditForm(shinryou, attr);
        gui(() -> {
            form.setPrefHeight(Region.USE_COMPUTED_SIZE);
            form.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(300);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;

    }

    @CompTest
    public void disp(){
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Void> setShinryouAttr(int shinryouId, ShinryouAttrDTO attr) {
                System.out.println("Set shinryou attr: " + attr);
                return value(null);
            }

            @Override
            public CompletableFuture<Void> deleteShinryouCascading(int shinryouId) {
                System.out.println("Delete shinryou cascading: " + shinryouId);
                return value(null);
            }
        };
        ShinryouFullDTO shinryou = new ShinryouFullDTO();
        shinryou.master = SampleShinryouMaster.初診;
        shinryou.shinryou = new ShinryouDTO();
        shinryou.shinryou.shinryouId = 1;
        shinryou.shinryou.shinryoucode = shinryou.master.shinryoucode;
        shinryou.shinryou.visitId = 1;
        ShinryouEditForm form = createForm(shinryou, null);
        form.setOnEnteredHandler(attr -> System.out.println("entered: " + attr));
        form.setOnCancelHandler(() -> System.out.println("cancel"));
        form.setOnDeletedHandler(() -> System.out.println("deleted"));
    }

}
