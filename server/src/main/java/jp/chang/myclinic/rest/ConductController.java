package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ConductDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.GazouLabelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

	@RequestMapping(value="/list-conduct-full-by-ids", method=RequestMethod.GET)
	public List<ConductFullDTO> listByIds(@RequestParam(value="conduct-id", required=false) Optional<List<Integer>> conductIds){
		if( !conductIds.isPresent() ){
			return Collections.emptyList();
		}
		return dbGateway.listConductFullByIds(conductIds.get());
	}

}
