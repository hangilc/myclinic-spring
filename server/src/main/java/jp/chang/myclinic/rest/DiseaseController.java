package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
class DiseaseController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/list-current-disease-full", method= RequestMethod.GET)
    public List<DiseaseFullDTO> listCurrentDiseaseFull(@RequestParam("patient-id") int patientId,
                                                @RequestParam("at") String at){
        LocalDate atDate = LocalDate.parse(at);
        return dbGateway.listCurrentDiseaseFull(patientId, atDate);
    }

    @RequestMapping(value="/get-disease-full", method=RequestMethod.GET)
    public DiseaseFullDTO getDiseaseFull(@RequestParam("disease-id") int diseaseId){
        return dbGateway.getDiseaseFull(diseaseId);
    }

    @RequestMapping(value="/enter-disease", method=RequestMethod.POST)
    public int enterDisease(@RequestBody DiseaseNewDTO diseaseNew){
        return dbGateway.enterDisease(diseaseNew.disease, diseaseNew.adjList);
    }

}
