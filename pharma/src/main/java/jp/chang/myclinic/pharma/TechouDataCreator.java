package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.techou.TechouDrawerData;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TechouDataCreator {

    private String patientName;
    private String prescDate;
    private List<String> drugs = new ArrayList<>();
    private List<String> clinic = new ArrayList<>();

    public TechouDataCreator(PatientDTO patient, LocalDate at, List<DrugFullDTO> drugs, ClinicInfoDTO clinicInfo){
        patientName = patient.lastName + " " + patient.firstName;
        prescDate = DateTimeUtil.toKanji(at);
        int index = 1;
        for(DrugFullDTO drug: drugs){
            this.drugs.add(index + ") " + DrugUtil.drugRep(drug));
            index += 1;
        }
        clinic = createClinicData(clinicInfo);
    }

    public TechouDrawerData createData(){
        TechouDrawerData data = new TechouDrawerData();
        data.patientName = patientName;
        data.prescDate = prescDate;
        data.drugs = drugs;
        data.clinic = clinic;
        return data;
    }

    private List<String> createClinicData(ClinicInfoDTO clinicInfo){
        List<String> lines = new ArrayList<>();
        lines.add(clinicInfo.name);
        lines.add(clinicInfo.address);
        lines.add("TEL " + clinicInfo.tel);
        lines.add("院長 " + clinicInfo.doctorName);
        return lines;
    }

}
