package jp.chang.myclinic.practice.selftest;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.PracticeFun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PracticeSelfTest {

    //private static Logger logger = LoggerFactory.getLogger(PracticeSelfTest.class);
    private MockData mocker = new MockData();

    public void run(){
        confirmMockPatient();
        testListWqueue();
    }

    private void confirmMockPatient() {
        Service.api.getPatient(1)
                .thenAccept(patient -> {
                    if (!("試験".equals(patient.lastName) && "データ".equals(patient.firstName))) {
                        System.err.println("Invalid mock patient.");
                        System.exit(3);
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.err.println("Cannot find mock patient.");
                    System.exit(2);
                    return null;
                });
    }

    private void testListWqueue(){
        PatientDTO patient = enterMockPatient();
        VisitDTO visit = startVisit(patient.patientId);
        List<WqueueFullDTO> list = PracticeFun.listWqueue().join();
        WqueueFullDTO wqueue = list.get(list.size()-1);
        assertTrue(wqueue.visit.visitId == visit.visitId);
    }

    private void assertTrue(boolean value){
        if( !value ){
            throw new RuntimeException("assertion failed");
        }
    }

    private PatientDTO enterMockPatient(){
        PatientDTO patient = mocker.mockPatient();
        patient.patientId = Service.api.enterPatient(patient).join();
        return patient;
    }

    private VisitDTO startVisit(int patientId){
        int visitId = Service.api.startVisit(patientId).join();
        return Service.api.getVisit(visitId).join();
    }

}
