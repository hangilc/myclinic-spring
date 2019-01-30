package jp.chang.myclinic.reception.grpc;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleData {

    private static Logger logger = LoggerFactory.getLogger(SampleData.class);

    public static PatientInputs patientInputs1 = PatientInputs.newBuilder()
            .setLastName("足立")
            .setFirstName("愛子")
            .setLastNameYomi("あだち")
            .setFirstNameYomi("あいこ")
            .setBirthdayGengou("昭和")
            .setBirthdayNen("57")
            .setBirthdayMonth("9")
            .setBirthdayDay("12")
            .setSex("F")
            .setAddress("杉並区参考地1-23-4")
            .setPhone("03-1234-5678")
            .build();

}
