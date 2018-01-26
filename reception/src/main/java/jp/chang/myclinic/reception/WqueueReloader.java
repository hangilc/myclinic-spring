package jp.chang.myclinic.reception;

import jp.chang.myclinic.dto.WqueueFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WqueueReloader implements Runnable {

    public interface Callback {
        void onLoad(List<WqueueFullDTO> list);
        void onError(Throwable ex);
    }

    private static Logger logger = LoggerFactory.getLogger(WqueueReloader.class);
    private int msecInterval;
    private BlockingQueue<Integer> triggerQueue = new ArrayBlockingQueue<>(1);
    private Callback callback = new Callback(){
        public void onLoad(List<WqueueFullDTO> list){}
        public void onError(Throwable ex){}
    };

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void trigger(){
        triggerQueue.offer(0);
    }

    public WqueueReloader(int msecInterval){
        this.msecInterval = msecInterval;
    }

    @Override
    public void run() {
        while(true){
            try {
                triggerQueue.poll(msecInterval, TimeUnit.MILLISECONDS);
                reload();
            } catch (Exception e) {
                logger.error("Failed to load wqueue list.", e);
                callback.onError(e);
            }
        }
    }

    private void reload(){
        Service.api.listWqueue()
                .thenAccept(callback::onLoad)
                .exceptionally(t -> {
                    logger.error("Failed to load wqueue list.", t);
                    callback.onError(t);
                    return null;
                });
    }

}
