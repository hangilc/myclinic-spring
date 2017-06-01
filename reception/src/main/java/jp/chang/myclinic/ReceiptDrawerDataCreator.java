package jp.chang.myclinic;

import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.HokenUtil;

import java.text.NumberFormat;
import java.time.LocalDate;

/**
 * Created by hangil on 2017/05/21.
 */
public class ReceiptDrawerDataCreator {

    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public static ReceiptDrawerData create(int charge, PatientDTO patient, VisitDTO visit, MeisaiDTO meisai,
        ClinicInfoDTO clinicInfo){
        ReceiptDrawerDataCreator creator = new ReceiptDrawerDataCreator();
        ReceiptDrawerData data = new ReceiptDrawerData();
        data.setPatientName(patient.lastName + patient.firstName);
        data.setChargeByInt(charge);
        data.setVisitDate(DateTimeUtil.formatSqlDateTime(visit.visitedAt, DateTimeUtil.kanjiFormatter1));
        data.setIssueDate(DateTimeUtil.toKanji(LocalDate.now(), DateTimeUtil.kanjiFormatter1));
        data.setPatientId("" + patient.patientId);
        data.setHoken(creator.hokenRep(meisai.hoken));
        data.setFutanWari("" + meisai.futanWari);
        for(MeisaiSectionDTO section: meisai.sections){
            String ten = creator.format(section.sectionTotalTen);
            switch(MeisaiSection.valueOf(section.name)){
                case ShoshinSaisin: data.setShoshin(ten); break;
                case IgakuKanri: data.setKanri(ten); break;
                case Zaitaku: data.setZaitaku(ten); break;
                case Kensa: data.setKensa(ten); break;
                case Gazou: data.setGazou(ten); break;
                case Touyaku: data.setTouyaku(ten); break;
                case Chuusha: data.setChuusha(ten); break;
                case Shochi: data.setShochi(ten); break;
                case Sonota: data.setSonota(ten); break;
                default: System.out.println("unknown meisai section: " + section.name);
            }
        }
        data.setSouten(creator.format(meisai.totalTen));
        data.setClinicName(clinicInfo.name);
        data.setAddressLines(new String[]{
                clinicInfo.postalCode,
                clinicInfo.address,
                clinicInfo.tel,
                clinicInfo.fax,
                clinicInfo.homepage
        });
        return data;
    }

    private String hokenRep(HokenDTO hoken){
        if( hoken != null ){
            String rep = HokenUtil.hokenRep(hoken);
            if( !rep.isEmpty() ){
                return rep;
            }
        }
        return "";
    }

    private String format(int number){
        return numberFormat.format(number);
    }
}
