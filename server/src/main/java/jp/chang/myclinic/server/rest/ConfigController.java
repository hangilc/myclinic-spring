package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.PracticeConfigDTO;
import jp.chang.myclinic.server.PracticeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
public class ConfigController {

    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);
    @Autowired
    private PracticeConfig practiceConfig;

    @RequestMapping(value="/get-practice-config", method=RequestMethod.GET)
    public PracticeConfigDTO getPracticeConfig(){
        return practiceConfig.toDTO();
    }
}
