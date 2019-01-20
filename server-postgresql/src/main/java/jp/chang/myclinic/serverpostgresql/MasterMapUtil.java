package jp.chang.myclinic.serverpostgresql;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.mastermap2.CodeMapEntry;
import jp.chang.myclinic.mastermap2.MapKind;
import jp.chang.myclinic.mastermap2.MasterMap;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MasterMapUtil {

    @Autowired
    @Qualifier("master-code-maps")
    private Map<MapKind, List<CodeMapEntry>> masterCodeMaps;
    @Autowired
    @Qualifier("master-name-maps")
    private Map<MapKind, Map<String, Integer>> masterNameMaps;
    @Autowired
    private DbGateway dbGateway;

    public int adaptShinryoucode(int shinryoucode, LocalDate at){
        MasterMap mm = new MasterMap();
        return mm.adaptCodeToDate(shinryoucode, masterCodeMaps.get(MapKind.Shinryou), at);
    }

    public Optional<Integer> getShinryoucodeByName(String name, LocalDate at){
        return Optional.ofNullable(masterNameMaps.get(MapKind.Shinryou).getOrDefault(name, null))
                .map(code -> adaptShinryoucode(code, at));
    }

    public Optional<ShinryouMasterDTO> resolveShinryouMaster(String name, LocalDate at){
        Optional<Integer> optCode = getShinryoucodeByName(name, at);
        if( optCode.isPresent() ){
            return dbGateway.findShinryouMasterByShinryoucode(optCode.get(), at);
        } else {
            return dbGateway.findShinryouMasterByName(name, at);
        }
    }

    public int adaptKizaicode(int kizaicode, LocalDate at){
        MasterMap mm = new MasterMap();
        return mm.adaptCodeToDate(kizaicode, masterCodeMaps.get(MapKind.Kizai), at);
    }

    public Optional<Integer> getKizaicodeByName(String name, LocalDate at){
        return Optional.ofNullable(masterNameMaps.get(MapKind.Kizai).getOrDefault(name, null))
                .map(code -> adaptKizaicode(code, at));
    }

    public Optional<KizaiMasterDTO> resolveKizaiMaster(String name, LocalDate at){
        Optional<Integer> optKizaicode = getKizaicodeByName(name, at);
        if( optKizaicode.isPresent() ){
            return dbGateway.findKizaiMasterByKizaicode(optKizaicode.get(), at);
        } else {
            return dbGateway.findKizaiMasterByName(name, at);
        }
    }

    public int adaptIyakuhincode(int iyakuhincode, LocalDate at){
        MasterMap mm = new MasterMap();
        return mm.adaptCodeToDate(iyakuhincode, masterCodeMaps.get(MapKind.Iyakuhin), at);
    }

    public IyakuhinMasterDTO resolveIyakuhinMaster(int iyakuhincode, LocalDate at){
        //int newIyakuhincode = masterMap.resolveIyakuhinCode(iyakuhincode, at);
        int newIyakuhincode = adaptIyakuhincode(iyakuhincode, at);
        Optional<IyakuhinMasterDTO> opt = dbGateway.findIyakuhinMasterByIyakuhincode(newIyakuhincode, at);
        if( opt.isPresent() ){
            return opt.get();
        } else {
            throw new RuntimeException("cannot find master for " + iyakuhincode);
        }
    }


}
