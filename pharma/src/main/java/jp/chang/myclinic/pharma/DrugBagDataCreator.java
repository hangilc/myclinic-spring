package jp.chang.myclinic.pharma;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.DrawerColor;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PatientDTO;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrugBagDataCreator {

    private DrugDTO drug;
    private IyakuhinMasterDTO master;
    private PatientDTO patient;

    public DrugBagDataCreator(DrugFullDTO drugFull, PatientDTO patient){
        this.drug = drugFull.drug;
        this.master = drugFull.master;
        this.patient = patient;
    }

    public DrugBagDrawerData createData(){
        DrugBagDrawerData data = new DrugBagDrawerData();
        data.color = getColor();
        data.title = getTitle();
        data.patientName = patient.lastName + " " + patient.firstName;
        data.patientNameYomi = patient.lastNameYomi + " " + patient.firstNameYomi;
        return data;
    }

    private DrawerColor getColor(){
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if( category == null ){
            return new DrawerColor(0, 0, 0);
        }
        switch(category){
            case Naifuku: return new DrawerColor(0, 0, 255);
            case Tonpuku: return new DrawerColor(0, 255, 0);
            case Gaiyou: return new DrawerColor(255, 0, 0);
            default: return new DrawerColor(0, 0, 0);
        }
    }

    private String getTitle(){
        DrugCategory category = DrugCategory.fromCode(drug.category);
        if( category == null ){
            return "";
        }
        switch(category){
            case Naifuku: return "内服薬";
            case Tonpuku: return "頓服薬";
            case Gaiyou: return "外用薬";
            default: return "おくすり";
        }
    }
}
