package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCheckHandanryou extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckHandanryou.class);

    @Test
    public void duplicates(){
        List<Integer> handanryouCodess = new ArrayList<>();
        handanryouCodess.add(shinryouMap.尿便検査判断料);
        handanryouCodess.add(shinryouMap.血液検査判断料);
        handanryouCodess.add(shinryouMap.生化Ⅰ判断料);
        handanryouCodess.add(shinryouMap.生化Ⅱ判断料);
        handanryouCodess.add(shinryouMap.免疫検査判断料);
        handanryouCodess.add(shinryouMap.微生物検査判断料);
        handanryouCodess.forEach(this::testDuplicates);
    }

    private void testDuplicates(int shinryoucode){
        doBaseBefore();
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryoucode);
        int shinryouId = clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        new CheckHandanryou(scope).check();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId);
    }

}
