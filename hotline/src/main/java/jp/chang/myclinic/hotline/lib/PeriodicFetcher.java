package jp.chang.myclinic.hotline.lib;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PeriodicFetcher implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(PeriodicFetcher.class);
    private Consumer<List<HotlineDTO>> consumer;
    private Consumer<String> errorConsumer;
    private int lastHotlineId;
    private BlockingQueue<Integer> triggerQueue = new ArrayBlockingQueue<>(1);

    public PeriodicFetcher(Consumer<List<HotlineDTO>> consumer, Consumer<String> errorConsumer){
        this.consumer = consumer;
        this.errorConsumer = errorConsumer;
    }

    public void trigger(){
        triggerQueue.offer(0);
    }

    @Override
    public void run() {
        while(true){
            try {
                triggerQueue.poll(lastHotlineId <= 0 ? 0 : 2000, TimeUnit.MILLISECONDS);
                Response<List<HotlineDTO>> response = reload();
                if (response.isSuccessful()) {
                    List<HotlineDTO> hotlines = response.body();
                    if (hotlines.size() > 0) {
                        consumer.accept(hotlines);
                        lastHotlineId = hotlines.get(hotlines.size() - 1).hotlineId;
                    }
                }
            } catch (InterruptedException ex){
                return;
            } catch (Exception e) {
                String message = "サーバーからの読み込みに失敗しました。";
                logger.error(message, e);
                errorConsumer.accept(message);
            }
        }
    }

    private Response<List<HotlineDTO>> reload() throws IOException {
        return (lastHotlineId <= 0 ? Service.api.listTodaysHotlineSync() : Service.api.listRecentHotlineSync(lastHotlineId)).execute();
    }

}
