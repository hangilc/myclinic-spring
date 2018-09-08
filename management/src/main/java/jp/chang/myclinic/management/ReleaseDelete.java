package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;
import jp.chang.myclinic.util.IntRange;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashSet;
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
            System.out.println("### DELETE RELEASES ########################");
            doList();
            doPrompt();
            String line = System.console().readLine();
            line = line.trim();
            if( "q".equals(line) ){
                break;
            }
            try {
                List<IntRange> ranges = IntRange.parseList(line, makeResolver(currentIndex, releasePaths.size()));
                doDelete(ranges);
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

    private void doDelete(List<IntRange> deleteList) throws IOException {
        Set<Integer> deleteSet = new LinkedHashSet<>(IntRange.toIntList(deleteList));
        deleteSet.remove(currentIndex+1);
        for (int i = 0; i < releasePaths.size(); i++) {
            String dir = releasePaths.get(i).getFileName().toString();
            boolean isCurrent = currentIndex != null && currentIndex == i;
            String pre = " ";
            if( isCurrent ){
                pre = "*";
            } else if( deleteSet.contains(i+1) ){
                pre = "D";
            }
            System.out.println(pre + ". " + dir);
        }
        System.out.print("D のついたリリースを削除していいですか？");
        //noinspection InfiniteLoopStatement
        while( true ) {
            System.out.print("(Y/N) > ");
            String input = System.console().readLine().trim();
            if( input.startsWith("Y") ){
                for(Integer index: deleteSet){
                    Path path = releasePaths.get(index-1);
                    removeDir(path);
                    System.out.println(path + " を削除しました。");
                    System.out.println();
                }
                break;
            } else if( input.startsWith("N") ){
                break;
            } else {
                System.out.println("Y か N を入力してください。");
            }
        }
    }

    private void removeDir(Path dir) throws IOException {
        Files.walkFileTree(dir, new FileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dirPath, IOException exc) throws IOException {
                Files.delete(dirPath);
                return FileVisitResult.CONTINUE;
            }
        });
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
