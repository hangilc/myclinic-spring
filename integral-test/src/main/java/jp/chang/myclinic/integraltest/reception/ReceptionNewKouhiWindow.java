package jp.chang.myclinic.integraltest.reception;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReceptionNewKouhiWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewKouhiWindow.class);
    private int patientId;

    ReceptionNewKouhiWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType,
                            int patientId) {
        super(receptionStub, windowType);
        this.patientId = patientId;
    }

    public void setInputs(KouhiInputs inputs){
        SetNewKouhiWindowInputsRequest kouhiReq = SetNewKouhiWindowInputsRequest.newBuilder()
                .setWindow(windowType)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewKouhiWindowInputs(kouhiReq).getValue();
        if( !ok ){
            throw new RuntimeException("set kouhi inputs failed");
        }
    }

    public KouhiDTO clickEnterButton(){
        int lastKouhiId = getLastKouhiId(patientId);
        boolean ok = receptionStub.clickNewKouhiWindowEnterButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Click enter failed in new kouhi window.");
        }
        return getCreatedKouhi(patientId, lastKouhiId);
    }

    private int getLastKouhiId(int patientId){
        List<KouhiDTO> hokenList = Service.api.listHoken(patientId).join().kouhiListDTO;
        return hokenList.stream().map(h -> h.kouhiId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private KouhiDTO getCreatedKouhi(int patientId, int lastKouhiId){
        return rpc(5, () -> {
            List<KouhiDTO> list = Service.api.listHoken(patientId).join().kouhiListDTO
                    .stream().filter(dto -> dto.kouhiId > lastKouhiId)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many kouhi created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }

}
