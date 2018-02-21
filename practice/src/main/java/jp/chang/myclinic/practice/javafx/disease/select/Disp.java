package jp.chang.myclinic.practice.javafx.disease.select;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.util.DiseaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disp extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(Disp.class);

    private Text nameText = new Text("");
    private Text startDateText = new Text("");
    private Text endReasonText = new Text("");
    private Text endDateText = new Text("");
    private DiseaseFullDTO disease;

    public Disp() {
        rightAlignFirstColumn();
        addRow("名前：", new TextFlow(nameText));
        addRow("開始日：", new TextFlow(startDateText));
        addRow("転機：", new TextFlow(endReasonText));
        addRow("終了日：", new TextFlow(endDateText));
    }

    private void setName(String value){
        this.nameText.setText(value);
    }
    private void setStartDate(String value){
        this.startDateText.setText(value);
    }
    private void setEndReason(String value){
        this.endReasonText.setText(value);
    }
    private void setEndDate(String value){
        this.endDateText.setText(value);
    }

    public void setDisease(DiseaseFullDTO disease){
        this.disease = disease;
        setName(DiseaseUtil.getFullName(disease));
        setStartDate(DiseaseUtil.formatDateAsKanji(disease.disease.startDate));
        setEndReason(DiseaseEndReason.fromCode(disease.disease.endReason).getKanjiRep());
        setEndDate(DiseaseUtil.formatDateAsKanji(disease.disease.endDate));
    }

    public DiseaseFullDTO getDisease() {
        return disease;
    }
}
