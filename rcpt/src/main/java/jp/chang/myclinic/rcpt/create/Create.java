package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.rcpt.Common;
import jp.chang.myclinic.rcpt.create.subshuukei.ShuukeiMap;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

class Create {

    private static Logger logger = LoggerFactory.getLogger(Create.class);
    private String xmlSrcFile;

    Create(String xmlSrcFile) {
        this.xmlSrcFile = xmlSrcFile;
    }

    void run() {
        try (FileInputStream ins = new FileInputStream(xmlSrcFile)) {
            XmlMapper mapper = new XmlMapper();
            Rcpt rcpt = mapper.readValue(ins, Rcpt.class);
            rcpt.seikyuuList.sort(seikyuuComparator());
            outputRcpt(rcpt);
        } catch (Exception ex) {
            logger.error("Failed to run carete.", ex);
            System.exit(1);
        }
    }

    private Comparator<Seikyuu> seikyuuComparator() {
        Comparator<Seikyuu> comp = Comparator.comparing(Seikyuu::getRankTag);
        return comp.thenComparing(Comparator.comparing(s -> s.patientId));
    }

    private void outputRcpt(Rcpt rcpt) {
        Common.MasterMaps masterMaps = Common.getMasterMaps(rcpt.getDate(1));
        ShuukeiMap.setShinryouMasterMap(masterMaps.resolvedMap.shinryouMap);
        rcpt.seikyuuList.forEach(seikyuu -> {
            System.out.print("rcpt_begin\n");
            System.out.printf("kikancode %s\n", rcpt.kikancode);
            System.out.printf("fukenbangou %d\n", rcpt.todoufukenBangou);
            System.out.printf("shinryou.nen %d\n", rcpt.nen);
            System.out.printf("shinryou.tsuki %d\n", rcpt.month);
            System.out.printf("shozaichimeishou.line1 %s\n", rcpt.clinicAddress);
            System.out.printf("shozaichimeishou.line2 %s\n", rcpt.clinicPhone);
            System.out.printf("shozaichimeishou.line4 %s\n", rcpt.clinicName);
            System.out.printf("patient_id %d\n", seikyuu.patientId);
            System.out.printf("hokenshubetsu %s\n", hokenShubetsuSlug(seikyuu.hokenShubetsu));
            System.out.printf("hokentandoku %s\n", hokenTandokuSlug(seikyuu.hokenTandoku));
            System.out.printf("hokenfutan %s\n", hokenFutanSlug(seikyuu.hokenFutan));
            ifPositive(seikyuu.kouhiFutanshaBangou1, n ->
                    System.out.printf("kouhifutanshabangou1 %08d\n", n)
            );
            ifPositive(seikyuu.kouhiJukyuushaBangou1, n ->
                    System.out.printf("kouhifutaniryoujukyuushabangou1 %07d\n", n)
            );
            ifPositive(seikyuu.kouhiFutanshaBangou2, n ->
                    System.out.printf("kouhifutanshabangou2 %08d\n", n)
            );
            ifPositive(seikyuu.kouhiJukyuushaBangou2, n ->
                    System.out.printf("kouhifutaniryoujukyuushabangou2 %07d\n", n)
            );
            ifPositiveOrElse(seikyuu.hokenshaBangou,
                    n -> System.out.printf("hokenshabangou %s\n", formatHokenshaBangou(n)),
                    () -> System.err.printf("%d: 保険者番号なし\n", seikyuu.patientId)
            );
            ifNotEmpty(seikyuu.hihokenshaKigou, seikyuu.hihokenshaBangou,
                    (a, b) -> System.out.printf("hihokenshashou %s     %s\n", a, b),
                    () -> System.err.printf("%d: 被保険者記号番号なし\n", seikyuu.patientId)
            );
            System.out.printf("kyuufuwariai %d\n", seikyuu.kyuufuWariai);
            System.out.printf("shimei %s\n", seikyuu.shimei);
            System.out.printf("seibetsu %s\n", seibetsuSlug(seikyuu.seibetsu));
            LocalDate birthday = LocalDate.parse(seikyuu.birthday);
            System.out.printf("seinengappi.gengou %s\n", getGengouSlug(birthday));
            System.out.printf("seinengappi.nen %d\n", DateTimeUtil.getNen(birthday));
            System.out.printf("seinengappi.nen %d\n", birthday.getMonthValue());
            System.out.printf("seinengappi.nen %d\n", birthday.getDayOfMonth());
            outputByoumei(seikyuu);
            System.out.printf("shinryounissuu.hoken %d\n", calcShinryouNissuu(seikyuu.visits));
            ShuukeiMap grandShuukei = new ShuukeiMap();
            seikyuu.visits.forEach(visit -> {
                ShuukeiMap shuukei = new ShuukeiMap();
                visit.shinryouList.forEach(shinryou -> dispatchShinryou(shuukei, shinryou));
                visit.drug.naifukuList.forEach(drug -> shuukei.getTouyakuVisit().add(drug));
                visit.drug.tonpukuList.forEach(drug -> shuukei.getTouyakuVisit().add(drug));
                visit.drug.gaiyouList.forEach(drug -> shuukei.getTouyakuVisit().add(drug));
                visit.conducts.forEach(conduct -> dispatchConduct(shuukei, conduct));
                grandShuukei.merge(shuukei);
            });
            System.out.print("rcpt_end\n");
        });
    }

