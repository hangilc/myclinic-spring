package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.input.Naifuku;
import jp.chang.myclinic.rcpt.newcreate.input.Shinryou;
import jp.chang.myclinic.rcpt.newcreate.input.Tonpuku;
import jp.chang.myclinic.util.RcptUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Item {
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
        TonpukuRep rep = new TonpukuRep(drug);
        return new Item(
                rep,
                RcptUtil.touyakuKingakuToTen(drug.amount * drug.yakka),
                (output, shuukei, tanka, count) -> {
                    output.beginDrug(shuukei, tanka, count);
                    output.addDrug(drug.name, drug.amount, drug.unit);
                    output.endDrug();
                },
                drug.days
        );
    }


}
