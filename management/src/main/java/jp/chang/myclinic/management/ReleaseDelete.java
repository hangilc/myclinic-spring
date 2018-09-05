package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ReleaseDelete {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseDelete.class);

    public static void main(String[] args) throws Exception {
        //noinspection InfiniteLoopStatement
        while(true){
            doList();
            break;
        }
    }

    private static void doList() throws IOException {
        String cur = ReleaseLib.getCurrentVersion();
        List<Path> paths = ReleaseLib.listReleases();
        for(int i=0;i<paths.size();i++){
            String dir = paths.get(i).getFileName().toString();
            boolean isCurrent = dir.equals(cur);
            String pre = makePrefix(paths.size(), i, isCurrent);
            System.out.println(pre + dir);
        }
    }

    private static String makePrefix(int total, int index, boolean isCurrent){
        String numFormat = "%d.";
        String curFormat = "*.";
        if( total >= 10 ){
            numFormat = "%2d.";
            curFormat = "**.";
        }
        if( isCurrent ){
            return curFormat;
        } else {
            return String.format(numFormat, index+1);
        }
    }

}
