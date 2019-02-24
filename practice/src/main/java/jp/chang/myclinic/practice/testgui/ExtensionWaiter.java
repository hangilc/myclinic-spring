package jp.chang.myclinic.practice.testgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ExtensionWaiter<T> implements TestHelper {

    private int origSize;
    private Supplier<List<T>> supplier;

    public ExtensionWaiter(Supplier<List<T>> supplier) {
        this.supplier = supplier;
        this.origSize = supplier.get().size();
    }

    public T waitForExtension(int nTry){
        return waitFor(nTry, () -> {
            List<T> curr = supplier.get();
            if( curr.size() > origSize ){
                return Optional.of(curr.get(curr.size() - 1));
            } else {
                return Optional.empty();
            }
        });
    }

}
