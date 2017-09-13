package jp.chang.myclinic.rest;

import jp.chang.myclinic.MasterMapUtil;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class ConductController {

	@Autowired
	private DbGateway dbGateway;
	@Autowired
	private MasterMapUtil masterMapUtil;

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
	public List<ConductFullDTO> listByIds(@RequestParam(value="conduct-id", defaultValue="") List<Integer> conductIds){
		return dbGateway.listConductFullByIds(conductIds);
	}

	@RequestMapping(value="/enter-xp", method=RequestMethod.POST)
	public Integer enterXp(@RequestParam("visit-id") int visitId, @RequestParam("label") String label,
						   @RequestParam("film") String film){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));

		return createConduct(visitId, ConductKind.Gazou.getCode(), label,
				new ConductShinryouDTO[]{
						createConductShinryou("単純撮影", at),
						createConductShinryou("単純撮影診断", at)
				},
				null,
				new ConductKizaiDTO[]{ createConductKizai(film, at, 1) }
		);
	}

	@RequestMapping(value="/enter-inject", method=RequestMethod.POST)
	public Integer enterInject(@RequestParam("visit-id") int visitId, @RequestParam("shinryou") String shinryouName,
							   @RequestParam("iyakuhincode") int iyakuhincode,
							   @RequestParam("amount") double amount){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));

		return createConduct(visitId, ConductKind.Gazou.getCode(),
				null,
				new ConductShinryouDTO[]{ createConductShinryou(shinryouName, at) },
				new ConductDrugDTO[]{ createConductDrug(iyakuhincode, amount) },
				null
		);
	}

	private ConductShinryouDTO createConductShinryou(String name, LocalDate at){
		ConductShinryouDTO shinryou = new ConductShinryouDTO();
		shinryou.shinryoucode = masterMapUtil.resolveShinryouMaster(name, at).shinryoucode;
		return shinryou;
	}

	private ConductDrugDTO createConductDrug(int iyakuhincode, double amount){
		ConductDrugDTO drug = new ConductDrugDTO();
		drug.iyakuhincode = iyakuhincode;
		drug.amount = amount;
		return drug;
	}

	private ConductKizaiDTO createConductKizai(String name, LocalDate at, double amount){
		ConductKizaiDTO kizai = new ConductKizaiDTO();
		kizai.kizaicode = masterMapUtil.resolveKizaiMaster(name, at).kizaicode;
		kizai.amount = amount;
		return kizai;
	}

	private int createConduct(int visitId, int kind, String gazouLabel, ConductShinryouDTO[] shinryouList,
							  ConductDrugDTO[] drugs, ConductKizaiDTO[] kizaiList){
		ConductDTO conduct = new ConductDTO();
		conduct.visitId = visitId;
		conduct.kind = kind;
		conduct.conductId = dbGateway.enterConduct(conduct);
		if( gazouLabel != null ){
			GazouLabelDTO gazouLabelDTO = new GazouLabelDTO();
			gazouLabelDTO.conductId = conduct.conductId;
			gazouLabelDTO.label = gazouLabel;
			dbGateway.enterGazouLabel(gazouLabelDTO);
		}
		if( shinryouList != null ){
			for(ConductShinryouDTO shinryou: shinryouList){
				shinryou.conductId = conduct.conductId;
				dbGateway.enterConductShinryou(shinryou);
			}
		}
		if( drugs != null ){
			for(ConductDrugDTO drug: drugs){
				drug.conductId = conduct.conductId;
				dbGateway.enterConductDrug(drug);
			}
		}
		if( kizaiList != null ){
			for(ConductKizaiDTO kizai: kizaiList){
				kizai.conductId = conduct.conductId;
				dbGateway.enterConductKizai(kizai);
			}
		}
		return conduct.conductId;
	}

}
