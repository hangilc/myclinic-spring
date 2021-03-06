package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ReleaseLabel {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseLabel.class);

    private static void usage(PrintStream out){
        out.println("Usage: release-label           --> interactive");
        out.println("       release-label create release-dir label");
    }

    public static void main(String[] args) throws Exception {
        if( args.length == 0 ){
            new ReleaseLabel().interactive();
        } else {
            String cmd = args[0];
            switch(cmd){
                case "create": {
                    if( args.length < 3 ){
                        usage(System.err);
                        System.exit(1);
                    }
                    String[] labelItems = Arrays.copyOfRange(args, 2, args.length);
                    String label = String.join(" ", labelItems);
                    new ReleaseLabel().create(args[1], label);
                    break;
                }
                default: {
                    usage(System.err);
                    System.exit(1);
                }
            }
        }
    }

    private ReleaseLabel() {

    }

    private void create(String releaseDir, String label) throws IOException {
        Path releasePath = Paths.get(releaseDir);
        if( ReleaseLib.getVersion(releasePath) == null ){
            System.err.println("Does not appear to be release directory: " + releaseDir);
            System.exit(1);
        }
        ReleaseLib.createLabel(releasePath, label);
    }

    private void interactive() throws IOException {
        //noinspection InfiniteLoopStatement
        while(true){
            System.out.println("### RELEASE LABEL ################");
            List<Path> paths = ReleaseLib.listReleases();
            String cur = ReleaseLib.getCurrentVersion();
            Integer currentIndex = null;
            for(int i=0;i<paths.size();i++){
                Path path = paths.get(i);
                Path filePath = path.getFileName();
                boolean isCurrent = cur != null && cur.equals(filePath.toString());
                if( isCurrent ){
                    currentIndex = i;
                }
                String pre = makePrefix(paths.size(), i, isCurrent);
                String label = ReleaseLib.getLabel(path);
                System.out.println(pre + " " + filePath.toString() + " " + label);
            }
            System.out.println("リリースを番号で選択してください。（q：終了）");
            System.out.print("> ");
            String input = System.console().readLine().trim();
            if( input.equals("q") ){
                break;
            }
            try {
                int index;
                if( input.equals("*") ){
                    if( currentIndex == null ){
                        System.out.println("現在のリリースがありません。");
                        continue;
                    }
                    index = currentIndex;
                } else {
                    index = Integer.parseInt(input) - 1;
                }
                Path selectedPath = paths.get(index);
                if( selectedPath == null ){
                    System.out.println("対応するリリースがありません。");
                    continue;
                }
                System.out.println("selected release: " + selectedPath.toString());
                System.out.println("設定するラベルを入力してください。");
                System.out.print("label> ");
                String label = System.console().readLine().trim();
                ReleaseLib.createLabel(selectedPath, label);
            } catch(NumberFormatException ex){
                System.out.println("リリースの番号が不適切です。");
            }
        }
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
