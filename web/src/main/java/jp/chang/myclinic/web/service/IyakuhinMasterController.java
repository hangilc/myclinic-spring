package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.IyakuhinMaster;
import jp.chang.myclinic.model.IyakuhinMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)

public class IyakuhinMasterController {

    @Autowired
    IyakuhinMasterRepository iyakuhinMasterRepository;

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=search_most_recent_iyakuhin_master", "text"})
    public List<IyakuhinMaster> searchMostRecentIyakuhinMaster(@RequestParam(value="text") String text){
        text = text.trim();
        text = text.replace("\u3000", " "); // zenkaku space
        String[] words = text.split("\\s", 2);
        if( words.length == 1 ){
            return iyakuhinMasterRepository.searchByName(text);
        } else {
            return iyakuhinMasterRepository.searchByName(words[0], words[1]);
        }
    }

}
