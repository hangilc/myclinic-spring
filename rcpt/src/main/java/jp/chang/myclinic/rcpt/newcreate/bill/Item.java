package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.input.Shinryou;

import java.util.Map;
import java.util.Objects;

public class Item {
    public Object rep;
    public int tanka;
    public TekiyouProc tekiyouProc;
    public int count;

    public Item(Object rep, int tanka, TekiyouProc tekiyouProc, int count){
        this.rep = rep;
        this.tanka = tanka;
        this.tekiyouProc = tekiyouProc;
        this.count = count;
    }

    public boolean canMerge(Item arg){
        if( arg == null ){
            return false;
        }
        return Objects.equals(rep, arg.rep);
    }

    public static Item fromShinryou(Shinryou shinryou, TekiyouProc tekiyouProc){
        return new Item(
                new ShinryouRep(shinryou),
                shinryou.tensuu,
                tekiyouProc,
                1
        );
    }

    public static Item fromShinryou(Shinryou shinryou, Map<Integer, String> aliasMap){
        String a = aliasMap.get(shinryou.shinryoucode);
        String name = a == null ? shinryou.name : a;
        return fromShinryou(shinryou,
                (output, shuukei, tanka, count) -> output.printTekiyou(shuukei, name, tanka, count));
    }

}
