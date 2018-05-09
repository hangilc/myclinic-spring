package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class TestCheckShoshinByoumei extends Base {

    @Test
    public void noDisease(){
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.初診);
        scope.visits = clinic.getVisits();
        new CheckShoshinByoumei(scope).check();
        assertEquals(1, nerror);
    }

    @Test
    public void continueingDisease(){
        Clinic clinic = new Clinic();
        LocalDate baseDate = clinic.getBaseDate();
        LocalDate visitedAt = baseDate.plusMonths(3);
        clinic.startVisit(modifier -> modifier.setVisitedAt(visitedAt));
        clinic.addDisease(modifier -> modifier.setStartDate(visitedAt.minusDays(10)));
        clinic.addShinryou(shinryouMap.初診);
        syncScope(clinic);
        new CheckShoshinByoumei(scope).check();
        assertEquals(2, nerror);  // continuing diseases, and no diseases at shoshin date
    }

    @Test
    public void ok(){
        Clinic clinic = new Clinic();
        LocalDate baseDate = clinic.getBaseDate();
        LocalDate visitedAt = baseDate.plusMonths(3);
        clinic.startVisit(modifier -> modifier.setVisitedAt(visitedAt));
        clinic.addDisease(modifier -> modifier.setStartDate(visitedAt));
        clinic.addShinryou(shinryouMap.初診);
        syncScope(clinic);
        new CheckShoshinByoumei(scope).check();
        assertEquals(0, nerror);
    }
}
