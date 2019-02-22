package jp.chang.myclinic.practice.testintegration;

import java.util.Optional;
import java.util.function.Supplier;

class IncrementWaiter extends IntegrationTestBase {

    private int lastValue;
    private Supplier<Integer> counter;
    private int requiredIncrement = 1;

    IncrementWaiter(Supplier<Integer> counter) {
        this.lastValue = counter.get();
        this.counter = counter;
    }

    IncrementWaiter setRequiredIncrement(int req){
        this.requiredIncrement = req;
        return this;
    }

    int waitForIncrement(){
        return waitForIncrement(5);
    }

    int waitForIncrement(int nTry){
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
