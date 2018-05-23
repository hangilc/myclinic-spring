package jp.chang.myclinic.rcpt.create.subshuukei;

class StandardTekiyou implements Tekiyou {

    private String label;
    private Integer tanka;
    private Integer  count;

    StandardTekiyou(String label, Integer tanka, Integer count) {
        this.label = label;
        this.tanka = tanka;
        this.count = count;
    }

    @Override
    public void output(String shuukei){
        System.out.printf("tekiyou %s:%s:%s:%s\n",
                shuukei,
                label == null ? "" : label,
                tanka == null ? "" : ("" + tanka),
                count == null ? "" : ("" + count));
    }

}
