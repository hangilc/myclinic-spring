package jp.chang.myclinic.practice.javafx.parts;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.MeisaiSectionDTO;
import jp.chang.myclinic.dto.SectionItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeisaiDisp extends GridPane {

    private static Logger logger = LoggerFactory.getLogger(MeisaiDisp.class);
    private int row = 0;

    public MeisaiDisp(MeisaiDTO meisai) {
        getStyleClass().add("meisai-disp");
        ColumnConstraints headerColumn = new ColumnConstraints(12);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints tankaTimesCountColumn = new ColumnConstraints();
        tankaTimesCountColumn.setHalignment(HPos.RIGHT);
        ColumnConstraints subtotalColumn = new ColumnConstraints();
        subtotalColumn.setHalignment(HPos.RIGHT);
        getColumnConstraints().addAll(headerColumn, labelColumn, tankaTimesCountColumn, subtotalColumn);
        for(MeisaiSectionDTO section: meisai.sections){
            addTitle(section);
            for(SectionItemDTO item: section.items){
                addItem(item);
            }
        }
    }

    private void addTitle(MeisaiSectionDTO section){
        String title = section.label;
        int total = calcSectionTotal(section);
        HBox hbox = new HBox(4);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold");
        hbox.getChildren().addAll(
                titleLabel,
                new Label(String.format("(%d)", total))
        );
        setColumnSpan(hbox, 3);
        add(hbox, 0, row);
        row += 1;
    }

    private void addItem(SectionItemDTO item){
        addItemLabel(item.label);
        addItemTankaCount(item.tanka, item.count);
        addItemTotal(item.tanka * item.count);
        row += 1;
    }

    private void addItemLabel(String text){
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(260);
        add(label, 1, row);
    }

    private void addItemTankaCount(int tanka, int count){
        Label label = new Label(String.format("%dx%d = ", tanka, count));
        label.setMinWidth(Control.USE_PREF_SIZE);
        setValignment(label, VPos.TOP);
        add(label, 2, row);
    }

    private void addItemTotal(int total){
        Label label = new Label(String.format("%,d", total));
        label.setMinWidth(Control.USE_PREF_SIZE);
        setValignment(label, VPos.TOP);
        add(label, 3, row);
    }

    private int calcSectionTotal(MeisaiSectionDTO section){
        return section.items.stream().map(item -> item.tanka * item.count).reduce((a, b) -> a+b).orElse(0);
    }

}
