package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseModifyEndReasonDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.server.DiseaseExample;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
@Transactional
class DiseaseController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private DiseaseExample diseaseExample;

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

    @RequestMapping(value="/list-disease-example", method=RequestMethod.GET)
    public List<DiseaseExampleDTO> listDiseaseExample(){
        return diseaseExample.getDiseaseExample().stream()
                .map(this::toDiseaseExampleDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/batch-update-disease-end-reason", method=RequestMethod.POST)
    public boolean batchUpdateEndReason(@RequestBody List<DiseaseModifyEndReasonDTO> args){
        args.forEach(arg -> {
            LocalDate endDate = LocalDate.parse(arg.endDate);
            dbGateway.modifyDiseaseEndReason(arg.diseaseId, endDate, arg.endReason);
        });
        return true;
    }

    private DiseaseExampleDTO toDiseaseExampleDTO(DiseaseExample.Entry entry){
        DiseaseExampleDTO dto = new DiseaseExampleDTO();
        String label = entry.getLabel();
        List<String> pre = entry.getPre();
        List<String> post = entry.getPost();
        if( label == null ){
            label = entry.getByoumei();
            if( label == null ){
                label = "";
            }
            if( pre != null ){
                label = String.join("", pre) + label;
            }
            if( post != null ){
                label = label + String.join("", post);
            }
        }
        List<String> adjList = new ArrayList<>();
        if( pre != null ){
            adjList.addAll(pre);
        }
        if( post != null ){
            adjList.addAll(post);
        }
        dto.label = label;
        dto.byoumei = entry.getByoumei();
        dto.adjList = adjList;
        return dto;
    }

}
