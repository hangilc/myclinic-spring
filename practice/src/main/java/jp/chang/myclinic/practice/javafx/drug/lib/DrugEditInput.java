package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.CheckBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.utilfx.GuiUtil;

public class DrugEditInput extends DrugInput {

    //private static Logger logger = LoggerFactory.getLogger(DrugEditInput.class);
    private int drugId;
    private int visitId;
    private int prescribed;
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    public DrugEditInput() {
        addRow(allFixedCheck);
    }

    public int getDrugId() {
        return drugId;
    }

    @Override
    public void setDrug(DrugFullDTO drugFull) {
        this.drugId = drugFull.drug.drugId;
        this.visitId = drugFull.drug.visitId;
        this.prescribed = drugFull.drug.prescribed;
        if( allFixedCheck.isSelected() ){
            setMaster(drugFull.master);
        } else {
            super.setDrug(drugFull);
        }
    }

    @Override
    public void setExample(PrescExampleFullDTO exampleFull) {
        if( allFixedCheck.isSelected() ){
            setMaster(exampleFull.master);
        } else {
            super.setExample(exampleFull);
        }
    }

    public DrugDTO createDrug() {
        DrugDTO dto = new DrugDTO();
        dto.drugId = drugId;
        dto.visitId = visitId;
        dto.iyakuhincode = getIyakuhincode();
        if (dto.iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が設定されていません。");
            return null;
        }
        try {
            dto.amount = Double.parseDouble(getAmount());
            if (!(dto.amount > 0)) {
                GuiUtil.alertError("用量の値が正でありません。");
                return null;
            }
        } catch (NumberFormatException e) {
            GuiUtil.alertError("用量の入力が不適切です。");
            return null;
        }
        dto.usage = getUsage();
        DrugCategory category = getCategory();
        dto.category = category.getCode();
        if (category == DrugCategory.Gaiyou) {
            dto.days = 1;
        } else {
            try {
                dto.days = Integer.parseInt(getDays());
                if (!(dto.days > 0)) {
                    GuiUtil.alertError("日数の値が正の整数でありません。");
                    return null;
                }
            } catch (NumberFormatException e) {
                GuiUtil.alertError("日数の入力が不敵津です。");
                return null;
            }
        }
        dto.prescribed = prescribed;
        return dto;
    }

}
