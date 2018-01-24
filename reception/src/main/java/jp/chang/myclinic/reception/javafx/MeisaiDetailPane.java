package jp.chang.myclinic.reception.javafx;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.MeisaiSectionDTO;
import jp.chang.myclinic.dto.SectionItemDTO;

public class MeisaiDetailPane extends GridPane {

    public MeisaiDetailPane(MeisaiDTO meisai){
        setHgap(4);
        int row = 0;
        for(MeisaiSectionDTO section: meisai.sections){
            Node title = sectionTitle(section);
            setColumnSpan(title, 3);
            add(title, 0, row);
            row += 1;
            for(SectionItemDTO item: section.items){
                Label label = new Label(item.label);
                label.setWrapText(true);
                label.setMaxWidth(200);
                add(label, 1, row);
                Label tankaTimesCount = new Label(String.format("%dx%d =", item.tanka, item.count));
                setHalignment(tankaTimesCount, HPos.RIGHT);
                Label subtotal = new Label(String.format("%,d", item.tanka * item.count));
                setHalignment(subtotal, HPos.RIGHT);
                add(tankaTimesCount, 2, row);
                add(subtotal, 3, row);
                row += 1;
            }
        }
        ColumnConstraints headerColumn = new ColumnConstraints(6);
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints tankaTimesCountColumn = new ColumnConstraints();
        tankaTimesCountColumn.setHalignment(HPos.RIGHT);
        ColumnConstraints subtotalColumn = new ColumnConstraints();
        subtotalColumn.setHalignment(HPos.RIGHT);
        getColumnConstraints().addAll(headerColumn, labelColumn, tankaTimesCountColumn, subtotalColumn);
    }

    private Node sectionTitle(MeisaiSectionDTO section){
        String title = section.label;
        int total = calcSectionTotal(section);
        HBox hbox = new HBox(4);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold");
        hbox.getChildren().addAll(
                titleLabel,
                new Label(String.format("(%d)", total))
        );
        return hbox;
    }

    private int calcSectionTotal(MeisaiSectionDTO section){
        return section.items.stream().map(item -> item.tanka * item.count).reduce((a, b) -> a+b).orElse(0);
    }
}
