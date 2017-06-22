package jp.chang.myclinic.rest;

import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.db.Visit;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.rcpt.*;
import jp.chang.myclinic.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
@Transactional
public class VisitController {

	@Autowired
	private DbGateway dbGateway;
	@Autowired
	private HoukatsuKensa houkatsuKensa;

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

	@RequestMapping(value="/start-visit", method=RequestMethod.POST)
	public int startVisit(@RequestParam("patient-id") int patientId){
		LocalDateTime at = LocalDateTime.now();
		LocalDate atDate = at.toLocalDate();
		VisitDTO visitDTO = new VisitDTO();
		visitDTO.patientId = patientId;
		visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
		{
			List<ShahokokuhoDTO> list = dbGateway.findAvailableShahokokuho(patientId, atDate);
			if( list.size() == 0 ){
				visitDTO.shahokokuhoId = 0;
			} else {
				visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
			}
		}
		{
			List<KoukikoureiDTO> list = dbGateway.findAvailableKoukikourei(patientId, atDate);
			if( list.size() == 0 ){
				visitDTO.koukikoureiId = 0;
			} else {
				visitDTO.koukikoureiId = list.get(0).koukikoureiId;
			}
		}
		{
			List<RoujinDTO> list = dbGateway.findAvailableRoujin(patientId, atDate);
			if( list.size() == 0 ){
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
			if( n > 0 ){
				visitDTO.kouhi1Id = list.get(0).kouhiId;
				if( n > 1 ){
					visitDTO.kouhi2Id = list.get(1).kouhiId;
					if( n > 2 ){
						visitDTO.kouhi3Id = list.get(2).kouhiId;
					}
				}
			}
		}
		int visitId = dbGateway.enterVisit(visitDTO);
		WqueueDTO wqueueDTO = new WqueueDTO();
		wqueueDTO.visitId = visitId;
		wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
		dbGateway.enterWqueue(wqueueDTO);
		return visitId;
	}

	@RequestMapping(value="/get-visit-full", method=RequestMethod.GET)
	public VisitFullDTO getVisitFull(@RequestParam("visit-id") int visitId){
		return dbGateway.getVisitFull(visitId);
	}

	@RequestMapping(value="/get-visit-meisai", method=RequestMethod.GET)
	public MeisaiDTO getVisitMeisai(@RequestParam("visit-id") int visitId){
		RcptVisit rcptVisit = new RcptVisit();
		VisitDTO visit = dbGateway.getVisit(visitId);
		List<ShinryouFullDTO> shinryouList = dbGateway.listShinryouFull(visitId);
		List<DrugFullDTO> drugs = dbGateway.listDrugFull(visitId);
		List<ConductFullDTO> conducts = dbGateway.listConductFull(visitId);
		LocalDate at =DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
		HoukatsuKensa.Revision revision = houkatsuKensa.findRevision(at);
		rcptVisit.addShinryouList(shinryouList, revision);
		rcptVisit.addDrugs(drugs);
		rcptVisit.addConducts(conducts);
		Meisai meisai = rcptVisit.getMeisai();
		MeisaiDTO meisaiDTO = new MeisaiDTO();
		meisaiDTO.sections = new ArrayList<>();
		for(MeisaiSection section: MeisaiSection.values()){
			List<SectionItem> items = meisai.getItems(section);
			if( items != null ){
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
		if( patientDTO.birthday != null ){
			HokenDTO hokenDTO = dbGateway.getHokenForVisit(visit);
			meisaiDTO.hoken = hokenDTO;
			LocalDateTime atDateTime = DateTimeUtil.parseSqlDateTime(visit.visitedAt);
			LocalDate birthdayDate = DateTimeUtil.parseSqlDate(patientDTO.birthday);
			int rcptAge = RcptUtil.calcRcptAge(birthdayDate.getYear(), birthdayDate.getMonth().getValue(),
					birthdayDate.getDayOfMonth(), at.getYear(), at.getMonth().getValue());
			meisaiDTO.futanWari = FutanWari.calcFutanWari(hokenDTO, rcptAge);
		} else {
			meisaiDTO.futanWari = 10;
		}
		meisaiDTO.charge = RcptUtil.calcCharge(meisaiDTO.totalTen, meisaiDTO.futanWari);
		return meisaiDTO;
	}

	@RequestMapping(value="/list-visit-ids", method=RequestMethod.GET)
	public List<Integer> listVisitIds(){
		return dbGateway.listVisitIds();
	}

	@RequestMapping(value="list-visit-ids-for-patient", method=RequestMethod.GET)
	public List<Integer> listVisitIdsForPatient(@RequestParam("patient-id") int patientId){
		return dbGateway.listVisitIdsForPatient(patientId);
	}

	@RequestMapping(value="/list-visit-with-patient", method=RequestMethod.GET)
	public List<VisitPatientDTO> listVisitWithPatient(
		@RequestParam(value="page", defaultValue="0") int page,
		@RequestParam(value="items-per-page", defaultValue="30") int itemsPerPage){
		return dbGateway.listVisitWithPatient(page, itemsPerPage);
	}

	@RequestMapping(value="/get-visit", method=RequestMethod.GET)
	public VisitDTO getVisit(@RequestParam("visit-id") int visitId){
		return dbGateway.getVisit(visitId);
	}

	@RequestMapping(value="/delete-visit-from-reception", method=RequestMethod.POST)
	public boolean deleteVisitFromReception(@RequestParam("visit-id") int visitId){
		dbGateway.deleteVisitFromReception(visitId);
		return true;
	}

	@RequestMapping(value="/list-visit-text-drug", method=RequestMethod.GET)
	public List<VisitTextDrugDTO> listVisitTextDrug(@RequestParam("visit-id[]") Set<Integer> visitIds){
		return dbGateway.listVisitTextDrug(visitIds);
	}

	private SectionItemDTO toSectionItemDTO(SectionItem sectionItem) {
		SectionItemDTO sectionItemDTO = new SectionItemDTO();
		sectionItemDTO.label = sectionItem.getLabel();
		sectionItemDTO.tanka = sectionItem.getTanka();
		sectionItemDTO.count = sectionItem.getCount();
		return sectionItemDTO;
	}

}