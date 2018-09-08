package jp.chang.myclinic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

import static jp.chang.myclinic.util.IntRange.Position.End;
import static jp.chang.myclinic.util.IntRange.Position.Single;

public final class IntRange {

    public enum Position {
        Start, End, Single
    }

    public interface Resolver {
        int resolve(String token, Position position);
    }

    private final int startValue;
    private final int endValue;

    private IntRange(int startValue, int endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public int getStartValue() {
        return startValue;
    }

    public int getEndValue() {
        return endValue;
    }

    public void traverse(IntConsumer callback) {
        if (startValue <= endValue) {
            for (int i = startValue; i <= endValue; i++) {
                callback.accept(i);
            }
        } else {
            for (int i = startValue; i >= endValue; i--) {
                callback.accept(i);
            }
        }
    }

    @Override
    public String toString() {
        return "IntRange{" +
                "startValue=" + startValue +
                ", endValue=" + endValue +
                '}';
    }

    private static Pattern partSepPattern = Pattern.compile("\\s*,\\s*");
    private static Pattern rangeSepPattern = Pattern.compile("\\s*-\\s*");

    public static List<IntRange> parseList(String input, Resolver resolver) {
        if (input == null) {
            return Collections.emptyList();
        }
        input = input.trim();
        List<IntRange> result = new ArrayList<>();
        String[] parts = partSepPattern.split(input);
        for (String part : parts) {
            String[] limits = rangeSepPattern.split(part, 2);
            if (limits.length == 1) {
                int value = convertToInt(limits[0], resolver, Single);
                result.add(new IntRange(value, value));
            } else {
                int minValue = convertToInt(limits[0], resolver, Position.Start);
                int maxValue = convertToInt(limits[1], resolver, End);
                result.add(new IntRange(minValue, maxValue));
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static void traverse(List<IntRange> list, IntConsumer callback) {
        for (IntRange range : list) {
            range.traverse(callback);
        }
    }

    public static List<Integer> toIntList(List<IntRange> rangeList){
        List<Integer> result = new ArrayList<>();
        IntRange.traverse(rangeList, result::add);
        return result;
    }

    private static int convertToInt(String s, Resolver resolver, Position pos) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return resolver.resolve(s, pos);
        }
    }

}
