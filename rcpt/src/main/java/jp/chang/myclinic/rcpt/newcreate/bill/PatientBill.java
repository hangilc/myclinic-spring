package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.rcpt.newcreate.input.Rcpt;
import jp.chang.myclinic.rcpt.newcreate.input.Seikyuu;
import jp.chang.myclinic.rcpt.newcreate.output.Output;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PatientBill {

    private static Logger logger = LoggerFactory.getLogger(PatientBill.class);
    private Rcpt rcpt;
    private Seikyuu seikyuu;

    public PatientBill(Rcpt rcpt, Seikyuu seikyuu) {
        this.rcpt = rcpt;
        this.seikyuu = seikyuu;
    }

    public void print(Output out) {
        out.print("rcpt_begin");
        out.printStr("kikancode", rcpt.kikancode);
        out.printInt("fukenbangou", rcpt.todoufukenBangou);
        out.printInt("shinryou.nen", rcpt.nen);
        out.printInt("shinryou.tsuki", rcpt.month);
        out.printStr("shozaichimeishou.line1", rcpt.clinicAddress);
        out.printStr("shozaichimeishou.line2", rcpt.clinicPhone);
        out.printStr("shozaichimeishou.line4", rcpt.clinicName);
        out.printInt("patient_id", seikyuu.patientId);
        out.printStr("hokenshubetsu", hokenShubetsuSlug(seikyuu.hokenShubetsu));
        out.printStr("hokentandoku", hokenTandokuSlug(seikyuu.hokenTandoku));
        out.printStr("hokenfutan", hokenFutanSlug(seikyuu.hokenFutan));
        ifPositive(seikyuu.kouhiFutanshaBangou1, n -> {
            String value = String.format("%08d", n);
            out.printStr("kouhifutanshabangou1", value);
        });
        ifPositive(seikyuu.kouhiJukyuushaBangou1, n -> {
            String value = String.format("%07d", n);
            out.printStr("kouhifutaniryoujukyuushabangou1", value);
        });
        ifPositive(seikyuu.kouhiFutanshaBangou2, n -> {
            String value = String.format("%08d", n);
            out.printStr("kouhifutanshabangou2", value);
        });
        ifPositive(seikyuu.kouhiJukyuushaBangou2, n -> {
            String value = String.format("%07d", n);
            out.printStr("kouhifutaniryoujukyuushabangou2", value);
        });
        ifPositiveOrElse(seikyuu.hokenshaBangou,
                n -> out.printStr("hokenshabangou", formatHokenshaBangou(n)),
                () -> System.err.printf("%d: 保険者番号なし\n", seikyuu.patientId)
        );
        ifNotEmpty(seikyuu.hihokenshaKigou, seikyuu.hihokenshaBangou,
                (a, b) -> {
                    String value = String.format("%s     %s", a, b);
                    out.printStr("hihokenshashou", value);
                },
                () -> System.err.printf("%d: 被保険者記号番号なし\n", seikyuu.patientId)
        );
        out.printInt("kyuufuwariai", seikyuu.kyuufuWariai);
        out.printStr("shimei", seikyuu.shimei);
        out.printStr("seibetsu", seibetsuSlug(seikyuu.seibetsu));
        LocalDate birthday = LocalDate.parse(seikyuu.birthday);
        out.printStr("seinengappi.gengou", getGengouSlug(birthday));
        out.printInt("seinengappi.nen", DateTimeUtil.getNen(birthday));
        out.printInt("seinengappi.tsuki", birthday.getMonthValue());
        out.printInt("seinengappi.hi", birthday.getDayOfMonth());
        out.print("rcpt_end");
    }

    private String hokenShubetsuSlug(String hokenShubetsu) {
        switch (hokenShubetsu) {
            case "社国":
                return "shakoku";
            case "公費":
                return "kouhi";
            case "老人":
                return "roujin";
            case "退職":
                return "taishoku";
            case "後期高齢":
                return "koukikourei";
            default:
                throw new RuntimeException("Unknown hoken shubetsu: " + hokenShubetsu);
        }
    }

    private String hokenTandokuSlug(String hokenTandoku) {
        switch (hokenTandoku) {
            case "単独":
                return "tandoku";
            case "２併":
                return "hei2";
            case "３併":
                return "hei3";
            default:
                throw new RuntimeException("Unknown hoken tandoku: " + hokenTandoku);
        }
    }

    private String hokenFutanSlug(String hokenFutan) {
        switch (hokenFutan) {
            case "本人":
                return "honnin";
            case "三才未満":
                return "sansai";
            case "六才未満":
                return "rokusai";
            case "家族":
                return "kazoku";
            case "高齢９":
                return "kourei9";
            case "高齢８":
                return "kourei8";
            case "高齢７":
                return "kourei7";
            default:
                throw new RuntimeException("Unknown hoken futan: " + hokenFutan);
        }
    }

    private void ifPositive(int n, Consumer<Integer> cb) {
        if (n > 0) {
            cb.accept(n);
        }
    }

    private void ifPositiveOrElse(int n, Consumer<Integer> presentCb, Runnable errorCb) {
        if (n > 0) {
            presentCb.accept(n);
        } else {
            errorCb.run();
        }
    }

    private void ifNotEmpty(String a, String b, BiConsumer<String, String> cb, Runnable errorCb) {
        if (a == null) {
            a = "";
        }
        if (b == null) {
            b = "";
        }
        if (a.isEmpty() && b.isEmpty()) {
            errorCb.run();
        } else {
            cb.accept(a, b);
        }
    }

    private void ifNotNull(String s, Consumer<String> cb) {
        if (s != null) {
            cb.accept(s);
        }
    }

    private String formatHokenshaBangou(int hokenshaBangou) {
        if (hokenshaBangou < 10000)
            return String.format("%04d", hokenshaBangou);
        if (hokenshaBangou <= 99999)
            return String.format("%06d", hokenshaBangou);
        if (hokenshaBangou >= 1000000 && hokenshaBangou <= 9999999)
            return String.format("%08d", hokenshaBangou);
        return "" + hokenshaBangou;
    }

    private String seibetsuSlug(String seibetsu) {
        switch (seibetsu) {
            case "男":
                return "otoko";
            case "女":
                return "onna";
            default:
                throw new RuntimeException("Unknown seibtsu: " + seibetsu);
        }
    }

    private String getGengouSlug(LocalDate date) {
        return Gengou.fromEra(DateTimeUtil.getEra(date)).getRomaji();
    }

    private String getGengou(LocalDate date) {
        return Gengou.fromEra(DateTimeUtil.getEra(date)).getKanji();
    }


}
