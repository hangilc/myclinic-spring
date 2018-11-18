package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import jp.chang.myclinic.serverpostgresql.rcpt.HoukatsuKensa;
import jp.chang.myclinic.serverpostgresql.rcpt.Meisai;
import jp.chang.myclinic.serverpostgresql.rcpt.RcptVisit;
import jp.chang.myclinic.serverpostgresql.rcpt.SectionItem;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.util.RcptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
@Transactional
public class VisitController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private HoukatsuKensa houkatsuKensa;

    @RequestMapping(value = "/get-visit", method = RequestMethod.GET)
    public VisitDTO getVisit(@RequestParam("visit-id") int visitId) {
        return dbGateway.getVisit(visitId);
    }

    @RequestMapping(value = "/list-visit-ids", method = RequestMethod.GET)
    public List<Integer> listVisitIds() {
        return dbGateway.listVisitIds();
    }

    @RequestMapping(value="/get-hoken", method=RequestMethod.GET)
    public HokenDTO getHoken(@RequestParam("visit-id") int visitId){
        VisitDTO visit = dbGateway.getVisit(visitId);
        return dbGateway.getHokenForVisit(visit);
    }

    @RequestMapping(value = "list-visit-id-for-patient", method = RequestMethod.GET)
    public List<Integer> listVisitIdsForPatient(@RequestParam("patient-id") int patientId) {
        return dbGateway.listVisitIdsForPatient(patientId);
    }

    @RequestMapping(value = "list-visit-id-visited-at-for-patient", method = RequestMethod.GET)
    public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtForPatient(@RequestParam("patient-id") int patientId) {
        return dbGateway.listVisitIdVisitedAtForPatient(patientId);
    }

    @RequestMapping(value = "/list-visit-with-patient", method = RequestMethod.GET)
    public List<VisitPatientDTO> listVisitWithPatient(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "items-per-page", defaultValue = "30") int itemsPerPage) {
        return dbGateway.listVisitWithPatient(page, itemsPerPage);
    }

    @RequestMapping(value = "/update-hoken", method = RequestMethod.POST)
    public boolean updateHoken(@RequestBody VisitDTO visitDTO) {
        VisitDTO origVisit = dbGateway.getVisit(visitDTO.visitId);
        origVisit.shahokokuhoId = visitDTO.shahokokuhoId;
        origVisit.koukikoureiId = visitDTO.koukikoureiId;
        origVisit.roujinId = visitDTO.roujinId;
        origVisit.kouhi1Id = visitDTO.kouhi1Id;
        origVisit.kouhi2Id = visitDTO.kouhi2Id;
        origVisit.kouhi3Id = visitDTO.kouhi3Id;
        dbGateway.updateVisit(origVisit);
        return true;
    }

    @RequestMapping(value = "/list-todays-visits", method = RequestMethod.GET)
    public List<VisitPatientDTO> listTodaysVisits() {
        return dbGateway.listTodaysVisits();
    }

    @RequestMapping(value = "/start-visit", method = RequestMethod.POST)
    public int startVisit(@RequestParam("patient-id") int patientId) {
        LocalDateTime at = LocalDateTime.now();
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = dbGateway.findAvailableShahokokuho(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = dbGateway.findAvailableKoukikourei(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = dbGateway.findAvailableRoujin(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.roujinId = 0;
            } else {
                visitDTO.roujinId = list.get(0).roujinId;
            }
        }
        {
            visitDTO.kouhi1Id = 0;
            visitDTO.kouhi2Id = 0;
            visitDTO.kouhi3Id = 0;
            List<KouhiDTO> list = dbGateway.findAvailableKouhi(patientId, atDate);
            int n = list.size();
            if (n > 0) {
                visitDTO.kouhi1Id = list.get(0).kouhiId;
                if (n > 1) {
                    visitDTO.kouhi2Id = list.get(1).kouhiId;
                    if (n > 2) {
                        visitDTO.kouhi3Id = list.get(2).kouhiId;
                    }
                }
            }
        }
        int visitId = dbGateway.enterVisit(visitDTO);
        visitDTO.visitId = visitId;
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        dbGateway.enterWqueue(wqueueDTO);
        return visitId;
    }

    @RequestMapping(value = "/get-visit-full", method = RequestMethod.GET)
    public VisitFullDTO getVisitFull(@RequestParam("visit-id") int visitId) {
        return dbGateway.getVisitFull(visitId);
    }

    @RequestMapping(value = "/list-visit-full", method = RequestMethod.GET)
    public VisitFullPageDTO listVisitFull(@RequestParam("patient-id") int patientId, @RequestParam("page") int page) {
        return dbGateway.listVisitFull(patientId, page);
    }

    @RequestMapping(value = "/list-visit-full2", method = RequestMethod.GET)
    public VisitFull2PageDTO listVisitFull2(@RequestParam("patient-id") int patientId, @RequestParam("page") int page) {
        return dbGateway.listVisitFull2(patientId, page);
    }

    @RequestMapping(value = "/page-visit-full2-with-patient-at", method = RequestMethod.GET)
    public VisitFull2PatientPageDTO pageVisitFull2PatientAt(@RequestParam("at") String at,
                                                            @RequestParam("page") int page) {
        LocalDate date = LocalDate.parse(at);
        return dbGateway.pageVisitsWithPatientAt(date, page);
    }

    @RequestMapping(value = "/get-visit-meisai", method = RequestMethod.GET)
    public MeisaiDTO getVisitMeisai(@RequestParam("visit-id") int visitId) {
        RcptVisit rcptVisit = new RcptVisit();
        VisitDTO visit = dbGateway.getVisit(visitId);
        List<ShinryouFullDTO> shinryouList = dbGateway.listShinryouFull(visitId);
        List<DrugFullDTO> drugs = dbGateway.listDrugFull(visitId);
        List<ConductFullDTO> conducts = dbGateway.listConductFull(visitId);
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        HoukatsuKensa.Revision revision = houkatsuKensa.findRevision(at);
        rcptVisit.addShinryouList(shinryouList, revision);
        rcptVisit.addDrugs(drugs);
        rcptVisit.addConducts(conducts);
        Meisai meisai = rcptVisit.getMeisai();
        MeisaiDTO meisaiDTO = new MeisaiDTO();
        meisaiDTO.sections = new ArrayList<>();
        for (MeisaiSection section : MeisaiSection.values()) {
            List<SectionItem> items = meisai.getItems(section);
            if (items != null) {
                MeisaiSectionDTO meisaiSectionDTO = new MeisaiSectionDTO();
                meisaiSectionDTO.name = section.toString();
                meisaiSectionDTO.label = section.getLabel();
                meisaiSectionDTO.items = items.stream()
                        .map(this::toSectionItemDTO)
                        .collect(Collectors.toList());
                meisaiSectionDTO.sectionTotalTen = SectionItem.sum(items);
                meisaiDTO.sections.add(meisaiSectionDTO);
            }
        }
        meisaiDTO.totalTen = meisai.totalTen();
        PatientDTO patientDTO = dbGateway.getPatient(visit.patientId);
        if (patientDTO.birthday != null) {
            HokenDTO hokenDTO = dbGateway.getHokenForVisit(visit);
            meisaiDTO.hoken = hokenDTO;
            LocalDate birthdayDate = DateTimeUtil.parseSqlDate(patientDTO.birthday);
            int rcptAge = HokenUtil.calcRcptAge(birthdayDate.getYear(), birthdayDate.getMonth().getValue(),
                    birthdayDate.getDayOfMonth(), at.getYear(), at.getMonth().getValue());
            meisaiDTO.futanWari = HokenUtil.calcFutanWari(hokenDTO, rcptAge);
        } else {
            meisaiDTO.futanWari = 10;
        }
        meisaiDTO.charge = RcptUtil.calcCharge(meisaiDTO.totalTen, meisaiDTO.futanWari);
        return meisaiDTO;
    }

    @RequestMapping(value = "/delete-visit-from-reception", method = RequestMethod.POST)
    public boolean deleteVisitFromReception(@RequestParam("visit-id") int visitId) {
        dbGateway.deleteVisitFromReception(visitId);
        return true;
    }

    @RequestMapping(value = "/delete-visit", method = RequestMethod.POST)
    public boolean deleteVisit(@RequestParam("visit-id") int visitId) {
        dbGateway.deleteVisitSafely(visitId);
        return true;
    }

    @RequestMapping(value = "/list-visit-text-drug-for-patient", method = RequestMethod.GET)
    public VisitTextDrugPageDTO listVisitTextDrugForPatient(@RequestParam("patient-id") int patientId,
                                                            @RequestParam("page") int page) {
        return dbGateway.listVisitTextDrugForPatient(patientId, page);
    }

    @RequestMapping(value = "/list-visit-charge-patient-at", method = RequestMethod.GET)
    public List<VisitChargePatientDTO> listVisitChargePatientAt(
            @RequestParam("at") String at) {
        LocalDate date = LocalDate.parse(at);
        return dbGateway.listVisitChargePatientAt(date);
    }

    private SectionItemDTO toSectionItemDTO(SectionItem sectionItem) {
        SectionItemDTO sectionItemDTO = new SectionItemDTO();
        sectionItemDTO.label = sectionItem.getLabel();
        sectionItemDTO.tanka = sectionItem.getTanka();
        sectionItemDTO.count = sectionItem.getCount();
        return sectionItemDTO;
    }

    @RequestMapping(value = "/page-visit-drug", method = RequestMethod.GET)
    public VisitDrugPageDTO pageVisitDrug(@RequestParam("patient-id") int patientId,
                                          @RequestParam("page") int page) {
        return dbGateway.pageVisitIdHavingDrug(patientId, page);
    }

    @RequestMapping(value = "list-visiting-patient-id-having-hoken", method = RequestMethod.GET)
    public List<Integer> listVisitingPatientId(@RequestParam("year") int year,
                                               @RequestParam("month") int month) {
        return dbGateway.listVisitingPatientIdHavingHoken(year, month);
    }

    @RequestMapping(value = "list-visit-by-patient-having-hoken", method = RequestMethod.GET)
    public List<VisitFull2DTO> listVisitByPatientHavingHoken(
            @RequestParam("patient-id") int patientId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return dbGateway.listVisitByPatientHavingHoken(patientId, year, month);
    }

}