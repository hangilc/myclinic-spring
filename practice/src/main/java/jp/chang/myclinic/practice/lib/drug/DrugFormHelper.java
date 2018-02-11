package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.lib.PracticeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DrugFormHelper {

    private static DecimalFormat decimalFormatter = new DecimalFormat("###.##");

    public static void clear(DrugFormSetter setter, DrugInputConstraints constraints) {
        setter.setIyakuhincode(0);
        setter.setDrugName("");
        if (!constraints.isAmountFixed()) {
            setter.setAmount("");
        }
        setter.setAmountUnit("");
        if (!constraints.isUsageFixed()) {
            setter.setUsage("");
        }
        if (!constraints.isDaysFixed()) {
            setter.setDays("");
        }
        setter.setComment("");
    }

    public static void setMaster(DrugFormSetter setter, IyakuhinMasterDTO master, DrugFormGetter getter,
                                 DrugInputConstraints constraints) {
        setter.setIyakuhincode(master.iyakuhincode);
        setter.setDrugName(master.name);
        if (!constraints.isAmountFixed()) {
            setter.setAmount("");
        }
        setter.setAmountUnit(master.unit);
        if (!constraints.isUsageFixed()) {
            setter.setUsage("");
        }
        if (!constraints.isDaysFixed()) {
            setter.setDays("");
        }
        setter.setCategory(PracticeUtil.zaikeiToCategory(master.zaikei));
        setter.setComment("");
    }

    public static void setDrug(DrugFormSetter setter, DrugFullDTO drugFull, DrugFormGetter getter,
                               DrugInputConstraints constraints) {
        setMaster(setter, drugFull.master, getter, constraints);
        DrugDTO drug = drugFull.drug;
        if (!constraints.isAmountFixed() || getter.getAmount().isEmpty()) {
            setter.setAmount(decimalFormatter.format(drug.amount));
        }
        if (!constraints.isUsageFixed() || getter.getUsage().isEmpty()) {
            setter.setUsage(drug.usage);
        }
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if (category != null) {
            setter.setCategory(category);
            if (category != DrugCategory.Gaiyou) {
                if (!constraints.isDaysFixed() || getter.getDays().isEmpty()) {
                    setter.setDays("" + drug.days);
                }
            }
        }
        setter.setComment("");
    }

    public static void setExample(DrugFormSetter setter, PrescExampleFullDTO example, DrugFormGetter getter,
                                  DrugInputConstraints constraints) {
        setMaster(setter, example.master, getter, constraints);
        PrescExampleDTO prescExample = example.prescExample;
        if (!constraints.isAmountFixed() || getter.getAmount().isEmpty()) {
            setter.setAmount(prescExample.amount);
        }
        if (!constraints.isUsageFixed() || getter.getUsage().isEmpty()) {
            setter.setUsage(prescExample.usage);
        }
        DrugCategory category = DrugCategory.fromCode(prescExample.category);
        if (category != null) {
            setter.setCategory(category);
            if (category != DrugCategory.Gaiyou) {
                if (!constraints.isDaysFixed() || getter.getDays().isEmpty()) {
                    setter.setDays("" + prescExample.days);
                }
            }
        }
        setter.setComment(prescExample.comment);
    }

    public static void convertToDrug(DrugFormGetter getter, int drugId, int visitId, int prescribed,
                              BiConsumer<DrugDTO, List<String>> cb) {
        List<String> err = new ArrayList<>();
        DrugDTO drug = new DrugDTO();
        drug.drugId = drugId;
        drug.iyakuhincode = getter.getIyakuhincode();
        if( drug.iyakuhincode == 0 ){
            err.add("医薬品が指定されていません。");
        }
        try {
            drug.amount = Double.parseDouble(getter.getAmount());
        } catch (NumberFormatException ex) {
            err.add("用量の入力が適切でありません。");
        }
        drug.usage = getter.getUsage();
        if( drug.usage == null || drug.usage.isEmpty() ){
            err.add("用法が入力されていません。");
        }
        DrugCategory category = getter.getCategory();
        if (category != null) {
            drug.category = category.getCode();
        } else {
            err.add("Drug category is not specified.");
        }
        try {
            if (category == DrugCategory.Gaiyou) {
                drug.days = 1;
            } else {
                drug.days = Integer.parseInt(getter.getDays());
            }
        } catch (NumberFormatException ex) {
            err.add("日数・回数の入力が不適切です。");
        }
        drug.visitId = visitId;
        if( drug.visitId == 0 ) {
            err.add("Invalid visitId.");
        }
        drug.prescribed = prescribed;
        if( !(drug.prescribed == 0 || drug.prescribed == 1) ){
            err.add("Invalid prescribed value.");
        }
        cb.accept(drug, err);
    }

}