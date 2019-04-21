package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReferServiceData implements ReferService {

    private List<ReferItemDTO> refers;

    public ReferServiceData(List<ReferItemDTO> refers) {
        this.refers = refers;
    }

    @Override
    public List<ReferItemDTO> listRefer() {
        return refers;
    }
}
