package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.Wqueue;
import jp.chang.myclinic.model.WqueueRepository;
import jp.chang.myclinic.model.WqueueState;
import jp.chang.myclinic.web.service.json.JsonFullWqueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hangil on 2017/03/03.
 */
@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)
public class WqueueController {
    @Autowired
    private WqueueRepository wqueueRepository;

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=list_full_wqueue_for_exam"})
    public List<JsonFullWqueue> listFullWqueueForExam(){
        Set<WqueueState> stateSet = EnumSet.of(WqueueState.WaitExam, WqueueState.WaitReExam);
        List<Wqueue> wqueueList = wqueueRepository.findByStateSet(stateSet);
        return wqueueList.stream()
                .map(JsonFullWqueue::fromWqueue)
                .collect(Collectors.toList());
    }

}
