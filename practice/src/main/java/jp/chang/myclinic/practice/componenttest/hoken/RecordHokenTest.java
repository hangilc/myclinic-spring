package jp.chang.myclinic.practice.componenttest.hoken;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
        Context.frontend = createFrontend(hoken, hoken);
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

    @CompTest
    public void editKoukikourei() {
        MockData mock = new MockData();
        int patientId = 1;
        KoukikoureiDTO koukikourei = mock.pickKoukikoureiWithKoukikoureiId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.koukikourei = koukikourei;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.koukikoureiId = koukikourei.koukikoureiId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        Context.frontend = createFrontend(hoken, hoken);
        RecordHoken record = createRecord(hoken, visit);
        {
            HokenDisp disp = waitFor(record::findDisp);
            gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
            HokenSelectForm form = waitFor(record::findForm);
            gui(() -> {
                form.simulateKoukikoureiSelect(false);
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
                form.simulateKoukikoureiSelect(true);
                form.simulateEnterButtonClick();
            });
            HokenDisp disp2 = waitFor(record::findDisp);
            HokenDTO updated = disp2.getHoken();
            confirm(updated.shahokokuho == null && koukikourei.equals(updated.koukikourei) &&
                    updated.roujin == null && updated.kouhi1 == null &&
                    updated.kouhi2 == null && updated.kouhi3 == null);
        }
    }

    @CompTest
    public void editKouhi1() {
        MockData mock = new MockData();
        int patientId = 1;
        KouhiDTO kouhi1 = mock.pickKouhiWithKouhiId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.kouhi1 = kouhi1;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.kouhi1Id = kouhi1.kouhiId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        Context.frontend = createFrontend(hoken, hoken);
        RecordHoken record = createRecord(hoken, visit);
        {
            HokenDisp disp = waitFor(record::findDisp);
            gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
            HokenSelectForm form = waitFor(record::findForm);
            gui(() -> {
                form.simulateKouhiSelect(kouhi1.kouhiId, false);
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
                form.simulateKouhiSelect(kouhi1.kouhiId, true);
                form.simulateEnterButtonClick();
            });
            HokenDisp disp2 = waitFor(record::findDisp);
            HokenDTO updated = disp2.getHoken();
            confirm(updated.shahokokuho == null && updated.koukikourei == null &&
                    updated.roujin == null && kouhi1.equals(updated.kouhi1) &&
                    updated.kouhi2 == null && updated.kouhi3 == null);
        }
    }

    @CompTest
    public void editKouhi2() {
        MockData mock = new MockData();
        int patientId = 1;
        KouhiDTO kouhi1 = mock.pickKouhiWithKouhiId(patientId);
        KouhiDTO kouhi2 = mock.pickKouhiWithKouhiId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.kouhi1 = kouhi1;
        hoken.kouhi2 = kouhi2;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.kouhi1Id = kouhi1.kouhiId;
        visit.kouhi2Id = kouhi2.kouhiId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        Context.frontend = createFrontend(hoken, hoken);
        RecordHoken record = createRecord(hoken, visit);
        for (boolean selKouhi1 : List.of(false, true)) {
            for (boolean selKouhi2 : List.of(false, true)) {
                HokenDisp disp = waitFor(record::findDisp);
                gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
                HokenSelectForm form = waitFor(record::findForm);
                gui(() -> {
                    form.simulateKouhiSelect(kouhi1.kouhiId, selKouhi1);
                    form.simulateKouhiSelect(kouhi2.kouhiId, selKouhi2);
                    form.simulateEnterButtonClick();
                });
                HokenDisp disp2 = waitFor(record::findDisp);
                HokenDTO updated = disp2.getHoken();
                KouhiDTO[] resultKouhi = new KouhiDTO[]{null, null};
                int resultIndex = 0;
                if (selKouhi1) {
                    resultKouhi[resultIndex++] = kouhi1;
                }
                if (selKouhi2) {
                    resultKouhi[resultIndex] = kouhi2;
                }
                confirm(isHoken(updated, null, null, resultKouhi[0], resultKouhi[1], null));
            }
        }
    }

    @CompTest
    public void editKouhi3() {
        MockData mock = new MockData();
        int patientId = 1;
        KouhiDTO kouhi1 = mock.pickKouhiWithKouhiId(patientId);
        KouhiDTO kouhi2 = mock.pickKouhiWithKouhiId(patientId);
        KouhiDTO kouhi3 = mock.pickKouhiWithKouhiId(patientId);
        HokenDTO hoken = new HokenDTO();
        hoken.kouhi1 = kouhi1;
        hoken.kouhi2 = kouhi2;
        hoken.kouhi3 = kouhi3;
        VisitDTO visit = new VisitDTO();
        visit.visitId = 1;
        visit.patientId = patientId;
        visit.kouhi1Id = kouhi1.kouhiId;
        visit.kouhi2Id = kouhi2.kouhiId;
        visit.kouhi3Id = kouhi3.kouhiId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
        Context.frontend = createFrontend(hoken, hoken);
        RecordHoken record = createRecord(hoken, visit);
        for (boolean selKouhi1 : List.of(false, true)) {
            for (boolean selKouhi2 : List.of(false, true)) {
                for (boolean selKouhi3 : List.of(false, true)) {
                    HokenDisp disp = waitFor(record::findDisp);
                    gui(() -> disp.fireEvent(createMouseClickedEvent(disp)));
                    HokenSelectForm form = waitFor(record::findForm);
                    gui(() -> {
                        form.simulateKouhiSelect(kouhi1.kouhiId, selKouhi1);
                        form.simulateKouhiSelect(kouhi2.kouhiId, selKouhi2);
                        form.simulateKouhiSelect(kouhi3.kouhiId, selKouhi3);
                        form.simulateEnterButtonClick();
                    });
                    HokenDisp disp2 = waitFor(record::findDisp);
                    HokenDTO updated = disp2.getHoken();
                    KouhiDTO[] resultKouhi = new KouhiDTO[]{null, null, null};
                    int resultIndex = 0;
                    if (selKouhi1) {
                        resultKouhi[resultIndex++] = kouhi1;
                    }
                    if (selKouhi2) {
                        resultKouhi[resultIndex++] = kouhi2;
                    }
                    if (selKouhi3) {
                        resultKouhi[resultIndex] = kouhi3;
                    }
                    confirm(isHoken(updated, null, null, resultKouhi[0], resultKouhi[1], resultKouhi[2]));
                }
            }
        }
    }

    private boolean isHoken(HokenDTO hoken, ShahokokuhoDTO shahokokuho, KoukikoureiDTO koukikourei,
                            KouhiDTO kouhi1, KouhiDTO kouhi2, KouhiDTO kouhi3) {
        return Objects.equals(hoken.shahokokuho, shahokokuho) &&
                Objects.equals(hoken.koukikourei, koukikourei) &&
                hoken.roujin == null &&
                Objects.equals(hoken.kouhi1, kouhi1) &&
                Objects.equals(hoken.kouhi2, kouhi2) &&
                Objects.equals(hoken.kouhi3, kouhi3);
    }

    private Frontend createFrontend(HokenDTO available, HokenDTO current) {
        HokenDTO currentHoken = HokenDTO.copy(available);
        return new FrontendAdapter() {
            @Override
            public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
                return value(available);
            }

            @Override
            public CompletableFuture<Void> updateHoken(VisitDTO visit) {
                if (visit.shahokokuhoId == 0) {
                    currentHoken.shahokokuho = null;
                } else {
                    confirm(visit.shahokokuhoId == available.shahokokuho.shahokokuhoId);
                    currentHoken.shahokokuho = available.shahokokuho;
                }
                if (visit.koukikoureiId == 0) {
                    currentHoken.koukikourei = null;
                } else {
                    confirm(visit.koukikoureiId == available.koukikourei.koukikoureiId);
                    currentHoken.koukikourei = available.koukikourei;
                }
                if (visit.roujinId == 0) {
                    currentHoken.roujin = null;
                } else {
                    confirm(visit.roujinId == available.roujin.roujinId);
                    currentHoken.roujin = available.roujin;
                }
                if (visit.kouhi1Id == 0) {
                    currentHoken.kouhi1 = null;
                } else {
                    currentHoken.kouhi1 = findKouhi(available, visit.kouhi1Id);
                    confirm(current.kouhi1 != null);
                }
                if (visit.kouhi2Id == 0) {
                    currentHoken.kouhi2 = null;
                } else {
                    currentHoken.kouhi2 = findKouhi(available, visit.kouhi2Id);
                    confirm(current.kouhi2 != null);
                }
                if (visit.kouhi3Id == 0) {
                    currentHoken.kouhi3 = null;
                } else {
                    currentHoken.kouhi3 = findKouhi(available, visit.kouhi3Id);
                    confirm(current.kouhi3 != null);
                }
                return value(null);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                return value(HokenDTO.copy(currentHoken));
            }
        };
    }

    private KouhiDTO findKouhi(HokenDTO hoken, int kouhiId) {
        List<KouhiDTO> kouhiList = Stream.of(hoken.kouhi1, hoken.kouhi2, hoken.kouhi3)
                .filter(Objects::nonNull)
                .collect(toList());
        for (KouhiDTO kouhi : kouhiList) {
            if (kouhi != null && kouhi.kouhiId == kouhiId) {
                return kouhi;
            }
        }
        return null;
    }

}
