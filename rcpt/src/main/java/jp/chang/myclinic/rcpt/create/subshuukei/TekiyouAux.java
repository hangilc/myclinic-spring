package jp.chang.myclinic.rcpt.create.subshuukei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TekiyouAux implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouAux.class);

    private double leftMargin = 8;
    private String text;

    TekiyouAux(String text) {
        this.text = text;
    }

    @Override
    public void output(String shuukei) {
        System.out.printf("tekiyou_aux {%s} %s\n", composeOption(), text);
    }

    private String composeOption(){
        Map<String, String> map = new HashMap<>();
        if( leftMargin != 0.0 ){
            map.put("left-margin", Double.toString(leftMargin));
        }
        List<String> items = new ArrayList<>();
        map.forEach((key, value) -> {
            String s = String.format("%s:%s", key, value);
            items.add(s);
        });
        return String.join(",", items);
    }

}
