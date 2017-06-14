package jp.chang.myclinic.pharma;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.DrawerColor;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrugBagDataCreator {

    private DrugDTO drug;
    private IyakuhinMasterDTO master;

    public DrugBagDataCreator(DrugFullDTO drugFull){
        drug = drugFull.drug;
        master = drugFull.master;
    }

    public DrugBagDrawerData createData(){
        DrugBagDrawerData data = new DrugBagDrawerData();
        data.color = getColor();
        data.title = getTitle();
        return data;
    }

    private DrawerColor getColor(){
        switch(DrugCategory.fromCode(drug.category)){
            case Naifuku: return new DrawerColor(0, 0, 255);
            case Tonpuku: return new DrawerColor(0, 255, 0);
            case Gaiyou: return new DrawerColor(255, 0, 0);
            default: return new DrawerColor(0, 0, 0);
        }
    }

    private String getTitle(){
        switch(DrugCategory.fromCode(drug.category)){
            case Naifuku: return "内服薬";
            case Tonpuku: return "頓服薬";
            case Gaiyou: return "外用薬";
            default: return "おくすり";
        }
    }
}
