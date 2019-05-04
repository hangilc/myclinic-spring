package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

/**
 * Created by hangil on 2017/06/11.
 */
public class PharmaQueueDTO {
    @Primary
    public int visitId;
    public int pharmaState;

    public static PharmaQueueDTO copy(PharmaQueueDTO src){
        PharmaQueueDTO dst = new PharmaQueueDTO();
        dst.visitId = src.visitId;
        dst.pharmaState = src.pharmaState;
        return dst;
    }

    @Override
    public String toString() {
        return "PharmaQueueDTO{" +
                "visitId=" + visitId +
                ", pharmaState=" + pharmaState +
                '}';
    }
}
