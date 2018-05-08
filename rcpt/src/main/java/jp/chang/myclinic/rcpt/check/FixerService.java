package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.dto.ShinryouDTO;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

class FixerService implements Fixer {

    //private static Logger logger = LoggerFactory.getLogger(FixerService.class);
    private Service.ServerAPI api;

    FixerService(Service.ServerAPI api) {
        this.api = api;
    }

    @Override
    public int enterShinryou(ShinryouDTO shinryou) {
        try {
            return api.enterShinryouCall(shinryou).execute().body();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean batchDeleteShinryou(List<Integer> shinryouIds) {
        try {
            return api.batchDeleteShinryouCall(shinryouIds).execute().body();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int enterDisease(DiseaseNewDTO disease) {
        try {
            return api.enterDiseaseCall(disease).execute().body();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
