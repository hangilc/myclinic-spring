package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.PracticeConfigDTO;
import jp.chang.myclinic.dto.StringResultDTO;
import jp.chang.myclinic.serverpostgresql.PracticeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/json")
public class ConfigController {

    //private static Logger logger = LoggerFactory.getLogger(ConfigController.class);
    @Value("${myclinic.master-map-file}")
    private String masterMapConfigFilePath;
    @Value("${myclinic.name-map-file}")
    private String nameMapConfigFilePath;
    @Value("${myclinic.shinryou-byoumei-file}")
    private String shinryouByoumeiMapConfigFilePath;
    @Value("${myclinic.powder-drug-file}")
    private String powderDrugFilePath;

    @Autowired
    private PracticeConfig practiceConfig;

    @RequestMapping(value = "/get-practice-config", method = RequestMethod.GET)
    public PracticeConfigDTO getPracticeConfig() {
        return practiceConfig.toDTO();
    }

    @RequestMapping(value = "/get-master-map-config-file-path", method = RequestMethod.GET)
    public StringResultDTO getMasterMapConfigFilePath() {
        Path path = Paths.get(masterMapConfigFilePath);
        StringResultDTO result = new StringResultDTO();
        result.value = path.toAbsolutePath().toString();
        return result;
    }

    @RequestMapping(value = "/get-name-map-config-file-path", method = RequestMethod.GET)
    public StringResultDTO getNameMapConfigFilePath() {
        Path path = Paths.get(nameMapConfigFilePath);
        StringResultDTO result = new StringResultDTO();
        result.value = path.toAbsolutePath().toString();
        return result;
    }

    @RequestMapping(value = "/get-shinryou-byoumei-map-config-file-path", method = RequestMethod.GET)
    public StringResultDTO getShinryouByoumeiMapConfigFilePath() {
        Path path = Paths.get(shinryouByoumeiMapConfigFilePath);
        StringResultDTO result = new StringResultDTO();
        result.value = path.toAbsolutePath().toString();
        return result;
    }

    @RequestMapping(value = "/get-powder-drug-config-file-path", method = RequestMethod.GET)
    public StringResultDTO getPowderDrugFilePath() {
        Path path = Paths.get(powderDrugFilePath);
        StringResultDTO result = new StringResultDTO();
        result.value = path.toAbsolutePath().toString();
        return result;
    }

}
