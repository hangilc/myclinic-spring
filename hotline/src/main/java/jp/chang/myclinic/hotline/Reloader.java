package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import retrofit2.Response;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class Reloader implements Runnable {

    private MainFrame mainFrame;
    private int lastHotlineId;
    private BlockingQueue<Integer> triggerQueue = new ArrayBlockingQueue<>(1);

    Reloader(MainFrame mainFrame){
        this.mainFrame = mainFrame;
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
                if( response.isSuccessful() ){
                    List<HotlineDTO> hotlines = response.body();
                    if( hotlines.size() > 0 ){
                        EventQueue.invokeAndWait(() -> {
                            mainFrame.onNewHotline(hotlines);
                            lastHotlineId = hotlines.get(hotlines.size()-1).hotlineId;
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: show error message in main frame
            }

        }
    }

    private Response<List<HotlineDTO>> reload() throws IOException {
        return (lastHotlineId <= 0 ? Service.api.listTodaysHotlineSync() : Service.api.listRecentHotlineSync(lastHotlineId)).execute();
    }

}
