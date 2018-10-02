package jp.chang.myclinic.util.logic;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLogic {


    @Test
    public void testLogic(){
        class IntegerLogic implements Logic<Integer> {

            @Override
            public Integer getValue(Consumer<String> errorHandler) {
                return 10;
            }

            @Override
            public String setValue(Integer value) {
                return null;
            }
        }
        class Result {
            private Integer value;
            private String error;
        }
        Result result = new Result();
        IntegerLogic logic = new IntegerLogic();
        boolean ver = logic.verify(
                value -> result.value = value,
                error -> result.error = error
        );
        assertTrue(ver);
        assertEquals(10, (int) result.value);
    }
}
