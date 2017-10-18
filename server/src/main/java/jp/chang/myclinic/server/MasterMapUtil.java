package jp.chang.myclinic.server;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
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

    public ShinryouMasterDTO resolveShinryouMaster(int shinryoucode, LocalDate at){
        int newShinryoucode = masterMap.resolveShinryouCode(shinryoucode, at);
        Optional<ShinryouMasterDTO> opt = dbGateway.findShinryouMasterByShinryoucode(newShinryoucode, at);
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + shinryoucode);
        }
    }

    public ShinryouMasterDTO resolveShinryouMaster(String name, LocalDate at){
        Optional<Integer> optShinryoucode = masterMap.getShinryoucodeByName(name);
        if( optShinryoucode.isPresent() ){
            return resolveShinryouMaster(optShinryoucode.get(), at);
        } else {
            Optional<ShinryouMasterDTO> opt;
            opt = dbGateway.findShinryouMasterByName(name, at);
            if( opt.isPresent() ){
                return opt.get();
            } else {
                throw new RuntimeException("cannot find master for " + name);
            }
        }
    }

    public KizaiMasterDTO resolveKizaiMaster(int kizaicode, LocalDate at){
        int newKizaicode = masterMap.resolveKizaiCode(kizaicode, at);
        Optional<KizaiMasterDTO> opt = dbGateway.findKizaiMasterByKizaicode(newKizaicode, at);
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + kizaicode);
        }
    }

    public KizaiMasterDTO resolveKizaiMaster(String name, LocalDate at){
        Optional<Integer> optKizaicode = masterMap.getKizaicodeByName(name);
        if( optKizaicode.isPresent() ){
            return resolveKizaiMaster(optKizaicode.get(), at);
        } else {
            Optional<KizaiMasterDTO> opt;
            opt = dbGateway.findKizaiMasterByName(name, at);
            if( opt.isPresent() ){
                return opt.get();
            } else {
                throw new RuntimeException("cannot find master for " + name);
            }
        }
    }

    public IyakuhinMasterDTO resolveIyakuhinMaster(int iyakuhincode, LocalDate at){
        int newIyakuhincode = masterMap.resolveIyakuhinCode(iyakuhincode, at);
        Optional<IyakuhinMasterDTO> opt = dbGateway.findIyakuhinMasterByIyakuhincode(newIyakuhincode, at);
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + iyakuhincode);
        }
    }


}
