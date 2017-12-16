package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.ClinicInfo;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
//@Transactional
public class InfoController {

    @Autowired
    private ClinicInfo clinicInfo;

    @RequestMapping(value="/get-clinic-info", method=RequestMethod.GET)
    public ClinicInfoDTO getClinicInfo(){
        ClinicInfoDTO clinicInfoDTO = new ClinicInfoDTO();
        clinicInfoDTO.name = clinicInfo.getName();
        clinicInfoDTO.postalCode = clinicInfo.getPostalCode();
        clinicInfoDTO.address = clinicInfo.getAddress();
        clinicInfoDTO.tel = clinicInfo.getTel();
        clinicInfoDTO.fax = clinicInfo.getFax();
        clinicInfoDTO.todoufukencode = clinicInfo.getTodoufukencode();
        clinicInfoDTO.tensuuhyoucode = clinicInfo.getTensuuhyoucode();
        clinicInfoDTO.kikancode = clinicInfo.getKikancode();
        clinicInfoDTO.homepage = clinicInfo.getHomepage();
        clinicInfoDTO.doctorName = clinicInfo.getDoctorName();
        return clinicInfoDTO;
    }
}

