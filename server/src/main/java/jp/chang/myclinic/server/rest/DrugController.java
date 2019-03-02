package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
@Transactional
public class DrugController {

    @Autowired
    private DbGatewayInterface dbGateway;

    @RequestMapping(value = "/get-drug", method=RequestMethod.GET)
    public DrugDTO getDrug(@RequestParam("drug-id") int drugId){
        return dbGateway.getDrug(drugId);
    }

    @RequestMapping(value = "/get-drug-full", method = RequestMethod.GET)
    public DrugFullDTO getDrugFull(@RequestParam("drug-id") int drugId) {
        return dbGateway.getDrugFull(drugId);
    }

    @RequestMapping(value = "/list-drug-full", method = RequestMethod.GET)
    public List<DrugFullDTO> listDrugFull(@RequestParam("visit-id") int visitId) {
        return dbGateway.listDrugFull(visitId);
    }

    @RequestMapping(value = "/list-drug-full-by-drug-ids", method = RequestMethod.GET)
    public List<DrugFullDTO> listDrugFullByDrugIds(@RequestParam(value = "drug-id", required = false) List<Integer> drugIds) {
        if (drugIds == null) {
            drugIds = Collections.emptyList();
        }
        return drugIds.stream().map(dbGateway::getDrugFull).collect(Collectors.toList());
    }

    @RequestMapping(value = "/list-iyakuhin-for-patient", method = RequestMethod.GET)
    public List<IyakuhincodeNameDTO> listIyakuhinForPatient(@RequestParam("patient-id") int patientId) {
        return dbGateway.listIyakuhinForPatient(patientId);
    }

    @Deprecated
    @RequestMapping(value = "/list-visit-id-visited-at-by-patient-and-iyakuhincode", method = RequestMethod.GET)
    public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByPatientAndIyakuhincode(@RequestParam("patient-id") int patientId,
                                                                                  @RequestParam("iyakuhincode") int iyakuhincode) {
        return dbGateway.listVisitIdVisitedAtByIyakuhincodeAndPatientId(patientId, iyakuhincode);
    }

    @RequestMapping(value = "/list-visit-text-drug-by-patient-and-iyakuhincode", method = RequestMethod.GET)
    public VisitTextDrugPageDTO listVisitTextDrgByPatientAndIyakuhincode(@RequestParam("patient-id") int patientId,
                                                                         @RequestParam("iyakuhincode") int iyakuhincode,
                                                                         @RequestParam("page") int page) {
        return dbGateway.listVisitTextDrugByPatientAndIyakuhincode(patientId, iyakuhincode, page);
    }

    @RequestMapping(value = "/mark-drug-as-prescribed-for-visit", method = RequestMethod.POST)
    public boolean markDrugAsPrescribedForVisit(@RequestParam("visit-id") int visitId) {
        dbGateway.markDrugsAsPrescribedForVisit(visitId);
        return true;
    }

    @RequestMapping(value = "/presc-done", method = RequestMethod.POST)
    public boolean prescDone(@RequestParam("visit-id") int visitId) {
        dbGateway.markDrugsAsPrescribedForVisit(visitId);
        dbGateway.findPharmaQueue(visitId)
                .ifPresent(pharmaQueueDTO1 -> dbGateway.deletePharmaQueue(pharmaQueueDTO1));
        dbGateway.findWqueue(visitId)
                .ifPresent(wqueueDTO1 -> dbGateway.deleteWqueue(wqueueDTO1));
        return true;
    }

    @RequestMapping(value = "/search-prev-drug", method = RequestMethod.GET)
    public List<DrugFullDTO> searchPrevDrug(@RequestParam("patient-id") int patientId,
                                            @RequestParam(value = "text", defaultValue = "") String text) {
        List<DrugFullDTO> result;
        if (text.isEmpty()) {
            result = dbGateway.searchPrevDrug(patientId);
        } else {
            result = dbGateway.searchPrevDrug(patientId, text);
        }
        result.sort((o1, o2) -> {
            int i1 = o1.drug.visitId;
            int i2 = o2.drug.visitId;
            if (i1 < i2) {
                return 1;
            } else if (i1 > i2) {
                return -1;
            } else {
                int k1 = o1.drug.drugId;
                int k2 = o2.drug.drugId;
                return k1 - k2;
            }
        });
        return result;
    }

    @RequestMapping(value = "/enter-drug", method = RequestMethod.POST)
    public int enterDrug(@RequestBody DrugDTO drug) {
        return dbGateway.enterDrug(drug);
    }

    @RequestMapping(value = "/batch-enter-drugs", method = RequestMethod.POST)
    public List<Integer> batchEnterDrugs(@RequestBody List<DrugDTO> drugs) {
        List<Integer> drugIds = new ArrayList<>();
        drugs.forEach(drug -> {
            drugIds.add(dbGateway.enterDrug(drug));
        });
        return drugIds;
    }

    @RequestMapping(value = "/batch-update-drug-days", method = RequestMethod.POST)
    public boolean batchUpdateDrugDays(@RequestParam(value = "drug-id", required = false) List<Integer> drugIds, @RequestParam("days") int days) {
        if (drugIds != null && drugIds.size() > 0) {
            dbGateway.batchUpdateDrugDays(drugIds, days);
        }
        return true;
    }

    @RequestMapping(value = "/update-drug", method = RequestMethod.POST)
    public boolean updateDrug(@RequestBody DrugDTO drug) {
        dbGateway.updateDrug(drug);
        return true;
    }

    @RequestMapping(value = "/batch-delete-drugs", method = RequestMethod.POST)
    public boolean batchDeleteDrugs(@RequestParam(value = "drug-id", required = false) List<Integer> drugIds) {
        if (drugIds != null) {
            drugIds.forEach(dbGateway::deleteDrug);
        }
        return true;
    }

    @RequestMapping(value = "/delete-drug", method = RequestMethod.POST)
    public boolean deleteDrug(@RequestParam("drug-id") int drugId) {
        dbGateway.deleteDrug(drugId);
        return true;
    }
}