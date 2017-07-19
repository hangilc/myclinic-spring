package jp.chang.myclinic.rest;

import jp.chang.myclinic.ClinicInfo;
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
        clinicInfoDTO.homepage = clinicInfo.getHomepage();
        clinicInfoDTO.doctorName = clinicInfo.getDoctorName();
        return clinicInfoDTO;
    }
}

