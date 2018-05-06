package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;
import java.util.function.Consumer;

public class PatientBuilder {

    //private static Logger logger = LoggerFactory.getLogger(PatientBuilder.class);
    private PatientDTO result;

    public PatientBuilder() {
        result = new PatientDTO();
        result.patientId = G.genid();
        result.lastName = G.gensym();
        result.firstName = G.gensym();
        result.lastNameYomi = G.gensym();
        result.firstNameYomi = G.gensym();
        result.birthday = LocalDate.of(1970, 2, 12).toString();
        result.sex = Sex.Female.getCode();
        result.address = G.gensym();
        result.phone = G.gensym();
    }

    public PatientDTO build(){
        return result;
    }

    public PatientBuilder modify(Consumer<PatientDTO> cb){
        cb.accept(result);
        return this;
    }

}
