package jp.chang.myclinic.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PowderDrugMap extends HashMap<Integer, Double> {

    //private static Logger logger = LoggerFactory.getLogger(PowderDrugMap.class);
    private static Pattern commentPattern = Pattern.compile("//.*");

    private PowderDrugMap() {

    }

    public static PowderDrugMap load(Path src) throws IOException {
        PowderDrugMap powderDrugMap = new PowderDrugMap();
        try(Stream<String> lines = Files.lines(src, StandardCharsets.UTF_8) ){
            lines.forEach(powderDrugMap::processLine);
        }
        return powderDrugMap;
    }

    private void processLine(String line){
        String origLine = line;
        Matcher matcher = commentPattern.matcher(line);
        line = StringUtil.trimSpaces(matcher.replaceFirst(""));
        if( !line.isEmpty() ){
            String[] toks = line.split("[\\s　]+", 2);
            if( toks.length != 2 ){
                throw new RuntimeException("Invalid powder drug line: " + origLine);
            }
            Integer i = Integer.parseInt(toks[0]);
            Double d = Double.parseDouble(toks[1]);
            put(i, d);
        }
    }

}
