package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;
import jp.chang.myclinic.util.IntRange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReleaseDelete {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseDelete.class);

    public static void main(String[] args) throws Exception {
        ReleaseDelete instance = new ReleaseDelete();
        instance.run();
    }

    private List<Path> releasePaths;
    private Integer currentIndex;

    private ReleaseDelete(){

    }

    private void run() throws IOException {
        //noinspection InfiniteLoopStatement
        while (true) {
            doUpdate();
            doList();
            doPrompt();
            String line = System.console().readLine();
            line = line.trim();
            if( "q".equals(line) ){
                break;
            }
            try {
                List<IntRange> ranges = IntRange.parseList(line, makeResolver(currentIndex, releasePaths.size()));
                System.out.println(ranges);
            } catch(Exception ex){
                System.err.println(ex);
            }
        }
    }

    private void doUpdate() throws IOException {
        String cur = ReleaseLib.getCurrentVersion();
        this.releasePaths = ReleaseLib.listReleases();
        this.currentIndex = null;
        for (int i = 0; i < releasePaths.size(); i++) {
            String dir = releasePaths.get(i).getFileName().toString();
            if( dir.equals(cur) ){
                this.currentIndex = i;
                break;
            }
        }
    }

    private void doPrompt(){
        System.out.println();
        System.out.println("削除するリリースを番号で指定してください。");
        System.out.println("終了するには q と入力してください。");
        System.out.print("> ");
    }

    private void doList() throws IOException {
        for (int i = 0; i < releasePaths.size(); i++) {
            String dir = releasePaths.get(i).getFileName().toString();
            boolean isCurrent = currentIndex != null && currentIndex == i;
            String pre = makePrefix(releasePaths.size(), i, isCurrent);
            System.out.println(pre + " " + dir);
        }
    }

    private void doListToBeDeleted(List<IntRange> deleteList) throws IOException {
        Set<Integer> deleteSet = new HashSet<>(IntRange.toIntList(deleteList));
        for (int i = 0; i < releasePaths.size(); i++) {
            String dir = releasePaths.get(i).getFileName().toString();
            boolean isCurrent = currentIndex != null && currentIndex == i;
            if( (!isCurrent) && deleteSet.contains(i) ){

            }
            String pre = makePrefix(releasePaths.size(), i, isCurrent);
            System.out.println(pre + " " + dir);
        }
    }

    private IntRange.Resolver makeResolver(Integer current, int size) {
        return (s, pos) -> {
            switch (s) {
                case "*":
                    if( current != null ) {
                        return current + 1;
                    } else {
                        throw new RuntimeException("Current release is not available.");
                    }
                case "":
                    if (pos == IntRange.Position.Start) {
                        return 1;
                    } else if (pos == IntRange.Position.End) {
                        return size;
                    } else {
                        throw new RuntimeException("Invalid int rage");
                    }
                default: throw new RuntimeException("Invalid int rage");
            }
        };
    }

    private String makePrefix(int total, int index, boolean isCurrent) {
        String numFormat = "%d.";
        String curFormat = "*.";
        if (total >= 10) {
            numFormat = "%2d.";
            curFormat = "**.";
        }
        if (isCurrent) {
            return curFormat;
        } else {
            return String.format(numFormat, index + 1);
        }
    }

}
