package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public interface DrugFormGetter {

    int getDrugId();

    int getVisitId();

    int getIyakuhincode();

    String getAmount();

    String getUsage();

    String getDays();

    DrugCategory getCategory();

    default void convertToDrug(BiConsumer<DrugDTO, List<String>> cb) {
        List<String> err = new ArrayList<>();
        DrugDTO drug = new DrugDTO();
        if (getIyakuhincode() > 0) {
            drug.iyakuhincode = getIyakuhincode();
        } else {
            err.add("医薬品が指定されていません。");
        }
        try {
            drug.amount = Double.parseDouble(getAmount());
        } catch (NumberFormatException ex) {
            err.add("用量の入力が適切でありません。");
        }
        if (!getUsage().isEmpty()) {
            drug.usage = getUsage();
        } else {
            err.add("用法が入力されていません。");
        }
        if (getCategory() != null) {
            drug.category = getCategory().getCode();
        } else {
            err.add("Drug category is not specified.");
        }
        try {
            if (getCategory() == DrugCategory.Gaiyou) {
                drug.days = 1;
            } else {
                drug.days = Integer.parseInt(getDays());
            }
        } catch (NumberFormatException ex) {
            err.add("日数・回数の入力が不適切です。");
        }
        if (getVisitId() > 0) {
            drug.visitId = getVisitId();
        } else {
            err.add("Invalid visitId.");
        }
        if (err.size() > 0) {
            cb.accept(null, err);
        } else {
            cb.accept(drug, null);
        }
    }

}

