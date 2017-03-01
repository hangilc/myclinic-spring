package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.Disease;
import jp.chang.myclinic.model.DiseaseAdj;
import jp.chang.myclinic.model.DiseaseAdjRepository;
import jp.chang.myclinic.model.DiseaseRepository;
import jp.chang.myclinic.web.service.json.JsonFullDisease;
import jp.chang.myclinic.web.service.json.JsonFullDiseaseAdj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)
public class DiseaseController {
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DiseaseAdjRepository diseaseAdjRepository;

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=list_current_full_diseases", "patient_id"})
    public List<JsonFullDisease> listCurrentFullDiseases(@RequestParam("patient_id") int patientId){
        List<Disease> diseases = diseaseRepository.findCurrentByPatientIdWithMaster(patientId);
        return diseases.stream()
                .map(disease -> {
                    JsonFullDisease dst = JsonFullDisease.fromDisease(disease);
                    List<DiseaseAdj> adjList = diseaseAdjRepository.findByDiseaseIdWithMaster(disease.getDiseaseId());
                    List<JsonFullDiseaseAdj> jsonAdjList = adjList.stream()
                            .map(JsonFullDiseaseAdj::fromDiseaseAdj)
                            .collect(Collectors.toList());
                    dst.setAdjList(jsonAdjList);
                    return dst;
                })
                .collect(Collectors.toList());
    }

}
