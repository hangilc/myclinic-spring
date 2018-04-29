package jp.chang.myclinic.utilfx;

import java.util.function.BiConsumer;

public interface NavHandler {
    void trigger(int page, BiConsumer<Integer, Integer> cb);
}
