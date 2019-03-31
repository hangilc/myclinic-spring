package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.DiseaseExampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface DiseaseExampleService {

    List<DiseaseExampleDTO> listDiseaseExamples();

}
