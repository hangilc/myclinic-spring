package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.ClinicInfo;

import java.util.List;
import java.util.Optional;

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

