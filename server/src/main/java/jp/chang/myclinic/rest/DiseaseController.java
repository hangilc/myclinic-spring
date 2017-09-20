package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
class DiseaseController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/list-current-disease-full", method= RequestMethod.GET)
    List<DiseaseFullDTO> listCurrentDiseaseFull(@RequestParam("patient-id") int patientId,
                                                @RequestParam("at") String at){
        LocalDate atDate = LocalDate.parse(at);
        return dbGateway.listCurrentDiseaseFull(patientId, atDate);
    }

}
