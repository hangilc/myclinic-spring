package jp.chang.myclinic.hotline.lib;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.client.Service;
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
    public final static Integer CMD_FETCH = 0;
    public final static Integer CMD_RESET = 1;

    private static Logger logger = LoggerFactory.getLogger(PeriodicFetcher.class);
    private PeriodicFetcherCallback consumer;
    private Consumer<String> errorConsumer;
    private int lastHotlineId;
    private BlockingQueue<Integer> triggerQueue = new ArrayBlockingQueue<>(1);

    public PeriodicFetcher(PeriodicFetcherCallback consumer, Consumer<String> errorConsumer){
        this.consumer = consumer;
        this.errorConsumer = errorConsumer;
    }

    public void trigger(Integer cmd){
        triggerQueue.offer(cmd);
    }

    @Override
    public void run() {
        logger.info("PeriodicFetcher started.");
        //noinspection InfiniteLoopStatement
        while(true){
            try {
                logger.info("Start polling.");
                Integer cmd = triggerQueue.poll(lastHotlineId <= 0 ? 0 : 2000, TimeUnit.MILLISECONDS);
                logger.info("Returned from polling. {}", cmd);
                if( cmd == null || CMD_FETCH.equals(cmd) ){
                    doFetch();
                } else if( CMD_RESET.equals(cmd) ){
                    lastHotlineId = 0;
                    doFetch();
                }
            } catch (Exception e) {
                logger.error("Error occured in periodic fetcher loop.", e);
                errorConsumer.accept("エラーが発生しました。");
            }
        }
    }

    private void doFetch() throws IOException, InterruptedException {
        Response<List<HotlineDTO>> response = reload();
        if (response.isSuccessful()) {
            List<HotlineDTO> hotlines = response.body();
            consumer.onPosts(hotlines, lastHotlineId == 0);
            if (hotlines.size() > 0) {
                lastHotlineId = hotlines.get(hotlines.size() - 1).hotlineId;
            }
        } else {
            logger.info("Server response was unsuccessful.");
            if( lastHotlineId == 0 ){
                Thread.sleep(2000);
            }
        }
    }

    private Response<List<HotlineDTO>> reload() throws IOException {
        if( lastHotlineId <= 0 ){
            return Service.api.listTodaysHotlineSync().execute();
        } else {
            return Service.api.listRecentHotlineSync(lastHotlineId).execute();
        }
    }

}
