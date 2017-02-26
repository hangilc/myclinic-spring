package jp.chang.myclinic.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.web.json.JsonRecentVisit;
import jp.chang.myclinic.web.json.JsonVisit;

import jp.chang.myclinic.model.Visit;
import jp.chang.myclinic.model.VisitRepository;

import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping(value="/service", params="_q")
public class VisitController {
	@Autowired VisitRepository visitRepository;

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
}
