package jp.chang.myclinic.practice.testintegration;

import java.util.Optional;
import java.util.function.Supplier;

class IncrementWaiter extends IntegrationTestBase {

    private int lastValue;
    private Supplier<Integer> counter;

    IncrementWaiter(Supplier<Integer> counter) {
        this.lastValue = counter.get();
        this.counter = counter;
    }

    int waitForIncrement(int nTry){
        return waitFor(nTry, () -> {
            int i = counter.get();
            if( i > lastValue ){
                return Optional.of(i);
            } else {
                return Optional.empty();
            }
        });
    }

}
