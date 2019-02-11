package jp.chang.myclinic.integraltest;

import com.google.protobuf.Empty;
import jp.chang.myclinic.Common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class GrpcHelper {

    private static Logger logger = LoggerFactory.getLogger(GrpcHelper.class);

    public GrpcHelper() {

    }

    public <T> T rpc(int maxTry, Supplier<Optional<T>> fun) {
        try {
            while (maxTry-- > 0) {
                Optional<T> opt = fun.get();
                if (opt.isPresent()) {
                    return opt.get();
                }
                Thread.sleep(500);
            }
            throw new RuntimeException("rpc failed");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("rpc failed");
        }
    }

    public WindowType findCreatedWindow(Function<Empty, WindowType> f) {
        return rpc(5, () -> {
            WindowType w = f.apply(null);
            return (w != null && w.getWindowId() > 0) ? Optional.of(w) : Optional.empty();
        });
    }

}
