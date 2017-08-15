package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class WqueueController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/list-wqueue-full", method=RequestMethod.GET)
	public List<WqueueFullDTO> listWqueueFull(){
		return dbGateway.listWqueueFull();
	}

	@RequestMapping(value="/try-delete-wqueue", method=RequestMethod.POST)
	public boolean tryDeleteWqueue(@RequestParam("visit-id") int visitId){
		Optional<WqueueDTO> wqueueDTO = dbGateway.findWqueue(visitId);
		if( wqueueDTO.isPresent() ){
			dbGateway.deleteWqueue(wqueueDTO.get());
		}
		return true;
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

	@RequestMapping(value="/end-exam", method=RequestMethod.POST)
	public boolean endExam(@RequestParam("visit-id") int visitId, @RequestParam("charge") int charge){
		dbGateway.endExam(visitId, charge);
		return true;
	}

}