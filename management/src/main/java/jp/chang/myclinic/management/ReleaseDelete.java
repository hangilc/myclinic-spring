package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;
import jp.chang.myclinic.util.IntRange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ReleaseDelete {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseDelete.class);

    public static void main(String[] args) throws Exception {
        //noinspection InfiniteLoopStatement
        while (true) {
            doList();
            break;
        }
    }

    private static void doList() throws IOException {
        String cur = ReleaseLib.getCurrentVersion();
        List<Path> paths = ReleaseLib.listReleases();
        Integer currentPos = null;
        for (int i = 0; i < paths.size(); i++) {
            String dir = paths.get(i).getFileName().toString();
            boolean isCurrent = dir.equals(cur);
            if( isCurrent ){
                currentPos = i+1;
            }
            String pre = makePrefix(paths.size(), i, isCurrent);
            System.out.println(pre + dir);
        }
        System.out.println();
        System.out.println("削除するリリースを番号で指定してください。");
        System.out.println("終了するには q と入力してください。");
        System.out.print("> ");
        String line = System.console().readLine();
        line = line.trim();
        if( "q".equals(line) ){

        }
        List<IntRange> ranges = IntRange.parseList(line, makeResolver(currentPos, paths.size()));
    }

    private static IntRange.Resolver makeResolver(Integer current, int size) {
        return (s, pos) -> {
            switch (s) {
                case "*":
                    if( current != null ) {
                        return current;
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
        }
    }

    private static String makePrefix(int total, int index, boolean isCurrent) {
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
