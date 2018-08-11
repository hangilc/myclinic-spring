package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.input.*;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.RcptUtil;
import jp.chang.myclinic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
    private static Logger logger = LoggerFactory.getLogger(Item.class);

    public Object rep;
    public int tanka;
    public TekiyouProc tekiyouProc;
    public int count;

    public Item(Object rep, int tanka, TekiyouProc tekiyouProc, int count) {
        this.rep = rep;
        this.tanka = tanka;
        this.tekiyouProc = tekiyouProc;
        this.count = count;
    }

    public boolean canMerge(Item arg) {
        if (arg == null) {
            return false;
        }
        return Objects.equals(rep, arg.rep);
    }

    public static void add(List<Item> items, Item item) {
        for (Item i : items) {
            if (i.canMerge(item)) {
                i.count += item.count;
                return;
            }
        }
        items.add(item);
    }

    public static Item fromShinryou(Shinryou shinryou, TekiyouProc tekiyouProc) {
        return new Item(
                new ShinryouRep(shinryou),
                shinryou.tensuu,
                tekiyouProc,
                1
        );
    }

    public static Item fromShinryou(Shinryou shinryou, Map<Integer, String> aliasMap) {
        String a = aliasMap.get(shinryou.shinryoucode);
        String name = a == null ? shinryou.name : a;
        return fromShinryou(shinryou,
                (output, shuukei, tanka, count) -> output.printTekiyou(shuukei, name, tanka, count));
    }

    public static Item fromNaifukuCollector(NaifukuCollector collector) {
        return new Item(
                collector.getNaifukuRep(),
                collector.getTanka(),
                (output, shuukei, tanka, count) -> {
                    output.beginDrug(shuukei, tanka, count);
                    for (Naifuku drug : collector.getNaifukuList()) {
                        output.addDrug(drug.name, drug.amount, drug.unit);
                    }
                    output.endDrug();
                },
                collector.getDays()
        );
    }

    public static Item fromTonpuku(Tonpuku drug) {
        return new Item(
                new TonpukuRep(drug),
                RcptUtil.touyakuKingakuToTen(drug.amount * drug.yakka),
                (output, shuukei, tanka, count) -> {
                    output.beginDrug(shuukei, tanka, count);
                    output.addDrug(drug.name, drug.amount, drug.unit);
                    output.endDrug();
                },
                drug.days
        );
    }

    public static Item fromGaiyou(Gaiyou drug) {
        if (drug.unit.equals("枚")) {
            int timesPerDay = parseGaiyouTimesPerDay(drug.usage, 1);
            int sheetsPerTimes = parseGaiyouSheetsPerTimes(drug.usage, 1);
            String digits = String.format("%d", timesPerDay * sheetsPerTimes);
            String kanjiDigits = StringUtil.transliterate(digits, StringUtil::digitToKanji);
            String text = String.format("１日%s枚", kanjiDigits);
            return new Item(
                    new GaiyouRep(drug),
                    RcptUtil.touyakuKingakuToTen(drug.amount * drug.yakka),
                    (output, shuukei, tanka, count) -> {
                        output.beginDrug(shuukei, tanka, count);
                        output.addDrug(drug.name, drug.amount, drug.unit);
                        output.endDrug();
                        output.printTekiyouAux(text);
                    },
                    1
            );
        } else {
            return new Item(
                    new GaiyouRep(drug),
                    RcptUtil.touyakuKingakuToTen(drug.amount * drug.yakka),
                    (output, shuukei, tanka, count) -> {
                        output.beginDrug(shuukei, tanka, count);
                        output.addDrug(drug.name, drug.amount, drug.unit);
                        output.endDrug();
                    },
                    1
            );
        }
    }

    private static Pattern gaiyouTaimesPerDayPattern = Pattern.compile(
            "[1１一]日\\s*([0-9０-９]+)回"
    );

    private static Integer parseGaiyouTimesPerDay(String usage, Integer defaultValue) {
        Matcher matcher = gaiyouTaimesPerDayPattern.matcher(usage);
        if (matcher.find()) {
            String src = StringUtil.transliterate(matcher.group(1), StringUtil::kanjiToDigit);
            try {
                return Integer.parseInt(src);
            } catch (NumberFormatException ex) {
                logger.error("Invalid gaiyou times per day: {}", usage);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    private static Pattern gaiyouSheetsPerTimesPattern = Pattern.compile(
            "[1１一]回\\s*([0-9０-９]+)枚"
    );

    private static Integer parseGaiyouSheetsPerTimes(String usage, Integer defaultValue) {
        Matcher matcher = gaiyouTaimesPerDayPattern.matcher(usage);
        if (matcher.find()) {
            String src = StringUtil.transliterate(matcher.group(1), StringUtil::kanjiToDigit);
            try {
                return Integer.parseInt(src);
            } catch (NumberFormatException ex) {
                logger.error("Invalid gaiyou sheets per times: {}", usage);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Item fromConductShinryou(ConductShinryou shinryou) {
        return new Item(
                new ConductShinryouRep(shinryou),
                shinryou.tensuu,
                (output, shuukei, tanka, count) -> output.printTekiyou(shuukei, shinryou.name, tanka, count),
                1
        );
    }

    public static Item fromConductDrug(SubShuukei subShuukei, ConductDrug drug) {
        int ten;
        if (subShuukei == SubShuukei.SUB_GAZOU) {
            ten = RcptUtil.shochiKingakuToTen(drug.yakka * drug.amount);
        } else {
            ten = RcptUtil.touyakuKingakuToTen(drug.yakka * drug.amount);
        }
        String label = DrugUtil.conductDrugRep()
        return new Item(
                new ConductDrugRep(drug),
                ten,
                (output, shuukei, tanka, count) -> {
                },
                1
        );
    }

}
