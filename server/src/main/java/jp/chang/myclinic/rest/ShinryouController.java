package jp.chang.myclinic.db;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.db.myclinic.ShinryouMaster;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@RequestMapping(value="/batch-enter-shinryou-by-name", method=RequestMethod.POST)
	public List<Integer> batchEnterShinryou(@RequestParam("visit-id") int visitId,
											@RequestParam("name") List<String> names){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));
	}

	private Optional<Integer> mapShinryouName(String shinryouName){
		return masterMap.getShinryoucodeByName(shinryouName);
	}

	private Integer resolveShinryoucode(int shinryoucode, LocalDate at){
		return masterMap.resolveShinryouCode(shinryoucode, at);
	}

	private ShinryouMaster getMaster(int shinryoucode, LocalDate at){

	}

}