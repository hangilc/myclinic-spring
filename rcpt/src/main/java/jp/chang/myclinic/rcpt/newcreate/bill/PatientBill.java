package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.newcreate.input.Byoumei;
import jp.chang.myclinic.rcpt.newcreate.input.Seikyuu;
import jp.chang.myclinic.rcpt.newcreate.input.Shinryou;
import jp.chang.myclinic.rcpt.newcreate.input.Visit;
import jp.chang.myclinic.rcpt.newcreate.output.Output;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

class PatientBill {

    //private static Logger logger = LoggerFactory.getLogger(PatientBill.class);
    private Seikyuu seikyuu;
    private Output out;
    private ResolvedShinryouMap resolvedShinryouMap;
    private Map<Integer, String> shinryouAliasMap;
    private Map<SubShuukei, List<Item>> itemMap = new HashMap<>();
    private Shuukei shoshinShuukei = new Shuukei("shoshin", false, true);
    private List<String> shoshinKasan = new ArrayList<>();
    private Shuukei saishinSaishinShuukei = new Shuukei("saishin.saishin", true, true);
    private Shuukei saishinGairaiKanriShuukei = new Shuukei("saishin.gairaikanri", true, true);
    private Shuukei saishinJikangaiShuukei = new Shuukei("saishin.jikangai", true, true);
    private Shuukei saishinKyuujitsuShuukei = new Shuukei("saishin.kyuujitsu", true, true);
    private Shuukei saishinShinyaShuukei = new Shuukei("saishin.shinya", true, true);
    private Shuukei shidouShuukei = new Shuukei("shidou", false, false);
    private Shuukei zaitakuOushinShuukei = new Shuukei("zaitaku.oushin", false, true);
    private Shuukei zaitakuSonotaShuukei = new Shuukei("zaitaku.sonota", false, false);
    // TODO: (Zaitaku) add yakan, shinya, houmon, yakuzai

    PatientBill(Seikyuu seikyuu, Output output, ResolvedShinryouMap resolvedShinryouMap,
                Map<Integer, String> shinryouAliasMap) {
        this.seikyuu = seikyuu;
        this.out = output;
        this.resolvedShinryouMap = resolvedShinryouMap;
        this.shinryouAliasMap = shinryouAliasMap;
    }

