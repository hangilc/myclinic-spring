package jp.chang.myclinic.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.web.json.JsonRecentVisit;
import jp.chang.myclinic.web.json.JsonVisit;
import jp.chang.myclinic.web.json.JsonFullVisit;
import jp.chang.myclinic.web.json.JsonText;
import jp.chang.myclinic.web.json.JsonFullDrug;
import jp.chang.myclinic.web.json.JsonFullShinryou;
import jp.chang.myclinic.web.json.JsonFullConduct;

import jp.chang.myclinic.model.Visit;
import jp.chang.myclinic.model.VisitRepository;
import jp.chang.myclinic.model.Text;
import jp.chang.myclinic.model.TextRepository;
import jp.chang.myclinic.model.Drug;
import jp.chang.myclinic.model.DrugRepository;
import jp.chang.myclinic.model.Shinryou;
import jp.chang.myclinic.model.ShinryouRepository;
import jp.chang.myclinic.model.Conduct;
import jp.chang.myclinic.model.ConductRepository;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)
public class VisitController {
	@Autowired VisitRepository visitRepository;
	@Autowired TextRepository textRepository;
	@Autowired DrugRepository drugRepository;
	@Autowired ShinryouRepository shinryouRepository;
	@Autowired ConductRepository conductRepository;

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
		JsonFullVisit.stuff(dst, visit);
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
				return jsonConduct;
			})
			.collect(Collectors.toList());
		dst.setConducts(jsonConducts);
		return dst;
	}

}
