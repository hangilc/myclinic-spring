package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class ShinryouController {

	@Autowired
	private DbGateway dbGateway;
	@Autowired
	private MasterMap masterMap;

	@RequestMapping(value="/get-shinryou-full", method=RequestMethod.GET)
	public ShinryouFullDTO getShinryouFull(@RequestParam("shinryou-id") int shinryouId){
		return dbGateway.getShinryouFull(shinryouId);
	}

	@RequestMapping(value="/list-shinryou-full-by-ids", method=RequestMethod.GET)
	public List<ShinryouFullDTO> listShinryouFullByIds(@RequestParam("shinryou-id") List<Integer> shinryouIds){
		return dbGateway.listShinryouFullByIds(shinryouIds);
	}

	@RequestMapping(value="/batch-enter-shinryou-by-name", method=RequestMethod.POST)
	public List<Integer> batchEnterShinryou(@RequestParam("visit-id") int visitId,
											@RequestParam("name") List<String> names){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));
		List<Integer> shinryouIds = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>();
		for(String name: names){
			Optional<ShinryouMasterDTO> optMaster = resolveShinryouMaster(name, at);
			if( optMaster.isPresent() ){
				ShinryouDTO shinryouDTO = new ShinryouDTO();
				shinryouDTO.visitId = visitId;
				shinryouDTO.shinryoucode = optMaster.get().shinryoucode;
				shinryouDTO = dbGateway.enterShinryou(shinryouDTO);
				shinryouIds.add(shinryouDTO.shinryouId);
			} else {
				errorMessages.add(String.format("%sはその期日に使用できません。", name));
			}
		}
		if( errorMessages.size() > 0 ){
			throw new RuntimeException(String.join("", errorMessages));
		}
		return shinryouIds;
	}

	private Optional<ShinryouMasterDTO> resolveShinryouMaster(String name, LocalDate at){
		Optional<Integer> optShinryoucode = masterMap.getShinryoucodeByName(name);
		if( optShinryoucode.isPresent() ){
			int shinryoucode = masterMap.resolveShinryouCode(optShinryoucode.get(), at);
			return dbGateway.findShinryouMasterByShinryoucode(shinryoucode, at);
		} else {
			return dbGateway.findShinryouMasterByName(name, at);
		}
	}

}