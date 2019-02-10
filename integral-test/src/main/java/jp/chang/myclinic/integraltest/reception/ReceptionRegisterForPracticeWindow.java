package jp.chang.myclinic.integraltest.reception;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc.*;
import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtGrpc;
import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ReceptionRegisterForPracticeWindow extends ReceptionWindow {

    private static Logger logger = LoggerFactory.getLogger(ReceptionRegisterForPracticeWindow.class);


    ReceptionRegisterForPracticeWindow(ReceptionMgmtBlockingStub receptionStub, WindowType windowType) {
        super(receptionStub, windowType);
    }

    public VisitDTO clickOkButton(){
        int lastVisitId = getLastVisitId();
        boolean ok = receptionStub.clickRegisterForPracticeWindowOkButton(windowType).getValue();
        if( !ok ){
            throw new RuntimeException("Clicking ok button failed (register for practice window).");
        }
        return getCreatedVisit(lastVisitId);
    }

    private int getLastVisitId(){
        List<VisitPatientDTO> list = Service.api.listRecentVisits(0, 1).join();
        return list.size() == 0 ? 0 : list.get(0).visit.visitId;
    }

    private VisitDTO getCreatedVisit(int lastVisitId){
        return rpc(5, () -> {
            List<VisitDTO> list = Service.api.listRecentVisits(0, 1).join()
                    .stream().filter(dto -> dto.visit.visitId > lastVisitId )
                    .map(dto -> dto.visit)
                    .collect(toList());
            if( list.size() > 1 ){
                throw new RuntimeException("Too many visit created.");
            } else if( list.size() == 1 ){
                return Optional.of(list.get(0));
            } else {
                return Optional.empty();
            }
        });
    }

}