    void run() {
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
        runByoumei(seikyuu);
        System.out.printf("shinryounissuu.hoken %d\n", calcShinryouNissuuHoken(seikyuu.visits));
        ifPositive(calcShinryouNissuuKouhi1(seikyuu.visits), kouhi1Count ->
                out.printInt("shinryounissuu.kouhi.1", kouhi1Count));
        ifPositive(calcShinryouNissuuKouhi2(seikyuu.visits), kouhi2Count ->
                out.printInt("shinryounissuu.kouhi.2", kouhi2Count));
        for (Visit visit : seikyuu.visits) {
            for (Shinryou shinryou : visit.shinryouList) {
                dispatchShinryou(shinryou, LocalDate.parse(visit.visitedAt.substring(0, 10)));
            }
        }
        shoshinShuukei.print(out);
        shoshinKasan.forEach(kasan -> out.printStr("shoshinkasan", kasan));
        outputTekiyou(SubShuukei.SUB_SHOSHIN);
        saishinSaishinShuukei.print(out);
        saishinGairaiKanriShuukei.print(out);
        saishinJikangaiShuukei.print(out);
        saishinKyuujitsuShuukei.print(out);
        saishinShinyaShuukei.print(out);
        outputTekiyou(SubShuukei.SUB_SAISHIN);
        shidouShuukei.print(out);
        outputTekiyou(SubShuukei.SUB_SHIDOU);
        zaitakuOushinShuukei.print(out);
        zaitakuSonotaShuukei.print(out);
        outputTekiyou(SubShuukei.SUB_ZAITAKU);
        out.printInt("kyuufu.hoken.seikyuuten", calcTotalTen());
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

    private void runByoumei(Seikyuu seikyuu) {
        int index = 1;
        List<String> chiyu = new ArrayList<>();
        List<String> shibou = new ArrayList<>();
        List<String> chuushi = new ArrayList<>();
        for (Byoumei byoumei : seikyuu.byoumeiList) {
            LocalDate startDate = LocalDate.parse(byoumei.startDate);
            if (index <= 4) {
                out.printStr(String.format("shoubyoumei.%d", index), byoumei.name);
                out.printInt(String.format("shinryoukaishi.nen.%d", index), DateTimeUtil.getNen(startDate));
                out.printInt(String.format("shinryoukaishi.tsuki.%d", index), startDate.getMonthValue());
                out.printInt(String.format("shinryoukaishi.hi.%d", index), startDate.getDayOfMonth());
            } else {
                out.printStr("shoubyoumei_extra",
                        String.format("%d:%s:%d:%d:%d",
                                index,
                                byoumei.name,
                                DateTimeUtil.getNen(startDate),
                                startDate.getMonthValue(),
                                startDate.getDayOfMonth()));
            }
            final int currentIndex = index;
            ifNotNull(byoumei.endDate, s -> {
                LocalDate d = LocalDate.parse(s);
                String tenkiDate = String.format("%c%d.%02d.%02d", getGengou(d).charAt(0), DateTimeUtil.getNen(d),
                        d.getMonthValue(), d.getDayOfMonth());
                String tenkiStr = String.format("%d(%s)", currentIndex, tenkiDate);
                switch (byoumei.tenki) {
                    case "治ゆ":
                        chiyu.add(tenkiStr);
                        break;
                    case "死亡":
                        shibou.add(tenkiStr);
                        break;
                    case "中止":
                        chuushi.add(tenkiStr);
                        break;
                }
            });
            index += 1;
        }
        if (chiyu.size() > 0) {
            out.printStr("tenki.chiyu", String.join(",", chiyu));
        }
        if (shibou.size() > 0) {
            out.printStr("tenki.shibou", String.join(",", shibou));
        }
        if (chuushi.size() > 0) {
            out.printStr("tenki.chuushi", String.join(",", chuushi));
        }
    }

    private int calcShinryouNissuuHoken(List<Visit> visits) {
        return (int) visits.stream().map(v -> v.visitedAt).distinct().count();
    }

    private int calcShinryouNissuuKouhi1(List<Visit> visits) {
        // TODO: implement kouhi1 nissuu
        return 0;
    }

    private int calcShinryouNissuuKouhi2(List<Visit> visits) {
        // TODO: implement kouhi2 nissuu
        return 0;
    }

    private void addItem(SubShuukei subShuukei, Item item) {
        if (itemMap.containsKey(subShuukei)) {
            List<Item> items = itemMap.get(subShuukei);
            Item prev = null;
            for (Item i : items) {
                if (i.canMerge(item)) {
                    prev = i;
                    break;
                }
            }
            if (prev != null) {
                prev.count += 1;
            } else {
                items.add(item);
            }
        } else {
            List<Item> items = new ArrayList<>();
            items.add(item);
            itemMap.put(subShuukei, items);
        }
    }

    private void runShoshinKasan(int shinryoucode) {
        if (shinryoucode == resolvedShinryouMap.初診時間外加算 ||
                shinryoucode == resolvedShinryouMap.初診乳幼児時間外加算) {
            shoshinKasan.add("jikangai");
        }
        if (shinryoucode == resolvedShinryouMap.初診休日加算 ||
                shinryoucode == resolvedShinryouMap.初診乳幼児休日加算) {
            shoshinKasan.add("kyuujitsu");
        }
        if (shinryoucode == resolvedShinryouMap.初診深夜加算 ||
                shinryoucode == resolvedShinryouMap.初診乳幼児深夜加算) {
            shoshinKasan.add("shinya");
        }

    }

    private void dispatchShinryou(Shinryou shinryou, LocalDate visitedAt) {
        switch (shinryou.shuukeisaki) {
            case SHUUKEI_SHOSHIN: {
                if (shinryou.shinryoucode == resolvedShinryouMap.初診) {
                    shoshinShuukei.add(shinryou.tensuu);
                    Item item = Item.fromShinryou(shinryou, TekiyouProc.noOutput);
                    addItem(SubShuukei.SUB_SHOSHIN, item);
                } else {
                    runShoshinKasan(shinryou.shinryoucode);
                    addItem(SubShuukei.SUB_SHOSHIN, Item.fromShinryou(shinryou, shinryouAliasMap));
                }
                break;
            }
            case SHUUKEI_SAISHIN_SAISHIN: {
                if (shinryou.shinryoucode == resolvedShinryouMap.再診) {
                    saishinSaishinShuukei.add(shinryou.tensuu);
                    Item item = Item.fromShinryou(shinryou, TekiyouProc.noOutput);
                    addItem(SubShuukei.SUB_SAISHIN, item);
                } else if (shinryou.shinryoucode == resolvedShinryouMap.同日再診) {
                    saishinSaishinShuukei.add(shinryou.tensuu);
                    addItem(SubShuukei.SUB_SAISHIN, Item.fromShinryou(shinryou, shinryouAliasMap));
                }
                break;
            }
            case SHUUKEI_SAISHIN_GAIRAIKANRI: {
                saishinGairaiKanriShuukei.add(shinryou.tensuu);
                addItem(SubShuukei.SUB_SAISHIN, Item.fromShinryou(shinryou, TekiyouProc.noOutput));
                break;
            }
            case SHUUKEI_SAISHIN_JIKANGAI: {
                saishinJikangaiShuukei.add(shinryou.tensuu);
                addItem(SubShuukei.SUB_SAISHIN, Item.fromShinryou(shinryou, TekiyouProc.noOutput));
                break;
            }
            case SHUUKEI_SAISHIN_KYUUJITSU: {
                saishinKyuujitsuShuukei.add(shinryou.tensuu);
                addItem(SubShuukei.SUB_SAISHIN, Item.fromShinryou(shinryou, TekiyouProc.noOutput));
                break;
            }
            case SHUUKEI_SAISHIN_SHINYA: {
                saishinShinyaShuukei.add(shinryou.tensuu);
                addItem(SubShuukei.SUB_SAISHIN, Item.fromShinryou(shinryou, TekiyouProc.noOutput));
                break;
            }
            case SHUUKEI_SHIDOU: {
                shidouShuukei.add(shinryou.tensuu);
                if (shinryou.shinryoucode == resolvedShinryouMap.診療情報提供料１ ||
                        shinryou.shinryoucode == resolvedShinryouMap.療養費同意書交付料) {
                    String dateLabel = DateTimeUtil.toKanji(visitedAt, DateTimeUtil.kanjiFormatter1);
                    addItem(SubShuukei.SUB_SHIDOU, Item.fromShinryou(shinryou, (output, shuukei, tanka, count) -> {
                        output.printTekiyou(shuukei, shinryou.name, tanka, count);
                        output.printTekiyouAux(dateLabel);
                    }));
                } else {
                    addItem(SubShuukei.SUB_SHIDOU, Item.fromShinryou(shinryou, shinryouAliasMap));
                }
                break;
            }
            case SHUUKEI_ZAITAKU:
                if( shinryou.shinryoucode == resolvedShinryouMap.往診 ){
                    zaitakuOushinShuukei.add(shinryou.tensuu);
                    addItem(SubShuukei.SUB_ZAITAKU, Item.fromShinryou(shinryou, shinryouAliasMap));
                } else {
                    zaitakuSonotaShuukei.add(shinryou.tensuu);
                    if( shinryou.shinryoucode == resolvedShinryouMap.訪問看護指示料 ){
                        String dateLabel = DateTimeUtil.toKanji(visitedAt, DateTimeUtil.kanjiFormatter1);
                        addItem(SubShuukei.SUB_ZAITAKU, Item.fromShinryou(shinryou, (output, shuukei, tanka, count) -> {
                            output.printTekiyou(shuukei, shinryou.name, tanka, count);
                            output.printTekiyouAux(dateLabel);
                        }));
                    } else {
                        addItem(SubShuukei.SUB_ZAITAKU, Item.fromShinryou(shinryou, shinryouAliasMap));
                    }
                }
                break;
//            case SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI:
//            case SHUUKEI_TOUYAKU_GAIYOUCHOUZAI:
//            case SHUUKEI_TOUYAKU_SHOHOU:
//            case SHUUKEI_TOUYAKU_MADOKU:
//            case SHUUKEI_TOUYAKU_CHOUKI:
//                shuukei.getTouyakuVisit().add(shinryou);
//                break;
//            case SHUUKEI_CHUUSHA_SEIBUTSUETC:
//            case SHUUKEI_CHUUSHA_HIKA:
//            case SHUUKEI_CHUUSHA_JOUMYAKU:
//            case SHUUKEI_CHUUSHA_OTHERS:
//                shuukei.getChuushaVisit().add(shinryou);
//                break;
//            case SHUUKEI_SHOCHI:
//                shuukei.getShochiVisit().add(shinryou);
//                break;
//            case SHUUKEI_SHUJUTSU_SHUJUTSU:
//            case SHUUKEI_SHUJUTSU_YUKETSU:
//            case SHUUKEI_MASUI:
//                shuukei.getShujutsuVisit().add(shinryou);
//                break;
//            case SHUUKEI_KENSA:
//                shuukei.getKensaVisit().add(shinryou);
//                break;
//            case SHUUKEI_GAZOUSHINDAN:
//                shuukei.getGazouVisit().add(shinryou);
//                break;
//            case SHUUKEI_OTHERS:
//                shuukei.getSonotaVisit().add(shinryou);
//                break;
//            default:
//                shuukei.getSonotaVisit().add(shinryou);
//                break;
        }
    }

    private int calcTotalTen() {
        int ten = 0;
        for (List<Item> items : itemMap.values()) {
            for (Item item : items) {
                ten += item.tanka * item.count;
            }
        }
        return ten;
    }

    private void outputTekiyou(SubShuukei subShuukei) {
        List<Item> items = itemMap.get(subShuukei);
        if( items == null ){
            return;
        }
        int n = items.size();
        for (int i = 0; i < n; i++) {
            Item item = items.get(i);
            String shuukei;
            if (i == 0) {
                shuukei = "" + subShuukei.getCode();
            } else {
                shuukei = "";
            }
            item.tekiyouProc.outputTekiyou(out, shuukei, item.tanka, item.count);
        }
    }

    private String getShinryouTekiyouName(Shinryou shinryou) {
        String a = shinryouAliasMap.get(shinryou.shinryoucode);
        return a == null ? shinryou.name : a;
    }


}
