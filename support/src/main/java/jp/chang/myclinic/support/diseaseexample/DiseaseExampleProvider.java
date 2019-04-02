package jp.chang.myclinic.support.diseaseexample;

import jp.chang.myclinic.dto.DiseaseExampleDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiseaseExampleProvider {
    List<DiseaseExampleDTO> listDiseaseExample();
}
