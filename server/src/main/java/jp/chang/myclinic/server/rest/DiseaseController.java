package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.*;
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
    public List<DiseaseFullDTO> listCurrentDiseaseFull(@RequestParam("patient-id") int patientId){
        return dbGateway.listCurrentDiseaseFull(patientId);
    }

    @RequestMapping(value="/list-disease-full", method=RequestMethod.GET)
    public List<DiseaseFullDTO> listDiseaseFull(@RequestParam("patient-id") int patientId){
        return dbGateway.listDiseaseFull(patientId);
    }

    @RequestMapping(value="/count-page-of-disease-by-patient", method=RequestMethod.GET)
    public int countPageOfDiseaseByPatient(@RequestParam("patient-id") int patientId,
                                           @RequestParam("items-per-page") int itemsPerPage){
        long count = dbGateway.countDiseaseByPatient(patientId);
        return (int)((count + itemsPerPage - 1) / itemsPerPage);
    }

    @RequestMapping(value="/page-disease-full", method=RequestMethod.GET)
    public List<DiseaseFullDTO> pageDiseaseFull(@RequestParam("patient-id") int patientId,
                                                @RequestParam("page") int page,
                                                @RequestParam("items-per-page") int itemsPerPage){
        return dbGateway.pageDiseaseFull(patientId, page, itemsPerPage);
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

    @RequestMapping(value="/modify-disease", method=RequestMethod.POST)
    public boolean modifyDisease(@RequestBody DiseaseModifyDTO diseaseModifyDTO){
        dbGateway.modifyDisease(diseaseModifyDTO);
        return true;
    }

    @RequestMapping(value="/delete-disease", method=RequestMethod.POST)
    public boolean deleteDisease(@RequestParam("disease-id") int diseaseId){
        dbGateway.deleteDisease(diseaseId);
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

    @RequestMapping(value="list-disease-by-patient-at", method=RequestMethod.GET)
    List<DiseaseFullDTO> listDiseaseByPatientAt(@RequestParam("patient-id") int patientId,
                                                @RequestParam("year") int year,
                                                @RequestParam("month") int month){
        return dbGateway.listDiseaseByPatientAt(patientId, year, month);
    }

}
