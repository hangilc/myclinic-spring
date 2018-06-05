package jp.chang.myclinic.recordbrowser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Repeater implements Runnable {
    private static final int CMD_TRIGGER = 0;
    private static final int CMD_SKIP = -1;

    private static Logger logger = LoggerFactory.getLogger(Repeater.class);
    private int interval; // seconds
    private BlockingQueue<Integer> triggerQueue = new ArrayBlockingQueue<>(1);
    private Runnable callback;

    public Repeater(int seconds, Runnable callback) {
        this.interval = seconds;
        this.callback = callback;
        triggerQueue.add(CMD_TRIGGER);
    }


    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while(true){
            try {
                Integer cmd = triggerQueue.poll(interval, TimeUnit.SECONDS);
                if( cmd == null || cmd == CMD_TRIGGER ){
                    callback.run();
                } else if( cmd > 0 ){
                    this.interval = cmd;
                } else {
                    logger.error("Unknown command {}.", cmd);
                }
            } catch (InterruptedException e) {
                logger.info("Interrupted");
            }

        }
    }

    public boolean trigger(){
        return triggerQueue.offer(CMD_TRIGGER);
    }

    public boolean skip(){
        return triggerQueue.offer(CMD_SKIP);
    }

    public boolean setInterval(int seconds){
        if( seconds > 0 ){
            return triggerQueue.offer(seconds);
        } else {
            logger.warn("Negative interval {}.", seconds);
            return false;
        }
    }

}
