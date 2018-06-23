package jp.chang.myclinic.hotline.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

class Dispatcher implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Semaphore taskPermit = new Semaphore(1);
    private BlockingQueue<HotlineDTO> pendings = new LinkedBlockingQueue<>();
    private DispatchAction action;
    private Service.ServerAPI service;
    private Runnable toNext;
    private int lastId;

    Dispatcher(DispatchAction action, Service.ServerAPI service) {
        this.action = action;
        this.service = service;
        toNext = () -> taskPermit.release();
    }

    public void add(HotlineDTO log) {
        try {
            pendings.put(log);
        } catch (InterruptedException e) {
            logger.error("Failed to add practice log.", e);
        }
    }

    @Override
    public void run() {
        try {
            taskPermit.acquire();
            //noinspection InfiniteLoopStatement
            while (true) {
                HotlineDTO log = pendings.take();
                int logSerialId = log.hotlineId;
                if (logSerialId == (lastId + 1)) {
                    Platform.runLater(() -> dispatchOne(log, false));
                    taskPermit.acquire();
                    this.lastId = logSerialId;
                } else if ( logSerialId > (lastId + 1)) {
                    catchUp(lastId, logSerialId, log);
                }
            }
        } catch (InterruptedException ignored) {

        }
    }

    protected void beforeCatchup(){

    }

    protected void afterCatchup(){

    }

    private void catchUp(int lastId, int nextId, HotlineDTO nextLog) throws InterruptedException {
        boolean initialSetup = lastId == 0;
        boolean needCallback = false;
        try {
            final List<HotlineDTO> logs =
                    service.listTodaysHotlineInRangeCall(lastId, nextId).execute().body();
            logs.add(nextLog);
            if( logs.size() > 6 ){
                beforeCatchup();
                needCallback = true;
            }
            class Local {
                private int i = 0;
            }
            Local local = new Local();
            while( local.i < logs.size() ){
                HotlineDTO currentLog = logs.get(local.i);
                Platform.runLater(() -> dispatchOne(currentLog, initialSetup));
                taskPermit.acquire();
                this.lastId = currentLog.hotlineId;
                local.i += 1;
            }
            if( needCallback ){
                afterCatchup();
            }
        } catch (IOException e) {
            if( needCallback ){
                afterCatchup();
            }
            logger.error("Client::listPracticeLogInRange failed.", e);
        }
    }

    private void dispatchOne(HotlineDTO log, boolean initialSetup) {
        action.onHotlineCreated(log, initialSetup, toNext);
    }

}

