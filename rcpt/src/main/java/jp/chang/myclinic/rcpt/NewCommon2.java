package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap2.CodeMapEntry;
import jp.chang.myclinic.mastermap2.MapKind;
import jp.chang.myclinic.mastermap2.MasterMap;
import jp.chang.myclinic.rcpt.resolvedmap2.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCommon2 {

    //private static Logger logger = LoggerFactory.getLogger(NewCommon2.class);

    public static class MasterMaps {
        public ResolvedMap resolvedMap;
        public Map<Integer, List<ResolvedByoumei>> shinryouByoumeiMap;
    }

    private NewCommon2() {

    }

    public static MasterMaps getMasterMaps(LocalDate at){
        MasterMap mm = new MasterMap();
        Map<MapKind, List<CodeMapEntry>> entriesMap = mm.loadCodeMaps();
        MasterMaps masterMaps = new MasterMaps();
        ResolvedMap resolvedMap = new ResolvedMap();
        resolvedMap.diseaseAdjMap = new ResolvedDiseaseAdjMap();
        mm.adaptMembersToDate(resolvedMap.diseaseAdjMap, entriesMap.get(MapKind.DiseaseAdj), at);
        resolvedMap.diseaseMap = new ResolvedDiseaseMap();
        mm.adaptMembersToDate(resolvedMap.diseaseMap, entriesMap.get(MapKind.Disease), at);
        resolvedMap.kizaiMap = new ResolvedKizaiMap();
        mm.adaptMembersToDate(resolvedMap.kizaiMap, entriesMap.get(MapKind.Kizai), at);
        resolvedMap.shinryouMap = new ResolvedShinryouMap();
        mm.adaptMembersToDate(resolvedMap.shinryouMap, entriesMap.get(MapKind.Shinryou), at);
        masterMaps.resolvedMap = resolvedMap;
        Map<Integer, List<ResolvedByoumei>> shinryouByoumeiMap = new HashMap<>();
        Map<String, List<List<String>>> shinryouByoumeiNameMap = mm.loadShinryouByoumeiMap();

        masterMaps.shinryouByoumeiMap = shinryouByoumeiMap;
        return masterMaps;
    }
}
