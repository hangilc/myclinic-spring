package jp.chang.myclinic.util.value;

public class IntegerRangeLogic implements Logic<Integer> {

    private Logic<Integer> src;
    private int lower;
    private int upper;

    public IntegerRangeLogic(Logic<Integer> src, int lower, int upper) {
        this.src = src;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Integer getValue(String name, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        Integer value = src.getValue(name, em);
        if( em.hasErrorSince(ne) ){
            return null;
        }
        if( value == null ){
            em.add(String.format("%sが設定されていません。", name));
            return null;
        }
        if( value >= lower && value <= upper ){
            return value;
        } else {
            em.add(String.format("%sが適切な範囲内でありません。", name));
            return null;
        }
    }
}
