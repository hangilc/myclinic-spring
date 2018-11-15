package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/json")
@Transactional
public class WqueueController {

	@Autowired
	private DbGateway dbGateway;

	private static Set<WqueueWaitState> forExamStates = new HashSet<>();
	static {
		forExamStates.add(WqueueWaitState.WaitExam);
		forExamStates.add(WqueueWaitState.InExam);
		forExamStates.add(WqueueWaitState.WaitReExam);
	}

	private static Set<WqueueWaitState> inWaitingExamStates = new HashSet<>();
	static {
		inWaitingExamStates.add(WqueueWaitState.WaitExam);
		inWaitingExamStates.add(WqueueWaitState.WaitReExam);
	}

	@RequestMapping(value="/list-wqueue-full", method=RequestMethod.GET)
	public List<WqueueFullDTO> listWqueueFull(){
		return dbGateway.listWqueueFull();
	}

	@RequestMapping(value="/try-delete-wqueue", method=RequestMethod.POST)
	public boolean tryDeleteWqueue(@RequestParam("visit-id") int visitId){
		Optional<WqueueDTO> optWqueueDTO = dbGateway.findWqueue(visitId);
		optWqueueDTO.ifPresent(wqueue -> dbGateway.deleteWqueue(wqueue));
		return true;
	}

	@RequestMapping(value="/list-wqueue-full-for-exam", method=RequestMethod.GET)
	public List<WqueueFullDTO> listWqueueFullForExam(){
		return dbGateway.listWqueueFullByStates(forExamStates);
	}

	@RequestMapping(value="/list-wqueue-full-in-waiting-exam", method=RequestMethod.GET)
	public List<WqueueFullDTO> listWqueueFullInWaitingExam(){
		return dbGateway.listWqueueFullByStates(inWaitingExamStates);
	}

	@RequestMapping(value="/get-wqueue-full", method=RequestMethod.GET)
	public WqueueFullDTO getWqueueFull(@RequestParam("visit-id") int visitId){
		return dbGateway.getWqueueFull(visitId);
	}

	@RequestMapping(value="/list-wqueue-for-exam", method=RequestMethod.GET)
	public List<WqueueDTO> listWqueueForExam(){
		return dbGateway.listWqueueByStates(forExamStates, new Sort(Sort.Direction.DESC, "visitId"));
	}

	@RequestMapping(value="/start-exam", method=RequestMethod.POST)
	public boolean startExam(@RequestParam("visit-id") int visitId){
		dbGateway.startExam(visitId);
		return true;
	}

	@RequestMapping(value="/suspend-exam", method=RequestMethod.POST)
	public boolean suspendExam(@RequestParam("visit-id") int visitId){
		dbGateway.suspendExam(visitId);
		return true;
	}

//	@RequestMapping(value="/end-exam", method=RequestMethod.POST)
//	public boolean endExam(@RequestParam("visit-id") int visitId, @RequestParam("charge") int charge){
//		dbGateway.endExam(visitId, charge);
//		return true;
//	}

}