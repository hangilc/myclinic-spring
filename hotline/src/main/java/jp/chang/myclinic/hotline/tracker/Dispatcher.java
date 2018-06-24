package jp.chang.myclinic.hotline.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Service;
import jp.chang.myclinic.logdto.hotline.HotlineBeep;
import jp.chang.myclinic.logdto.hotline.HotlineCreated;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
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
    private BlockingQueue<HotlineLogDTO> pendings = new LinkedBlockingQueue<>();
    private DispatchAction action;
    private Service.ServerAPI service;
    private Runnable toNext;
    private int lastId;

    Dispatcher(DispatchAction action, Service.ServerAPI service) {
        this.action = action;
        this.service = service;
        toNext = () -> taskPermit.release();
    }

    public void add(HotlineLogDTO log) {
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
                HotlineLogDTO log = pendings.take();
                try {
                    if ("hotline-beep".equals(log.kind)) {
                        HotlineBeep body = mapper.readValue(log.body, HotlineBeep.class);
                        action.onHotlineBeep(body.receiver, toNext);
                        taskPermit.acquire();
                    } else if ("hotline-created".equals(log.kind)) {
                        HotlineCreated body = mapper.readValue(log.body, HotlineCreated.class);
                        int logSerialId = body.created.hotlineId;
                        if (logSerialId == (lastId + 1)) {
                            Platform.runLater(() -> action.onHotlineCreated(body.created, toNext));
                            taskPermit.acquire();
                            this.lastId = logSerialId;
                        } else if (logSerialId > (lastId + 1)) {
                            catchUp(lastId, logSerialId, body.created);
                        }
                    } else {
                        logger.error("Unknown hotline log: {}", log);
                    }
                } catch (Exception ex) {
                    logger.error("Failed to handle hotline log. {}", log);
                }
            }
        } catch (InterruptedException ignored) {

        }
    }

    protected void beforeCatchup() {

    }

    protected void afterCatchup() {

    }

    private void catchUp(int lastId, int nextId, HotlineDTO nextLog) throws InterruptedException {
        boolean needCallback = false;
        try {
            final List<HotlineDTO> logs =
                    service.listTodaysHotlineInRangeCall(lastId, nextId).execute().body();
            logs.add(nextLog);
            if (logs.size() > 6) {
                beforeCatchup();
                needCallback = true;
            }
            class Local {
                private int i = 0;
            }
            Local local = new Local();
            while (local.i < logs.size()) {
                HotlineDTO currentLog = logs.get(local.i);
                Platform.runLater(() -> action.onHotlineCreated(currentLog, toNext));
                taskPermit.acquire();
                this.lastId = currentLog.hotlineId;
                local.i += 1;
            }
            if (needCallback) {
                afterCatchup();
            }
        } catch (IOException e) {
            if (needCallback) {
                afterCatchup();
            }
            logger.error("Client::listPracticeLogInRange failed.", e);
        }
    }

}

