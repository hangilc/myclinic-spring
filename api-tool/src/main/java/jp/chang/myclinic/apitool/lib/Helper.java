package jp.chang.myclinic.apitool.lib;

import java.util.Arrays;

import static java.util.stream.Collectors.*;

public class Helper {

    public String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }

    public String snakeToCapital(String s){
        return Arrays.stream(s.split("_"))
                .map(this::capitalize)
                .collect(joining(""));
    }

}
