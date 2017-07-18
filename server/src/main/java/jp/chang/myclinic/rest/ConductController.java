package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;

@RestController
@RequestMapping("/json")
@Transactional
public class ConductController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-conduct", method=RequestMethod.GET)
	public ConductDTO getConduct(@RequestParam("conduct-id") int conductId){
		return dbGateway.getConduct(conductId);
	}

	@RequestMapping(value="/get-conduct-full", method=RequestMethod.GET)
	public ConductFullDTO getConductFull(@RequestParam("conduct-id") int conductId){
		return dbGateway.getConductFull(conductId);
	}

	@RequestMapping(value="/get-gazou-label", method=RequestMethod.GET)
	public GazouLabelDTO getGazouLabel(@RequestParam("conduct-id") int conductId){
		return dbGateway.findGazouLabel(conductId);
	}

}
