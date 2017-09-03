package jp.chang.myclinic.rest;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;
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
	public BatchEnterResultDTO batchEnterShinryou(@RequestParam("visit-id") int visitId,
											@RequestParam("name") List<String> names){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));
		BatchEnterAccum accum = new BatchEnterAccum();
		for (String name : names) {
			if (name.equals("骨塩定量")) {
				addKotsuenTeiryou(accum, visitId, at);
			} else {
				Optional<ShinryouMasterDTO> optMaster = resolveShinryouMaster(name, at);
				if (optMaster.isPresent()) {
					ShinryouDTO shinryouDTO = new ShinryouDTO();
					shinryouDTO.visitId = visitId;
					shinryouDTO.shinryoucode = optMaster.get().shinryoucode;
					shinryouDTO = dbGateway.enterShinryou(shinryouDTO);
					accum.shinryouIds.add(shinryouDTO.shinryouId);
				} else {
					accum.errorMessages.add(String.format("%sはその期日に使用できません。", name));
				}
			}
		}
		if( accum.errorMessages.size() > 0 ){
			throw new RuntimeException(String.join("", accum.errorMessages));
		}
		return accum.toBatchEnterResult();
	}

	private static class BatchEnterAccum {
		List<Integer> shinryouIds = new ArrayList<>();
		List<Integer> conductIds = new ArrayList<>();
		List<String> errorMessages = new ArrayList<>();

		BatchEnterResultDTO toBatchEnterResult(){
			BatchEnterResultDTO result = new BatchEnterResultDTO();
			result.shinryouIds = shinryouIds;
			result.conductIds = conductIds;
			return result;
		}
	}

	private void addKotsuenTeiryou(BatchEnterAccum accum, int visitId, LocalDate at){
		ConductDTO conduct = new ConductDTO();
		conduct.kind = ConductKind.Gazou.getCode();
		conduct.visitId = visitId;
		int conductId = dbGateway.enterConduct(conduct);
		GazouLabelDTO gazouLabel = new GazouLabelDTO();
		gazouLabel.conductId = conductId;
		gazouLabel.label = "骨塩定量に使用";
		dbGateway.enterGazouLabel(gazouLabel);
		Optional<ShinryouMasterDTO> optShinryouMaster = resolveShinryouMaster("骨塩定量ＭＤ法", at);
		if (!optShinryouMaster.isPresent()) {
			accum.errorMessages.add("骨塩定量ＭＤ法が適用できません。");
			return;
		}
		ShinryouMasterDTO shinryouMaster = optShinryouMaster.get();
		ConductShinryouDTO conductShinryou = new ConductShinryouDTO();
		conductShinryou.conductId = conductId;
		conductShinryou.shinryoucode = shinryouMaster.shinryoucode;
		dbGateway.enterConductShinryou(conductShinryou);
		Optional<KizaiMasterDTO> optKizaiMaster = resolveKizaiMaster("四ツ切", at);
		if( !optKizaiMaster.isPresent() ){
			accum.errorMessages.add("四ツ切が適用できません。");
			return;
		}
		KizaiMasterDTO kizaiMaster = optKizaiMaster.get();
		ConductKizaiDTO conductKizai = new ConductKizaiDTO();
		conductKizai.conductId = conductId;
		conductKizai.kizaicode = kizaiMaster.kizaicode;
		conductKizai.amount = 1.0;
		dbGateway.enterConductKizai(conductKizai);
		accum.conductIds.add(conductId);
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

	private Optional<KizaiMasterDTO> resolveKizaiMaster(String name, LocalDate at){
		Optional<Integer> optKizaicode = masterMap.getKizaicodeByName(name);
		if( optKizaicode.isPresent() ){
			int kizaicode = masterMap.resolveKizaiCode(optKizaicode.get(), at);
			return dbGateway.findKizaiMasterByKizaicode(kizaicode, at);
		} else {
			return dbGateway.findKizaiMasterByName(name, at);
		}
	}

}