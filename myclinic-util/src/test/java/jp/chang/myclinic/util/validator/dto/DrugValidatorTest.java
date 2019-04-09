package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.util.validator.Validated;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DrugValidatorTest {

    @Test
    public void validTest(){
        DrugValidator validator = new DrugValidator();
        validator.validateDrugId(123);
        validator.validateVisitId(11011);
        validator.validateIyakuhincode(1234567);
        validator.validateAmount(3.0);
        validator.validateUsage("分３　毎食後");
        validator.validateCategory(DrugCategory.Naifuku.getCode());
        validator.validateDays("5");
        validator.validatePrescribed(0);
        Validated<DrugDTO> validatedDrug = validator.validate();
        assertTrue(validatedDrug.isSuccess());
    }

    @Test
    public void failTest(){
        DrugValidator validator = new DrugValidator();
        validator.validateDrugId(0);
        validator.validateVisitId(0);
        validator.validateIyakuhincode(0);
        validator.validateAmount(-3.0);
        validator.validateUsage("");
        validator.validateCategory(-1);
        validator.validateDays("");
        validator.validatePrescribed(5);
        Validated<DrugDTO> validatedDrug = validator.validate();
        assertTrue(validatedDrug.isFailure());
        assertEquals(8, validatedDrug.getErrors().size());
    }
}
