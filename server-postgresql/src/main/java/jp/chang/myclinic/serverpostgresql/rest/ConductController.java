package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.serverpostgresql.MasterMapUtil;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	@RequestMapping(value="/enter-conduct-full", method=RequestMethod.POST)
	public ConductFullDTO enterConductFull(@RequestBody ConductEnterRequestDTO arg){
		ConductDTO conduct = new ConductDTO();
		conduct.visitId = arg.visitId;
		conduct.kind = arg.kind;
		int conductId = dbGateway.enterConduct(conduct);
		if( arg.gazouLabel != null ) {
			GazouLabelDTO gazouLabelDTO = new GazouLabelDTO();
			gazouLabelDTO.conductId = conductId;
			gazouLabelDTO.label = arg.gazouLabel;
			dbGateway.enterGazouLabel(gazouLabelDTO);
		}
		if( arg.shinryouList != null ) {
			for (ConductShinryouDTO shinryou : arg.shinryouList) {
				shinryou.conductId = conductId;
				dbGateway.enterConductShinryou(shinryou);
			}
		}
		if( arg.drugs != null ) {
			for (ConductDrugDTO drug : arg.drugs) {
				drug.conductId = conductId;
				dbGateway.enterConductDrug(drug);
			}
		}
		if( arg.kizaiList != null ) {
			for (ConductKizaiDTO kizai : arg.kizaiList) {
				kizai.conductId = conductId;
				dbGateway.enterConductKizai(kizai);
			}
		}
		return dbGateway.getConductFull(conductId);
	}

	@RequestMapping(value="/enter-inject", method=RequestMethod.POST)
	public Integer enterInject(@RequestParam("visit-id") int visitId,
							   @RequestParam("kind") int conductKindCode,
							   @RequestParam("iyakuhincode") int iyakuhincode,
							   @RequestParam("amount") double amount){
		VisitDTO visit = dbGateway.getVisit(visitId);
		LocalDate at = LocalDate.parse(visit.visitedAt.substring(0, 10));
		ConductKind kind = ConductKind.fromCode(conductKindCode);
		List<ConductShinryouDTO> shinryouList = new ArrayList<>();
		switch(kind){
			case HikaChuusha: {
				shinryouList.add(createConductShinryou("皮下筋注", at));
				break;
			}
			case JoumyakuChuusha: {
				shinryouList.add(createConductShinryou("静注", at));
				break;
			}
			default: throw new RuntimeException("invalid conduct kind: " + conductKindCode);
		}

		return createConduct(visitId,
				kind.getCode(),
				null,
				shinryouList.toArray(new ConductShinryouDTO[]{}),
				new ConductDrugDTO[]{ createConductDrug(iyakuhincode, amount) },
				null
		);
	}

	@RequestMapping(value="/copy-all-conducts", method=RequestMethod.POST)
	public List<Integer> copyAllConducts(@RequestParam("target-visit-id") int targetVisitId,
										 @RequestParam("source-visit-id") int sourceVisitId){
		List<ConductDTO> sourceConducts = dbGateway.listConducts(sourceVisitId);
		List<Integer> newConductIds = new ArrayList<>();
		VisitDTO targetVisit = dbGateway.getVisit(targetVisitId);
		LocalDate at = LocalDate.parse(targetVisit.visitedAt.substring(0, 10));
		for(ConductDTO source: sourceConducts){
			List<ConductShinryouDTO> shinryouList = dbGateway.listConductShinryou(source.conductId).stream()
					.map(shinryou -> {
						ConductShinryouDTO newShinryou = ConductShinryouDTO.copy(shinryou);
						newShinryou.conductId = 0;
						newShinryou.conductShinryouId = 0;
						newShinryou.shinryoucode =
								dbGateway.findShinryouMasterByShinryoucode(shinryou.shinryoucode, at)
									.map(m -> m.shinryoucode)
									.orElseThrow(() -> new RuntimeException("コピー先で資料できません。[" + shinryou.shinryoucode + "]"));
						return newShinryou;
					})
					.collect(Collectors.toList());
			List<ConductDrugDTO> drugs = dbGateway.listConductDrug(source.conductId).stream()
					.map(drug -> {
						ConductDrugDTO newDrug = ConductDrugDTO.copy(drug);
						newDrug.conductId = 0;
						newDrug.conductDrugId = 0;
						return newDrug;
					})
					.collect(Collectors.toList());

			List<ConductKizaiDTO> kizaiList = dbGateway.listConductKizai(source.conductId).stream()
					.map(kizai -> {
						ConductKizaiDTO newKizai = ConductKizaiDTO.copy(kizai);
						newKizai.conductId = 0;
						newKizai.conductKizaiId = 0;
						return newKizai;
					})
					.collect(Collectors.toList());
			int conductId = createConduct(targetVisitId,
					source.kind,
					dbGateway.findGazouLabelString(source.conductId),
					shinryouList.toArray(new ConductShinryouDTO[]{}),
					drugs.toArray(new ConductDrugDTO[]{}),
					kizaiList.toArray(new ConductKizaiDTO[]{})
			);
			newConductIds.add(conductId);
		}
		return newConductIds;
	}

	@RequestMapping(value="/delete-conduct", method=RequestMethod.POST)
	public boolean deleteConduct(@RequestParam("conduct-id") int conductId){
		dbGateway.deleteConduct(conductId);
		return true;
	}

	@RequestMapping(value="/modify-conduct-kind", method=RequestMethod.POST)
	public boolean modifyConductKind(@RequestParam("conduct-id") int conductId, @RequestParam("kind") int kind){
		dbGateway.modifyConductKind(conductId, kind);
		return true;
	}

	private ConductShinryouDTO createConductShinryou(String name, LocalDate at){
		ConductShinryouDTO shinryou = new ConductShinryouDTO();
		shinryou.shinryoucode = masterMapUtil.resolveShinryouMaster(name, at)
			.map(m -> m.shinryoucode).orElseThrow(() ->
						new RuntimeException("診療行為を使用できません。[" + name + "]"));
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
		kizai.kizaicode = masterMapUtil.resolveKizaiMaster(name, at)
			.map(m -> m.kizaicode).orElseThrow(() ->
						new RuntimeException("特定器材を使用できません。[" + name + "]"));
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