    private void outputByoumei(Seikyuu seikyuu) {
        int index = 1;
        List<String> chiyu = new ArrayList<>();
        List<String> shibou = new ArrayList<>();
        List<String> chuushi = new ArrayList<>();
        for (Byoumei byoumei : seikyuu.byoumeiList) {
            LocalDate startDate = LocalDate.parse(byoumei.startDate);
            if (index <= 4) {
                System.out.printf("shoubyoumei.%d %s\n", index, byoumei.name);
                System.out.printf("shinryoukaishi.nen.%d %d\n", index, DateTimeUtil.getNen(startDate));
                System.out.printf("shinryoukaishi.tsuki.%d %d\n", index, startDate.getMonthValue());
                System.out.printf("shinryoukaishi.hi.%d %d\n", index, startDate.getDayOfMonth());
            } else {
                System.out.printf("shoubyoumei_extra %d:%s:%d:%d:%d\n", index, byoumei.name,
                        DateTimeUtil.getNen(startDate), startDate.getMonthValue(), startDate.getDayOfMonth());
            }
            final int currentIndex = index;
            ifNotNull(byoumei.endDate, s -> {
                LocalDate d = LocalDate.parse(s);
                String tenkiDate = String.format("%c%d.%d.%d", getGengou(d).charAt(0), DateTimeUtil.getNen(d),
                        d.getMonthValue(), d.getDayOfMonth());
                String tenkiStr = String.format("%d(%s)", currentIndex, tenkiDate);
                switch(byoumei.tenki){
                    case "治ゆ": chiyu.add(tenkiStr); break;
                    case "死亡": shibou.add(tenkiStr); break;
                    case "中止": chuushi.add(tenkiStr); break;
                }
            });
            index += 1;
        }
        if( chiyu.size() > 0 ){
            System.out.printf("tenki.chiyu %s", String.join(",", chiyu));
        }
        if( shibou.size() > 0 ){
            System.out.printf("tenki.shibou %s", String.join(",", shibou));
        }
        if( chuushi.size() > 0 ){
            System.out.printf("tenki.chuushi %s", String.join(",", chuushi));
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

    private void ifNotNull(String s, Consumer<String> cb){
        if( s != null ){
            cb.accept(s);
        }
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

    private String getGengou(LocalDate date){
        return Gengou.fromEra(DateTimeUtil.getEra(date)).getKanji();
    }

    private int calcShinryouNissuu(List<Visit> visits){
        return (int)visits.stream().map(v -> v.visitedAt).distinct().count();
    }

    private void dispatchShinryou(ShuukeiMap shuukei, Shinryou shinryou){
        switch(shinryou.shuukeisaki){
            case SHUUKEI_SHOSHIN:
                shuukei.getShoshinVisit().add(shinryou); break;
            case SHUUKEI_SAISHIN_SAISHIN:
            case SHUUKEI_SAISHIN_GAIRAIKANRI:
            case SHUUKEI_SAISHIN_JIKANGAI:
            case SHUUKEI_SAISHIN_KYUUJITSU:
            case SHUUKEI_SAISHIN_SHINYA:
                shuukei.getSaishinVisit().add(shinryou); break;
            case SHUUKEI_SHIDOU:
                shuukei.getShidouVisit().add(shinryou); break;
            case SHUUKEI_ZAITAKU:
                shuukei.getZaitakuVisit().add(shinryou); break;
            case SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI:
            case SHUUKEI_TOUYAKU_GAIYOUCHOUZAI:
            case SHUUKEI_TOUYAKU_SHOHOU:
            case SHUUKEI_TOUYAKU_MADOKU:
            case SHUUKEI_TOUYAKU_CHOUKI:
                shuukei.getTouyakuVisit().add(shinryou); break;
            case SHUUKEI_CHUUSHA_SEIBUTSUETC:
            case SHUUKEI_CHUUSHA_HIKA:
            case SHUUKEI_CHUUSHA_JOUMYAKU:
            case SHUUKEI_CHUUSHA_OTHERS:
                shuukei.getChuushaVisit().add(shinryou); break;
            case SHUUKEI_SHOCHI:
                shuukei.getShochiVisit().add(shinryou); break;
            case SHUUKEI_SHUJUTSU_SHUJUTSU:
            case SHUUKEI_SHUJUTSU_YUKETSU:
            case SHUUKEI_MASUI:
                shuukei.getShujutsuVisit().add(shinryou); break;
            case SHUUKEI_KENSA:
                shuukei.getKensaVisit().add(shinryou); break;
            case SHUUKEI_GAZOUSHINDAN:
                shuukei.getGazouVisit().add(shinryou); break;
            case SHUUKEI_OTHERS:
                shuukei.getSonotaVisit().add(shinryou); break;
            default:
                shuukei.getSonotaVisit().add(shinryou); break;
        }
    }

    private void dispatchConduct(ShuukeiMap shuukei, Conduct conduct){
        ConductKind kind = ConductKind.fromKanjiRep(conduct.kind);
        if( kind == null ){
            throw new RuntimeException("Unknown conduct kind: " + conduct.kind);
        }
        switch(kind){
            case HikaChuusha:
            case JoumyakuChuusha:
            case OtherChuusha:
                shuukei.getChuushaVisit().add(conduct);
                break;
            case Gazou:
                shuukei.getGazouVisit().add(conduct);
                break;
            default:
                throw new RuntimeException("Unknown conduct kind: " + kind);
        }
    }

}
