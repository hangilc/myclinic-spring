package jp.chang.myclinic.practice.testgui;

import java.util.Optional;
import java.util.function.Supplier;

public class IncrementWaiter implements TestHelper {

    private int lastValue;
    private Supplier<Integer> counter;
    private int requiredIncrement = 1;

    public IncrementWaiter(Supplier<Integer> counter) {
        this.lastValue = counter.get();
        this.counter = counter;
    }

    public IncrementWaiter setRequiredIncrement(int req){
        this.requiredIncrement = req;
        return this;
    }

    public int waitForIncrement(){
        return waitForIncrement(5);
    }

    public int waitForIncrement(int nTry){
        return waitFor(nTry, () -> {
            int i = counter.get();
            if( i >= lastValue + requiredIncrement ){
                return Optional.of(i);
            } else {
                return Optional.empty();
            }
        });
    }

}
