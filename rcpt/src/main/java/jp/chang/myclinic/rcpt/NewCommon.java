package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.mastermap.next.MapKind;
import jp.chang.myclinic.mastermap.next.MasterMapManager;
import jp.chang.myclinic.mastermap.next.ShinryouByoumeiMap;
import jp.chang.myclinic.rcpt.resolvedmap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;

public class NewCommon {

    private static Logger logger = LoggerFactory.getLogger(Common.class);

    private NewCommon() { }

    public static class MasterMaps {
        public ResolvedMap resolvedMap;
        //public Map<Integer, List<ResolvedByoumei>> shinryouByoumeiMap;
    }

    public static MasterMaps getMasterMaps(LocalDate at) throws IOException {
        String nameMapConfigFile = Service.api.getNameMapConfigFilePathCall().execute().body().value;
        String masterMapConfigFile = Service.api.getMasterMapConfigFilePathCall().execute().body().value;
        MasterMapManager manager = MasterMapManager.load(nameMapConfigFile, masterMapConfigFile);
        Resolver shinryouResolver = (name, atDate) -> manager.resolve(MapKind.Shinryou, name, atDate);
        Resolver shoubyoumeiResolver = (name, atDate) -> manager.resolve(MapKind.Disease, name, atDate);
        Resolver shuushokugoResolver = (name, atDate) -> manager.resolve(MapKind.DiseaseAdj, name, atDate);
        ResolvedMap resolvedMap = new ResolvedMap();
        resolvedMap.shinryouMap = new ResolvedShinryouMap(shinryouResolver, at);
        resolvedMap.kizaiMap = new ResolvedKizaiMap(
                (name, atDate) -> manager.resolve(MapKind.Kizai, name, atDate),
                at);
        resolvedMap.diseaseMap = new ResolvedDiseaseMap(shoubyoumeiResolver, at);
        resolvedMap.diseaseAdjMap = new ResolvedDiseaseAdjMap(shuushokugoResolver, at);
        String shinryouByoumeiConfigFile = Service.api.getShinryouByoumeiMapConfigFilePathCall().execute().body().value;
        ShinryouByoumeiMap shinryouByoumeiMap = ShinryouByoumeiMap.loadFromYaml(Paths.get(shinryouByoumeiConfigFile));
        MasterMaps result = new MasterMaps();
        result.resolvedMap = resolvedMap;
//        result.shinryouByoumeiMap = new HashMap<>();
//        for(String shinryouName: shinryouByoumeiMap.keySet()){
//            List<ByoumeiByName> shuushokugoByNames = shinryouByoumeiMap.get(shinryouName);
//            List<ResolvedByoumei> shuushokugoList = shuushokugoByNames.stream()
//                    .map(s -> ResolvedByoumei.fromByoumeiByName(s, shoubyoumeiResolver, shuushokugoResolver, at))
//                    .collect(Collectors.toList());
//            result.shinryouByoumeiMap.put(shinryouResolver.resolve(shinryouName, at), shuushokugoList);
//        }
        return result;
    }

}
