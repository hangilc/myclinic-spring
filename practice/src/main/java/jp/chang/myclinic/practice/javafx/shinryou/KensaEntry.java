package jp.chang.myclinic.practice.javafx.shinryou;

public class KensaEntry {
    String name;
    String value;
    boolean preset;

    KensaEntry(String name){
        this(name, name, false);
    }

    KensaEntry(String name, boolean preset){
        this(name, name, preset);
    }

    KensaEntry(String name, String value){
        this(name, value, false);
    }

    KensaEntry(String name, String value, boolean preset){
        this.name = name;
        this.value = value;
        this.preset = preset;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isPreset() {
        return preset;
    }

    public static KensaEntry sep = new KensaEntry("-");

    public static KensaEntry[] getLeftEntries(){
        return leftEntries;
    }

    public static KensaEntry[] getRightEntries(){
        return rightEntries;
    }

    private static KensaEntry[] leftEntries = new KensaEntry[]{
            new KensaEntry("血算", true),
            new KensaEntry("末梢血液像"),
            new KensaEntry("ＨｂＡ１ｃ", true),
            new KensaEntry("ＰＴ"),
            KensaEntry.sep,
            new KensaEntry("ＧＯＴ", true),
            new KensaEntry("ＧＰＴ", true),
            new KensaEntry("γＧＴＰ", true),
            new KensaEntry("ＣＰＫ"),
            new KensaEntry("クレアチニン", true),
            new KensaEntry("尿酸", true),
            new KensaEntry("カリウム"),
            new KensaEntry("ＬＤＬ－Ｃｈ", "ＬＤＬコレステロール", true),
            new KensaEntry("ＨＤＬ－Ｃｈ", "ＨＤＬコレステロール", true),
            new KensaEntry("ＴＧ", true),
            new KensaEntry("グルコース"),
    };

    private static KensaEntry[] rightEntries = new KensaEntry[]{
            new KensaEntry("ＣＲＰ"),
            KensaEntry.sep,
            new KensaEntry("ＴＳＨ"),
            new KensaEntry("ＦＴ４"),
            new KensaEntry("ＦＴ３"),
            new KensaEntry("ＰＳＡ"),
            KensaEntry.sep,
            new KensaEntry("蛋白定量（尿）", "蛋白定量尿"),
            new KensaEntry("クレアチニン（尿）", "クレアチニン尿"),
    };


}
