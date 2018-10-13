package jp.chang.myclinic.util.logic;

import java.time.LocalDate;

public class BiValidators {

    private BiValidators() {

    }

    public static BiValidator<String> notBothAreEmpty(){
        return (a, b, leftName, rightName, em) -> {
            if( a.isEmpty() && b.isEmpty() ){
                em.add(String.format("%sと%sが両方とも空白です。", leftName, rightName));
            }
        };
    }

    public static BiValidator<LocalDate> validInterval(){
        return (a, b, leftName, rightName, em) -> {
            if( b != null ){
                if( a.isAfter(b) ){
                    em.add(String.format("%sが%sより前の値です。", rightName, leftName));
                }
            }
        };
    }

}
