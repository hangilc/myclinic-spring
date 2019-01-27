package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class MasterMapController {

    //private static Logger logger = LoggerFactory.getLogger(MasterMapController.class);

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value = "/batch-resolve-shinryou-names", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> batchResolveShinryouNames(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at,
            @RequestBody List<List<String>> args) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                dbGateway.findShinryouMasterByName(key, at).ifPresent(m -> result.put(key, m.shinryoucode));
            } else {
                for (String opt : arg.subList(1, arg.size())) {
                    Optional<ShinryouMasterDTO> optMaster = dbGateway.findShinryouMasterByName(opt, at);
                    if (optMaster.isPresent()) {
                        result.put(key, optMaster.get().shinryoucode);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/batch-resolve-kizai-names", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> batchResolveKizaiNames(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at,
            @RequestBody List<List<String>> args) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                dbGateway.findKizaiMasterByName(key, at).ifPresent(m -> result.put(key, m.kizaicode));
            } else {
                for (String opt : arg.subList(1, arg.size())) {
                    Optional<KizaiMasterDTO> optMaster = dbGateway.findKizaiMasterByName(opt, at);
                    if (optMaster.isPresent()) {
                        result.put(key, optMaster.get().kizaicode);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/batch-resolve-byoumei-names", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> batchResolveByoumeiNames(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at,
            @RequestBody List<List<String>> args) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                dbGateway.findByoumeiMasterByName(key, at).ifPresent(m -> result.put(key, m.shoubyoumeicode));
            } else {
                for (String opt : arg.subList(1, arg.size())) {
                    Optional<ByoumeiMasterDTO> optMaster = dbGateway.findByoumeiMasterByName(opt, at);
                    if (optMaster.isPresent()) {
                        result.put(key, optMaster.get().shoubyoumeicode);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/batch-resolve-shuushokugo-names", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> batchResolveShuushokugoNames(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at,
            @RequestBody List<List<String>> args) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                dbGateway.findShuushokugoMasterByName(key).ifPresent(m -> result.put(key, m.shuushokugocode));
            } else {
                for (String opt : arg.subList(1, arg.size())) {
                    Optional<ShuushokugoMasterDTO> optMaster = dbGateway.findShuushokugoMasterByName(opt);
                    if (optMaster.isPresent()) {
                        result.put(key, optMaster.get().shuushokugocode);
                        break;
                    }
                }
            }
        }
        return result;
    }

}
