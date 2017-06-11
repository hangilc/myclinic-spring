package jp.chang.myclinic.dto;

/**
 * Created by hangil on 2017/06/11.
 */
public class PharmaQueueDTO {
    public int visitId;
    public int pharmaState;

    @Override
    public String toString() {
        return "PharmaQueueDTO{" +
                "visitId=" + visitId +
                ", pharmaState=" + pharmaState +
                '}';
    }
}
