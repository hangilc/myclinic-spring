package jp.chang.myclinic.integraltest;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

public class GrpcBase {

    //private static Logger logger = LoggerFactory.getLogger(GrpcBase.class);

    public static <T> T rpc(int maxTry, Supplier<Optional<T>> fun) {
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

    public static WindowType findCreatedWindow(Function<VoidType, WindowType> f) {
        return rpc(5, () -> {
            WindowType w = f.apply(null);
            return (w != null && w.getWindowId() > 0) ? Optional.of(w) : Optional.empty();
        });
    }


}
