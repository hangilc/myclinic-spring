package jp.chang.myclinic.practice.javafx.disease;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseModifyEndReasonDTO;
import jp.chang.myclinic.practice.javafx.disease.end.DateControl;
import jp.chang.myclinic.practice.javafx.disease.end.DiseaseList;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxWithData;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

//import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;

public class End extends VBox {

    private static Logger logger = LoggerFactory.getLogger(End.class);
    private DiseaseList diseaseList;
    private DateForm dateForm;
    private RadioButtonGroup<DiseaseEndReason> reasonGroup;
    private int patientId;

    public End(List<DiseaseFullDTO> diseases, int patientId) {
        super(4);
        this.patientId = patientId;
        setup(diseases);
    }

    private void setup(List<DiseaseFullDTO> diseases) {
        diseaseList = new DiseaseList(diseases) {
            @Override
            protected void onChange(CheckBoxWithData<DiseaseFullDTO> check) {
                doSelectionChange(check);
            }
        };
        Button enterButton = new Button("入力");
        enterButton.setOnAction(evt -> doEnter());
        getChildren().addAll(
                diseaseList,
                createDateInput(),
                createDateControl(),
                createReasonGroup(),
                enterButton
        );
    }

    private Node createDateInput() {
        this.dateForm = new DateForm(Gengou.Recent, Gengou.Current);
        dateForm.setDayLabelClickHandler(this::handleDayLabelClick);
        dateForm.setMonthLabelClickHandler(this::handleMonthLabelClick);
        dateForm.setNenLabelClickHandler(this::handleNenLabelClick);
        return dateForm;
    }

    private void modifyDate(Function<LocalDate, LocalDate> modifier) {
        DateFormInputs inputs = dateForm.getDateFormInputs();
        ErrorMessages em = new ErrorMessages();
        LocalDate date = DateFormLogic.dateFormInputsToLocalDate(inputs, "", em);
        if (em.hasError()) {
            GuiUtil.alertError(em.getMessage());
            return;
        }
        LocalDate newDate = modifier.apply(date);
        DateFormInputs newInputs = DateFormLogic.localDateToDateFormInputs(newDate);
        if (em.hasError()) {
            GuiUtil.alertError(em.getMessage());
            return;
        }
        dateForm.setDateFormInputs(newInputs);
    }

    private void handleDayLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isControlDown()) {
            n = 5;
        }
        if (event.isShiftDown()) {
            n = -n;
        }
        final int nValue = n;
        modifyDate(date -> date.plus(nValue, ChronoUnit.DAYS));
    }

    private void handleMonthLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isShiftDown()) {
            n = -n;
        }
        final int nValue = n;
        modifyDate(date -> date.plus(nValue, ChronoUnit.MONTHS));
    }

    private void handleNenLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isShiftDown()) {
            n = -n;
        }
        final int nValue = n;
        modifyDate(date -> date.plus(nValue, ChronoUnit.YEARS));
    }

    private LocalDate endOfMonth(LocalDate date) {
        return date.withDayOfMonth(1).plus(1, ChronoUnit.MONTHS)
                .minus(1, ChronoUnit.DAYS);
    }

    private LocalDate endOfLastMonth(){
        return LocalDate.now().withDayOfMonth(1).minus(1, ChronoUnit.DAYS);
    }

    private Node createDateControl() {
        DateControl dateControl = new DateControl();
        dateControl.setOnWeekCallback(event -> {
            int n = 1;
            if (event.isShiftDown()) {
                n = -n;
            }
            final int nValue = n;
            modifyDate(date -> date.plus(nValue, ChronoUnit.WEEKS));
        });
        dateControl.setOnTodayCallback(event -> {
            modifyDate(date -> LocalDate.now());
        });
        dateControl.setOnMonthEndCallback(event -> {
            modifyDate(this::endOfMonth);
        });
        dateControl.setOnLastMonthEndCallback(event -> {
            modifyDate(date -> endOfLastMonth());
        });
        return dateControl;
    }

    private Node createReasonGroup() {
        HBox hbox = new HBox(4);
        this.reasonGroup = new RadioButtonGroup<>();
        reasonGroup.createRadioButton("治癒", DiseaseEndReason.Cured);
        reasonGroup.createRadioButton("中止", DiseaseEndReason.Stopped);
        reasonGroup.createRadioButton("死亡", DiseaseEndReason.Dead);
        reasonGroup.setValue(DiseaseEndReason.Cured);
        hbox.getChildren().add(new Label("転機："));
        hbox.getChildren().addAll(reasonGroup.getButtons());
        return hbox;
    }

    private void doSelectionChange(CheckBoxWithData<DiseaseFullDTO> check) {
        if (check.isSelected()) {
            LocalDate startDate = LocalDate.parse(check.getData().disease.startDate);
            if (dateForm.isEmpty()) {
                DateFormInputs inputs = DateFormLogic.localDateToDateFormInputs(startDate);
                dateForm.setDateFormInputs(inputs);
            } else {
                DateFormInputs inputs = dateForm.getDateFormInputs();
                ErrorMessages em = new ErrorMessages();
                LocalDate current = DateFormLogic.dateFormInputsToLocalDate(inputs, "", em);
                if (em.hasNoError()) {
                    if (startDate.isAfter(current)) {
                        DateFormInputs newInputs = DateFormLogic.localDateToDateFormInputs(startDate);
                        if (em.hasNoError()) {
                            dateForm.setDateFormInputs(newInputs);
                        }
                    }
                }
            }
        } else {
            Optional<String> lastDate = diseaseList.getSelected().stream()
                    .map(d -> d.disease.startDate).max(String::compareTo);
            if (lastDate.isPresent()) {
                LocalDate endDate = LocalDate.parse(lastDate.get());
                DateFormInputs inputs = DateFormLogic.localDateToDateFormInputs(endDate);
                dateForm.setDateFormInputs(inputs);
            } else {
                dateForm.clear();
            }
        }
    }

    private void doEnter() {
        ErrorMessages em = new ErrorMessages();
        LocalDate endDate = DateFormLogic.dateFormInputsToLocalDate(dateForm.getDateFormInputs(),
                "終了日", em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        doEnter(endDate);
    }

    private void doEnter(LocalDate endDate) {
        DiseaseEndReason endReason = reasonGroup.getValue();
        List<DiseaseModifyEndReasonDTO> modifies = diseaseList.getSelected().stream()
                .map(d -> PracticeUtil.createDiseaseModifyEndReason(d, endReason, endDate))
                .collect(Collectors.toList());
        if (modifies.size() > 0) {
            Context.frontend.batchUpdateDiseaseEndReason(modifies)
                    .thenCompose(result -> Context.frontend.listCurrentDiseaseFull(patientId))
                    .thenAccept(newDiseases -> Platform.runLater(() -> {
                        resetDiseases(newDiseases);
                        onModified(newDiseases);
                    }))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private void resetDiseases(List<DiseaseFullDTO> diseases) {
        getChildren().clear();
        setup(diseases);
    }

    protected void onModified(List<DiseaseFullDTO> newCurrentDiseases) {

    }

}
