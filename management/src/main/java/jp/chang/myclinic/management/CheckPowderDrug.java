package jp.chang.myclinic.management;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.mastermap.next.CodeMap;
import jp.chang.myclinic.mastermap.next.CodeMapEntry;
import jp.chang.myclinic.mastermap.next.CodeMapRegistry;
import jp.chang.myclinic.mastermap.next.MapKind;
import jp.chang.myclinic.util.PowderDrugMap;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CheckPowderDrug {

    //private static Logger logger = LoggerFactory.getLogger(CheckPowderDrug.class);

    private static class PowderDrugLink {
        private int iyakuhincode;
        private List<Integer> link;
        private int index;

        private PowderDrugLink(int iyakuhincode, List<Integer> link, int index) {
            this.iyakuhincode = iyakuhincode;
            this.link = link;
            this.index = index;
        }

        @Override
        public String toString() {
            return "PowderDrugLink{" +
                    "iyakuhincode=" + iyakuhincode +
                    ", index=" + index +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        if( args.length != 1 ){
            System.err.println("Usage: CheckPowderDrug date");
            System.exit(1);
        }
        LocalDate at = LocalDate.parse(args[0]);
        String serviceUrl = System.getenv("MYCLINIC_SERVICE");
        Service.setServerUrl(serviceUrl);
        String powderDrugConfigPath = Service.api.getPowderDrugConfigFilePathCall().execute().body().value;
        PowderDrugMap powderDrugMap = PowderDrugMap.load(Paths.get(powderDrugConfigPath));
        String masterMapConfigPath = Service.api.getMasterMapConfigFilePathCall().execute().body().value;
        CodeMapRegistry codeMapRegistry = CodeMapRegistry.load(Paths.get(masterMapConfigPath));
        CodeMap codeMap = codeMapRegistry.getCodeMap(MapKind.Iyakuhin);
        List<List<Integer>> codeLinks = composeCodeLinks(codeMap.getEntries());
        List<List<PowderDrugLink>> powderLinks = new ArrayList<>();
        for(int iyakuhincode: powderDrugMap.keySet()){
            if( codeMap.resolve(iyakuhincode, at) == 0 ){
                continue;
            }
            PowderDrugLink pdl = createPowderDrugLink(codeLinks, iyakuhincode);
            boolean done = false;
            for(List<PowderDrugLink> links: powderLinks){
                if( links.get(0).link.equals(pdl.link) ){
                    links.add(pdl);
                    done = true;
                    break;
                }
            }
            if( !done ){
                List<PowderDrugLink> links = new ArrayList<>();
                links.add(pdl);
                powderLinks.add(links);
            }
        }
        List<PowderDrugLink> lastPowders = new ArrayList<>();
        for(List<PowderDrugLink> links: powderLinks){
            links.stream().max(Comparator.comparing(a -> a.index)).ifPresent(lastPowders::add);
        }
        for(PowderDrugLink link: lastPowders){
            IyakuhinMasterDTO master = Service.api.resolveIyakuhinMasterCall(link.iyakuhincode, at.toString())
                    .execute().body();
            if( master == null ){
                System.out.println("Cannot resolve master for " + link.iyakuhincode);
            }
        }
    }

    private static PowderDrugLink createPowderDrugLink(List<List<Integer>> codeLinks, int iyakuhincode){
        for(List<Integer> link: codeLinks){
            int n = link.size();
            for(int i=0;i<n;i++){
                if( link.get(i) == iyakuhincode ){
                    return new PowderDrugLink(iyakuhincode, link, i);
                }
            }
        }
        return new PowderDrugLink(iyakuhincode, List.of(iyakuhincode), 0);
    }

    private static List<List<Integer>> composeCodeLinks(List<CodeMapEntry> entries){
        List<List<Integer>> result = new ArrayList<>();
        for(CodeMapEntry entry: entries){
            int prevCode = entry.getOldCode();
            int nextCode = entry.getNewCode();
            boolean done = false;
            for(List<Integer> links: result){
                if( links.get(links.size()-1) == prevCode ){
                    links.add(nextCode);
                    done = true;
                    break;
                }
            }
            if( !done ){
                List<Integer> links = new ArrayList<>();
                links.add(prevCode);
                links.add(nextCode);
                result.add(links);
            }
        }
        return result;
    }

}
