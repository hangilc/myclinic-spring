package jp.chang.myclinic.rcpt.create.subshuukei;

class StandardTekiyou implements Tekiyou {

    private String label;
    private int tanka;
    private int  count;

    StandardTekiyou(String label, int tanka, int count) {
        this.label = label;
        this.tanka = tanka;
        this.count = count;
    }

    @Override
    public void output(String shuukei){
        System.out.printf("tekiyou %s:%s:%d:%d\n", shuukei,
                label, tanka, count);
    }

}
