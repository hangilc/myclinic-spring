package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface ReferService {

    List<ReferItemDTO> listRefer();

}
