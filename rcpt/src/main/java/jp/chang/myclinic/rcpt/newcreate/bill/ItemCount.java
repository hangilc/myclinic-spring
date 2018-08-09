package jp.chang.myclinic.rcpt.newcreate.bill;

class ItemCount {

    //private static Logger logger = LoggerFactory.getLogger(ItemCount.class);
    private Item item;
    private int count;

    ItemCount(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    ItemCount increment(int n){
        return new ItemCount(item, count + n);
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
}
