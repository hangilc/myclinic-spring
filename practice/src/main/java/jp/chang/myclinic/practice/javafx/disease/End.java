package jp.chang.myclinic.practice.javafx.disease;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseModifyEndReasonDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.end.DateControl;
import jp.chang.myclinic.practice.javafx.disease.end.DiseaseList;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxWithData;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class End extends VBox {

    private static Logger logger = LoggerFactory.getLogger(End.class);
    private DiseaseList diseaseList;
    private DateInput dateInput;
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
        this.dateInput = new DateInput();
        dateInput.setGengou(Gengou.Current);
        dateInput.setDayLabelClickHandler(this::handleDayLabelClick);
        dateInput.setMonthLabelClickHandler(this::handleMonthLabelClick);
        dateInput.setNenLabelClickHandler(this::handleNenLabelClick);
        return dateInput;
    }

    private void handleDayLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isControlDown()) {
            n = 5;
        }
        if (event.isShiftDown()) {
            n = -n;
        }
        dateInput.advanceDay(n);
    }

    private void handleMonthLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isShiftDown()) {
            n = -n;
        }
        dateInput.advanceMonth(n);
    }

    private void handleNenLabelClick(MouseEvent event) {
        int n = 1;
        if (event.isShiftDown()) {
            n = -n;
        }
        dateInput.advanceYear(n);
    }

    private Node createDateControl() {
        DateControl dateControl = new DateControl();
        dateControl.setOnWeekCallback(event -> {
            int n = 1;
            if (event.isShiftDown()) {
                n = -n;
            }
            dateInput.advanceWeek(n);
        });
        dateControl.setOnTodayCallback(event -> {
            dateInput.setValue(LocalDate.now());
        });
        dateControl.setOnMonthEndCallback(event -> {
            dateInput.moveToEndOfMonth();
        });
        dateControl.setOnLastMonthEndCallback(event -> {
            dateInput.moveToEndOfLastMonth();
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
            if (dateInput.isEmpty()) {
                dateInput.setValue(startDate);
            } else {
                Result<LocalDate, List<String>> currentResult = dateInput.getValue();
                if (currentResult.hasValue()) {
                    if (currentResult.getValue().compareTo(startDate) < 0) {
                        dateInput.setValue(startDate);
                    }
                } else {
                    dateInput.setValue(startDate);
                }
            }
        } else {
            Optional<String> lastDate = diseaseList.getSelected().stream()
                    .map(d -> d.disease.startDate).max(String::compareTo);
            if (lastDate.isPresent()) {
                LocalDate endDate = LocalDate.parse(lastDate.get());
                dateInput.setValue(endDate);
            } else {
                dateInput.clear();
            }
        }
    }

    private void doEnter() {
        dateInput.getValue()
                .ifError(e -> GuiUtil.alertError("終了日の設定が不適切です。"))
                .ifPresent(this::doEnter);
    }

    private void doEnter(LocalDate endDate) {
        DiseaseEndReason endReason = reasonGroup.getValue();
        List<DiseaseModifyEndReasonDTO> modifies = diseaseList.getSelected().stream()
                .map(d -> PracticeUtil.createDiseaseModifyEndReason(d, endReason, endDate))
                .collect(Collectors.toList());
        if (modifies.size() > 0) {
            Service.api.batchUpdateDiseaseEndReason(modifies)
                    .thenCompose(result -> Service.api.listCurrentDiseaseFull(patientId))
                    .thenAccept(newDiseases -> Platform.runLater(() -> {
                        resetDiseases(newDiseases);
                        onModified(newDiseases);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void resetDiseases(List<DiseaseFullDTO> diseases) {
        getChildren().clear();
        setup(diseases);
    }

    protected void onModified(List<DiseaseFullDTO> newCurrentDiseases){

    }

}
