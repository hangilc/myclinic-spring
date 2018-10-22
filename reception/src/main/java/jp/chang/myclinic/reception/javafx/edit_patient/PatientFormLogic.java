package jp.chang.myclinic.reception.javafx.edit_patient;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.dto_logic.SexLogic;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

class PatientFormLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(PatientFormLogic.class);

    private PatientFormLogic() {

    }

    static PatientDTO patientFormInputsToPatientDTO(PatientFormInputs inputs, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        PatientDTO dto = new PatientDTO();

        dto.lastName = new LogicValue<>(inputs.lastNameInput)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .getValue(nameWith(name, "の") + "姓", em);

        dto.firstName = new LogicValue<>(inputs.firstNameInput)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .getValue(nameWith(name, "の") + "名", em);

        dto.lastNameYomi = new LogicValue<>(inputs.lastNameYomiInput)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .getValue(nameWith(name, "の") + "姓のよみ", em);

        dto.firstNameYomi = new LogicValue<>(inputs.firstNameYomiInput)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .getValue(nameWith(name, "の") + "名のよみ", em);

        dto.birthday = new LogicValue<>(inputs.birthdayInputs)
                .validate(Validators::isNotNull)
                .validate(DateFormLogic::isNotEmptyDateFormInputs)
                .convert(DateFormLogic::dateFormInputsToLocalDate)
                .map(Mappers::localDateToSqldate)
                .getValue(nameWith(name, "の") + "生年月日", em);

        dto.sex = new LogicValue<>(inputs.sexInput)
                .validate(Validators::isNotNull)
                .map(Sex::getCode)
                .getValue(nameWith(name, "の") + "姓", em);

        dto.address = new LogicValue<>(inputs.addressInput)
                .map(Mappers::nullToEmpty)
                .getValue(nameWith(name, "の") + "住所", em);

        dto.phone = new LogicValue<>(inputs.phoneInput)
                .map(Mappers::nullToEmpty)
                .getValue(nameWith(name, "の") + "電話", em);

        return em.hasErrorSince(ne) ? null : dto;
    }

    static PatientFormInputs patientDTOToPatientFormInputs(PatientDTO dto, String origName, ErrorMessages em){
        String name = nameWith(origName, "の");
        PatientFormInputs inputs = new PatientFormInputs();
        inputs.lastNameInput = dto.lastName;
        inputs.firstNameInput = dto.firstName;
        inputs.lastNameYomiInput = dto.lastNameYomi;
        inputs.firstNameYomiInput = dto.firstNameYomi;
        inputs.birthdayInputs = new LogicValue<>(dto.birthday)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::sqldateToLocalDate)
                .convert(DateFormLogic::localDateToDateFormInputs)
                .getValue("生年月日", em);
        inputs.sexInput = new LogicValue<>(dto.sex)
                .convert(SexLogic::codeToSex)
                .getValue(null, em);
        inputs.addressInput = dto.address;
        inputs.phoneInput = dto.phone;
        return inputs; // return inputs anyway
    }

}
