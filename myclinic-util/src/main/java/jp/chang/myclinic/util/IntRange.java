package jp.chang.myclinic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class IntRange {

    private int startValue;
    private int endValue;

    private IntRange(int startValue, int endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void traverse(Consumer<Integer> proc){
        if( startValue <= endValue){
            for(int i = startValue; i<= endValue; i++){
                proc.accept(i);
            }
        } else {
            for(int i = startValue; i>= endValue; i--){
                proc.accept(i);
            }
        }
    }

    private static Pattern partSepPattern = Pattern.compile("[\\s,]+");
    private static Pattern rangeSepPattern = Pattern.compile("\\s*-\\s*");

    public static List<IntRange> parse(String input, Function<String, Integer> specials){
        if( input == null ){
            return Collections.emptyList();
        }
        input = input.trim();
        List<IntRange> result = new ArrayList<>();
        String[] parts = partSepPattern.split(input);
        for(String part: parts){
            String[] limits = rangeSepPattern.split(part, 2);
            if( limits.length == 1 ){
                int value = convertToInt(limits[0], specials);
                result.add(new IntRange(value, value));
            } else {
                int minValue = convertToInt(limits[0], specials);
                int maxValue = convertToInt(limits[1], specials);
                result.add(new IntRange(minValue, maxValue));
            }
        }
        return result;
    }

    private static int convertToInt(String s, Function<String, Integer> specials){
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException ex){
            Integer value =  specials.apply(s);
            if( value == null ){
                throw new RuntimeException("Invalid integer value: " + s);
            }
            return value;
        }
    }

    public static void traverse(List<IntRange> ranges, Consumer<Integer> proc){
        ranges.forEach(range -> range.traverse(proc));
    }

}
