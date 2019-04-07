package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.FrontendAdapter;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.componenttest.CompTest;
import jp.chang.myclinic.practice.componenttest.ComponentTestBase;
import jp.chang.myclinic.practice.javafx.RecordHoken;
import jp.chang.myclinic.practice.javafx.hoken.HokenDisp;
import jp.chang.myclinic.practice.javafx.hoken.HokenSelectForm;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class RecordHokenTest extends ComponentTestBase {

    public RecordHokenTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private RecordHoken createRecord(HokenDTO hoken, VisitDTO visit) {
        RecordHoken record = new RecordHoken(hoken, visit);
        gui(() -> {
            record.setPrefHeight(Region.USE_COMPUTED_SIZE);
            record.setMaxHeight(Region.USE_PREF_SIZE);
            main.setPrefWidth(329);
            main.setPrefHeight(300);
            main.getChildren().setAll(record);
            stage.sizeToScene();
        });
        return record;
    }

    @CompTest
    public void disp() {
        MockData mock = new MockData();
        int patientId = 1;
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.shahokokuho = shahokokuho;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.shahokokuhoId = shahokokuho.shahokokuhoId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        createRecord(hoken, visit);
    }

    @CompTest
    public void editShahokokuho() {
        MockData mock = new MockData();
        int patientId = 1;
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuhoWithShahokokuhoId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.shahokokuho = shahokokuho;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.shahokokuhoId = shahokokuho.shahokokuhoId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        class Local {
            private int currentShahokokuhoId = shahokokuho.shahokokuhoId;
        }
        Local local = new Local();
        Context.frontend = new FrontendAdapter() {
            @Override
            public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
                HokenDTO available = new HokenDTO();
                available.shahokokuho = shahokokuho;
                return value(available);
            }

            @Override
            public CompletableFuture<Void> updateHoken(VisitDTO visit) {
                local.currentShahokokuhoId = visit.shahokokuhoId;
                return value(null);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                HokenDTO available = new HokenDTO();
                if (local.currentShahokokuhoId == shahokokuho.shahokokuhoId) {
                    available.shahokokuho = shahokokuho;
                }
                return value(available);
            }
        };
        RecordHoken record = createRecord(hoken, visit);
        {
            HokenDisp disp = waitFor(record::findDisp);
            gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
            HokenSelectForm form = waitFor(record::findForm);
            gui(() -> {
                form.simulateShahokokuhoSelect(false);
                form.simulateEnterButtonClick();
            });
            HokenDisp disp2 = waitFor(record::findDisp);
            HokenDTO updated = disp2.getHoken();
            confirm(updated.shahokokuho == null && updated.koukikourei == null &&
                    updated.roujin == null && updated.kouhi1 == null &&
                    updated.kouhi2 == null && updated.kouhi3 == null);
        }
        {
            HokenDisp disp = waitFor(record::findDisp);
            gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
            HokenSelectForm form = waitFor(record::findForm);
            gui(() -> {
                form.simulateShahokokuhoSelect(true);
                form.simulateEnterButtonClick();
            });
            HokenDisp disp2 = waitFor(record::findDisp);
            HokenDTO updated = disp2.getHoken();
            confirm(shahokokuho.equals(updated.shahokokuho) && updated.koukikourei == null &&
                    updated.roujin == null && updated.kouhi1 == null &&
                    updated.kouhi2 == null && updated.kouhi3 == null);
        }
    }

}
