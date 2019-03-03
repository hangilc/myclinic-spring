package jp.chang.myclinic.clientmock.dbgateway;

import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServerProc {

    private DbGatewayMock dbGateway;

    public ServerProc(DbGatewayMock dbGateway){
        this.dbGateway = dbGateway;
    }

    public int startVisit(int patientId, LocalDateTime at){
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = dbGateway.findAvailableShahokokuho(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = dbGateway.findAvailableKoukikourei(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = dbGateway.findAvailableRoujin(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.roujinId = 0;
            } else {
                visitDTO.roujinId = list.get(0).roujinId;
            }
        }
        {
            visitDTO.kouhi1Id = 0;
            visitDTO.kouhi2Id = 0;
            visitDTO.kouhi3Id = 0;
            List<KouhiDTO> list = dbGateway.findAvailableKouhi(patientId, atDate);
            int n = list.size();
            if (n > 0) {
                visitDTO.kouhi1Id = list.get(0).kouhiId;
                if (n > 1) {
                    visitDTO.kouhi2Id = list.get(1).kouhiId;
                    if (n > 2) {
                        visitDTO.kouhi3Id = list.get(2).kouhiId;
                    }
                }
            }
        }
        int visitId = dbGateway.enterVisit(visitDTO);
        visitDTO.visitId = visitId;
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        dbGateway.enterWqueue(wqueueDTO);
        return visitId;
    }

    public HokenDTO getHokenForVisit(VisitDTO visitDTO) {
        HokenDTO hokenDTO = new HokenDTO();
        if (visitDTO.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = dbGateway.getShahokokuho(visitDTO.shahokokuhoId);
        }
        if (visitDTO.koukikoureiId > 0) {
            hokenDTO.koukikourei = dbGateway.getKoukikourei(visitDTO.koukikoureiId);
        }
        if (visitDTO.roujinId > 0) {
            hokenDTO.roujin = dbGateway.getRoujin(visitDTO.roujinId);
        }
        if (visitDTO.kouhi1Id > 0) {
            hokenDTO.kouhi1 = dbGateway.getKouhi(visitDTO.kouhi1Id);
        }
        if (visitDTO.kouhi2Id > 0) {
            hokenDTO.kouhi2 = dbGateway.getKouhi(visitDTO.kouhi2Id);
        }
        if (visitDTO.kouhi3Id > 0) {
            hokenDTO.kouhi3 = dbGateway.getKouhi(visitDTO.kouhi3Id);
        }
        return hokenDTO;
    }

}
