package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.*;
import jp.chang.myclinic.web.service.json.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)
public class VisitController {
	@Autowired
	VisitRepository visitRepository;
	@Autowired
	TextRepository textRepository;
	@Autowired
	DrugRepository drugRepository;
	@Autowired
	ShinryouRepository shinryouRepository;
	@Autowired
	ConductRepository conductRepository;
	@Autowired
	GazouLabelRepository gazouLabelRepository;
	@Autowired
	ConductShinryouRepository conductShinryouRepository;
	@Autowired
	ConductDrugRepository conductDrugRepository;
	@Autowired
	ConductKizaiRepository conductKizaiRepository;
	@Autowired
	ShahokokuhoRepository shahokokuhoRepository;
	@Autowired
	KoukikoureiRepository koukikoureiRepository;
	@Autowired
	RoujinRepository roujinRepository;
	@Autowired
	KouhiRepository kouhiRepository;

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=recent_visits"})
	public List<JsonRecentVisit> recentVisits() {
		Pageable pageable = new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "visitId"));
		List<Visit> visits = visitRepository.findAllWithPatient(pageable);
		List<JsonRecentVisit> ret = new ArrayList<>();
		for(Visit visit: visits){
			JsonRecentVisit recentVisit = JsonRecentVisit.fromVisit(visit);
			ret.add(recentVisit);
		}
		return ret;
	}

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=get_visit", "visit_id"})
	public JsonVisit getVisit(@RequestParam(value="visit_id") int visitId){
		Visit visit = visitRepository.findOne(visitId);
		return JsonVisit.fromVisit(visit);
	}

	@RequestMapping(value="", method=RequestMethod.POST, params={"_q=enter_visit"})
	@Transactional()
	public Integer enterVisit(@RequestBody JsonVisit jsonVisit){
		jsonVisit.setVisitId(null);
		Visit visit = JsonVisit.toVisit(jsonVisit);
		visitRepository.saveAndFlush(visit);
		return visit.getVisitId();
	}

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=get_full_visit", "visit_id"})
	public JsonFullVisit getFullVisit(@RequestParam("visit_id") int visitId){
		JsonFullVisit dst = new JsonFullVisit();
		Visit visit = visitRepository.findOne(visitId);
		return extendVisit(visit);
	}

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=calc_visits", "patient_id"})
	public int calcVisits(@RequestParam("patient_id") int patientId){
		return visitRepository.countByPatientId(patientId);
	}

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=list_full_visits", "patient_id", "offset", "n"})
	public List<JsonFullVisit> listFullVisits(@RequestParam("patient_id") int patientId,
											  @RequestParam("offset") int offset, @RequestParam("n") int n){
		Pageable pageable = new PageRequest(offset/n, n, new Sort(Sort.Direction.DESC, "visitId"));
		List<Visit> visits = visitRepository.findByPatientId(patientId, pageable);
		return visits.stream()
				.map(this::extendVisit)
				.collect(Collectors.toList());
	}

	private JsonFullVisit extendVisit(Visit visit){
		int visitId = visit.getVisitId();
		JsonFullVisit dst = new JsonFullVisit();
		JsonFullVisit.stuff(dst, visit);
		if( visit.getShahokokuhoId() > 0 ){
			Shahokokuho shahokokuho = shahokokuhoRepository.findOne(visit.getShahokokuhoId());
			JsonShahokokuho jsonShahokokuho = JsonShahokokuho.fromShahokokuho((shahokokuho));
			dst.setShahokokuho(jsonShahokokuho);
		}
		if( visit.getKoukikoureiId() > 0 ){
			Koukikourei koukikourei = koukikoureiRepository.findOne(visit.getKoukikoureiId());
			JsonKoukikourei jsonKoukikourei = JsonKoukikourei.fromKoukikourei((koukikourei));
			dst.setKoukikourei(jsonKoukikourei);
		}
		if( visit.getRoujinId() > 0 ){
			Roujin roujin = roujinRepository.findOne(visit.getRoujinId());
			JsonRoujin jsonRoujin = JsonRoujin.fromRoujin((roujin));
			dst.setRoujin(jsonRoujin);
		}
		List<JsonKouhi> kouhiList = new ArrayList<>();
		if( visit.getKouhi1Id() > 0 ){
			Kouhi kouhi = kouhiRepository.findOne(visit.getKouhi1Id());
			JsonKouhi jsonKouhi = JsonKouhi.fromKouhi(kouhi);
			kouhiList.add(jsonKouhi);
		}
		if( visit.getKouhi2Id() > 0 ){
			Kouhi kouhi = kouhiRepository.findOne(visit.getKouhi2Id());
			JsonKouhi jsonKouhi = JsonKouhi.fromKouhi(kouhi);
			kouhiList.add(jsonKouhi);
		}
		if( visit.getKouhi3Id() > 0 ){
			Kouhi kouhi = kouhiRepository.findOne(visit.getKouhi3Id());
			JsonKouhi jsonKouhi = JsonKouhi.fromKouhi(kouhi);
			kouhiList.add(jsonKouhi);
		}
		dst.setKouhiList(kouhiList);
		List<Text> texts = textRepository.findAllByVisitId(visitId);
		List<JsonText> jsonTexts = texts.stream()
				.map(JsonText::fromText)
				.collect(Collectors.toList());
		dst.setTexts(jsonTexts);
		List<Drug> drugs = drugRepository.findByVisitIdWithMaster(visitId);
		List<JsonFullDrug> jsonDrugs = drugs.stream()
				.map(JsonFullDrug::create)
				.collect(Collectors.toList());
		dst.setDrugs(jsonDrugs);
		List<Shinryou> shinryouList = shinryouRepository.findByVisitIdWithMaster(visitId);
		List<JsonFullShinryou> jsonShinryouList = shinryouList.stream()
				.map(JsonFullShinryou::create)
				.collect(Collectors.toList());
		dst.setShinryouList(jsonShinryouList);
		List<Conduct> conducts = conductRepository.findByVisitId(visitId);
		List<JsonFullConduct> jsonConducts = conducts.stream()
				.map(c -> {
					JsonFullConduct jsonConduct = JsonFullConduct.create(c);
					Optional<String> gazouLabel = gazouLabelRepository.findOneByConductId(c.getConductId())
							.map(g -> g.getLabel());
					jsonConduct.setGazouLabel(gazouLabel.orElse(""));
					List<ConductShinryou> conductShinryouList = conductShinryouRepository.findByConductIdWithMaster(c.getConductId());
					List<JsonFullConductShinryou> jsonConductShinryouList = conductShinryouList.stream()
							.map(JsonFullConductShinryou::create)
							.collect(Collectors.toList());
					jsonConduct.setShinryouList(jsonConductShinryouList);
					List<ConductDrug> conductDrugs = conductDrugRepository.findByConductIdWithMaster(c.getConductId());
					List<JsonFullConductDrug> jsonConductDrugs = conductDrugs.stream()
							.map(JsonFullConductDrug::create)
							.collect(Collectors.toList());
					jsonConduct.setDrugs(jsonConductDrugs);
					List<ConductKizai> conductKizaiList = conductKizaiRepository.findByConductIdWithMaster(c.getConductId());
					List<JsonFullConductKizai> jsonConductKizaiList = conductKizaiList.stream()
							.map(JsonFullConductKizai::create)
							.collect(Collectors.toList());
					jsonConduct.setKizaiList(jsonConductKizaiList);
					return jsonConduct;
				})
				.collect(Collectors.toList());
		dst.setConducts(jsonConducts);
		return dst;
	}

}
