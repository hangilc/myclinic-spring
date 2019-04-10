package jp.chang.myclinic.practice.guitest.drug;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.guitest.GuiTest;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class DrugEnterFormTest extends GuiTestBase {

    public DrugEnterFormTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private DrugEnterForm createForm(VisitDTO visit){
        DrugEnterForm form = new DrugEnterForm(visit);
        gui(() -> {
            form.setPrefHeight(Region.USE_COMPUTED_SIZE);
            form.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(460);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @GuiTest
    public void disp(){
        MockData mock = new MockData();
        VisitDTO visit = mock.pickVisitWithVisitId(1, LocalDateTime.now());
        createForm(visit);
    }

}
