package jp.chang.myclinic;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class MasterMapUtil {

    @Autowired
    private MasterMap masterMap;
    @Autowired
    private DbGateway dbGateway;

    public ShinryouMasterDTO resolveShinryouMaster(String name, LocalDate at){
        Optional<Integer> optShinryoucode = masterMap.getShinryoucodeByName(name);
        Optional<ShinryouMasterDTO> opt;
        if( optShinryoucode.isPresent() ){
            int shinryoucode = masterMap.resolveShinryouCode(optShinryoucode.get(), at);
            opt = dbGateway.findShinryouMasterByShinryoucode(shinryoucode, at);
        } else {
            opt = dbGateway.findShinryouMasterByName(name, at);
        }
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + name);
        }
    }

    public KizaiMasterDTO resolveKizaiMaster(String name, LocalDate at){
        Optional<Integer> optKizaicode = masterMap.getKizaicodeByName(name);
        Optional<KizaiMasterDTO> opt;
        if( optKizaicode.isPresent() ){
            int kizaicode = masterMap.resolveKizaiCode(optKizaicode.get(), at);
            opt = dbGateway.findKizaiMasterByKizaicode(kizaicode, at);
        } else {
            opt = dbGateway.findKizaiMasterByName(name, at);
        }
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + name);
        }
    }

}
