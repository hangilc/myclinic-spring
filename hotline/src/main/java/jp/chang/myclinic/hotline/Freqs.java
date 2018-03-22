package jp.chang.myclinic.hotline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Freqs {

    private static Logger logger = LoggerFactory.getLogger(Freqs.class);

    public static Freqs INSTANCE = new Freqs();

    private Freqs() {

    }

    private List<String> practice = Arrays.asList(
            "おはようございます。",
            "診察室におねがいします。",
            "処方箋おねがいします。",
            " 様、伝。"
    );

    private List<String> reception = Arrays.asList(
            "おはようございます。",
            "退出します。",
            "戻りました。",
            "検温中です。",
            "体温 {} 度でした。",
            "胃腸の調子が悪いそうです。",
            "相談です。",
            "セットできました。",
            "お薬手帳忘れです。",
            "面会の方いらしてます。"
    );

    private List<String> pharma = Arrays.asList(
            "おはようございます。",
            "退出します。",
            "戻りました。"
    );

    public List<String> listFor(String user){
        user = user.toLowerCase();
        switch(user){
            case "practice": return practice;
            case "reception": return reception;
            case "pharma": return pharma;
            default: return Collections.emptyList();
        }
    }

}
