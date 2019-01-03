package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.serverpostgresql.HotlineLogger;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/json")
@Transactional
public class HotlineController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private HotlineLogger hotlineLogger;

    @RequestMapping(value="/get-last-hotline-id", method=GET)
    public int getLastHotlineId(){
        return dbGateway.getLastHotlineId();
    }

    @RequestMapping(value="/list-hotline-in-range", method=GET)
    public List<HotlineDTO> listHotlineInRange(@RequestParam("lower-hotline-id") int lowerHotlineId,
                                               @RequestParam("upper-hotline-id") int upperHotlineId){
        return dbGateway.listHotlineInRange(lowerHotlineId, upperHotlineId);
    }

    @RequestMapping(value="list-todays-hotline-in-range", method=GET)
    public List<HotlineDTO> listTodaysHotlineInRange(@RequestParam("after") int afterId,
                                                     @RequestParam("before") int beforeId){
        return dbGateway.listTodaysHotlineInRange(afterId, beforeId);
    }

    @RequestMapping(value="/enter-hotline", method=RequestMethod.POST)
    public int enterHotline(@RequestBody HotlineDTO hotlineDTO){
        return dbGateway.enterHotline(hotlineDTO);
    }

    @RequestMapping(value="/list-todays-hotline", method=GET)
    public List<HotlineDTO> listTodaysHotline(){
        return dbGateway.listTodaysHotline();
    }

    @RequestMapping(value="/list-recent-hotline", method=GET)
    public List<HotlineDTO> listRecentHotline(@RequestParam("threshold-hotline-id") int thresholdHotlineId){
        return dbGateway.listRecentHotline(thresholdHotlineId);
    }
}
