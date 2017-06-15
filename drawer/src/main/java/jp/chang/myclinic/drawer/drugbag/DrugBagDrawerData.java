package jp.chang.myclinic.drawer.drugbag;

import jp.chang.myclinic.drawer.DrawerColor;

import java.util.Collections;
import java.util.List;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrugBagDrawerData {
    public DrawerColor color;
    public String title;
    public String patientName = "　　　 　　　";
    public String patientNameYomi = "　　　 　　　　";
    public List<String> instructions = Collections.emptyList();
    public String drugName = "";
    public String drugDescription = "";
}
