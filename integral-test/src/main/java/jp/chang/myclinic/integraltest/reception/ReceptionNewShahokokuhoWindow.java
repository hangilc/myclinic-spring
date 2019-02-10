package jp.chang.myclinic.integraltest.reception;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.integraltest.GrpcBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReceptionNewShahokokuhoWindow extends ReceptionWindow {

    //private static Logger logger = LoggerFactory.getLogger(ReceptionNewShahokokuhoWindow.class);
    private int patientId;

    ReceptionNewShahokokuhoWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType,
                                  int patientId){
        super(receptionStub, windowType);
        this.patientId = patientId;
    }

    public void setInputs(ShahokokuhoInputs inputs){
        SetNewShahokokuhoWindowInputsRequest req = SetNewShahokokuhoWindowInputsRequest.newBuilder()
                .setWindow(windowType)
                .setInputs(inputs)
                .build();
        boolean ok = receptionStub.setNewShahokokuhoWindowInputs(req).getValue();
        if (!ok) {
            throw new RuntimeException("set shahokokuho inputs failed");
        }
    }

    public ShahokokuhoDTO clickEnterButton(){
        int lastShahokokuhoId = getLastShahokokuhoId(patientId);
        boolean ok = receptionStub.clickNewShahokokuhoWindowEnterButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking enter button failed (new shahokokuho window).");
        }
        return getCreatedShahokokuho(patientId, lastShahokokuhoId);
    }

    private int getLastShahokokuhoId(int patientId){
        List<ShahokokuhoDTO> hokenList = Service.api.listHoken(patientId).join().shahokokuhoListDTO;
        return hokenList.stream().map(h -> h.shahokokuhoId).max(Comparator.naturalOrder())
                .orElse(0);
    }

    private ShahokokuhoDTO getCreatedShahokokuho(int patientId, int lastShahokokuhoId){
        return rpc(5, () -> {
            List<ShahokokuhoDTO> list = Service.api.listHoken(patientId).join().shahokokuhoListDTO
                    .stream().filter(dto -> dto.shahokokuhoId > lastShahokokuhoId)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many shahokokuho created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }


}
