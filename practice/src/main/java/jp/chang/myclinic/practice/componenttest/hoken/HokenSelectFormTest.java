package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.hoken.HokenSelectForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class HokenSelectFormTest extends ComponentTestBase {

    public HokenSelectFormTest(Stage stage, Pane main) {
        super(stage, main);
    }

    private HokenSelectForm createForm(int visitId, HokenDTO available, HokenDTO current){
        HokenSelectForm form = new HokenSelectForm(visitId, available, current);
        gui(() -> {
            form.setPrefWidth(329);
            form.setPrefHeight(300);
            main.getChildren().setAll(form);
            stage.sizeToScene();
        });
        return form;
    }

    @CompTest
    public void disp(){
        HokenDTO hoken = new HokenDTO();
        MockData mock = new MockData();
        hoken.shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(1);
        createForm(1, hoken, hoken);
    }

    @CompTest
    public void cancel(){
        HokenDTO hoken = new HokenDTO();
        MockData mock = new MockData();
        hoken.shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(1);
        HokenSelectForm form = createForm(1, hoken, hoken);
        class Local {
            private boolean confirmClick;
        }
        Local local = new Local();
        form.setOnCancelHandler(() -> local.confirmClick = true);
        gui(form::simulateCancelButtonClick);
        waitForTrue(() -> local.confirmClick);
    }

    @CompTest
    public void enter(){
        MockData mock = new MockData();
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(1);
        HokenDTO hoken = new HokenDTO();
        hoken.shahokokuho = shahokokuho;
        class Local {
            private boolean confirmClick;
            private int selectedShahokokuhoId;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Void> updateHoken(VisitDTO visit) {
                local.selectedShahokokuhoId = visit.shahokokuhoId;
                return value(null);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                HokenDTO hoken = new HokenDTO();
                if( local.selectedShahokokuhoId == shahokokuho.shahokokuhoId ){
                    hoken.shahokokuho = shahokokuho;
                }
                return value(hoken);
            }
        };
        HokenSelectForm form = createForm(1, hoken, hoken);
        form.setOnEnteredHandler(newHoken -> {
            local.confirmClick = true;
            confirm(Objects.equals(newHoken.shahokokuho, shahokokuho));
        });
        gui(form::simulateEnterButtonClick);
        waitForTrue(() -> local.confirmClick);
    }

    @CompTest
    public void enterNone(){
        MockData mock = new MockData();
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(1);
        HokenDTO hoken = new HokenDTO();
        hoken.shahokokuho = shahokokuho;
        class Local {
            private boolean confirmClick;
            private VisitDTO updateHoken;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter(){
            @Override
            public CompletableFuture<Void> updateHoken(VisitDTO visit) {
                local.updateHoken = VisitDTO.copy(visit);
                return value(null);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                HokenDTO hoken = new HokenDTO();
                if( local.updateHoken.shahokokuhoId == shahokokuho.shahokokuhoId ){
                    hoken.shahokokuho = shahokokuho;
                }
                return value(hoken);
            }
        };
        HokenSelectForm form = createForm(1, hoken, hoken);
        form.setOnEnteredHandler(newHoken -> {
            local.confirmClick = true;
        });
        gui(() -> {
            form.simulateShahokokuhoSelect(false);
            form.simulateEnterButtonClick();
        });
        waitForTrue(() -> local.confirmClick);
        confirm(local.updateHoken != null && local.updateHoken.shahokokuhoId == 0);
    }

}
