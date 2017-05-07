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
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.rcpt.HoukatsuKensa;
import jp.chang.myclinic.rcpt.RcptVisit;
import jp.chang.myclinic.rcpt.Meisai;
import jp.chang.myclinic.rcpt.SectionItem;
import jp.chang.myclinic.util.DateTimeUtil;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	public int startVisit(@RequestBody PatientIdTimeDTO arg){
		int patientId = arg.patientId;
		LocalDateTime at = LocalDateTime.parse(arg.time, dateTimeFormatter);
		LocalDate atDate = at.toLocalDate();
		VisitDTO visitDTO = new VisitDTO();
		visitDTO.patientId = patientId;
		visitDTO.visitedAt = arg.time;
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
	public boolean getVisitMeisai(@RequestParam("visit-id") int visitId){
		RcptVisit rcptVisit = new RcptVisit();
		VisitDTO visit = dbGateway.getVisit(visitId);
		List<ShinryouFullDTO> shinryouList = dbGateway.listShinryouFull(visitId);
		LocalDate at =DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
		HoukatsuKensa.Revision revision = houkatsuKensa.findRevision(at);
		rcptVisit.add(shinryouList, revision);
		Meisai meisai = rcptVisit.getMeisai();
		for(MeisaiSection section: MeisaiSection.values()){
			System.out.println(section.getLabel());
			List<SectionItem> items = meisai.getItems(section);
			if( items != null ){
				items.forEach(item -> {
					System.out.println(item);
				});
			}
			System.out.println(meisai.sectionTotal(section));
		}
		return true;
	}

}