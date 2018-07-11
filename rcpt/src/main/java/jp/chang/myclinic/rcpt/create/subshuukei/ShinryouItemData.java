package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

class ShinryouItemData {

    private String name;
    private String tekiyou;
    private String shoujouShouki;

    ShinryouItemData(String name){
        this.name = name;
    }

    ShinryouItemData(Shinryou shinryou) {
        this.name = shinryou.getName();
        this.tekiyou = shinryou.getTekiyou();
        this.shoujouShouki = shinryou.getShoujouShouki();
    }

    public String getName() {
        return name;
    }

    public String getTekiyou() {
        return tekiyou;
    }

    public String getShoujouShouki() {
        return shoujouShouki;
    }
}
