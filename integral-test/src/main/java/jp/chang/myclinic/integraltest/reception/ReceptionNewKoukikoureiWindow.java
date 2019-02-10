package jp.chang.myclinic.integraltest.reception;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReceptionNewKoukikoureiWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewKoukikoureiWindow.class);
    int patientId;

    ReceptionNewKoukikoureiWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType,
                                  int patientId) {
        super(receptionStub, windowType);
        this.patientId = patientId;
    }

    public void setInputs(KoukikoureiInputs inputs){
        SetNewKoukikoureiWindowInputsRequest req = SetNewKoukikoureiWindowInputsRequest.newBuilder()
                .setWindow(windowType)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewKoukikoureiWindowInputs(req).getValue();
        if( !ok ){
            throw new RuntimeException("Setting inputs failed (new koukikourei window).");
        }
    }

    public KoukikoureiDTO clickEnterButton(){
        int lastKoukikoureiId = getLastKoukikoureiId(patientId);
        boolean ok = receptionStub.clickNewKoukikoureiWindowEnterButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking enter button failed (new koukikourei window).");
        }
        return getCreatedKoukikourei(patientId, lastKoukikoureiId);
    }

    private int getLastKoukikoureiId(int patientId){
        List<KoukikoureiDTO> hokenList = Service.api.listHoken(patientId).join().koukikoureiListDTO;
        return hokenList.stream().map(h -> h.koukikoureiId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private KoukikoureiDTO getCreatedKoukikourei(int patientId, int lastKoukikoureiId){
        return rpc(5, () -> {
            List<KoukikoureiDTO> list = Service.api.listHoken(patientId).join().koukikoureiListDTO
                    .stream().filter(dto -> dto.koukikoureiId > lastKoukikoureiId)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many koukikourei created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }

}
