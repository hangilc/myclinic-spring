package jp.chang.myclinic.util.value;

public class IntegerProvider implements ValueProvider<Integer> {

    private Integer value;

    public IntegerProvider(Integer value) {
        this.value = value;
    }

    public Integer get(){
        return value;
    }

}
