package jp.chang.myclinic.practice.lib.shinryou;

public class RegularShinryou {

    private static String[] leftItems = new String[]{
            "初診",
            "再診",
            "外来管理加算",
            "特定疾患管理",
            "-",
            "尿便検査判断料",
            "血液検査判断料",
            "生化Ⅰ判断料",
            "生化Ⅱ判断料",
            "免疫検査判断料",
            "微生物検査判断料",
            "静脈採血",
    };

    private static String[] rightItems = new String[]{
            "尿一般",
            "便潜血",
            "-",
            "処方料",
            "処方料７",
            "手帳記載加算",
            "外来後発加算１",
            "特定疾患処方",
            "長期処方",
            "内服調剤",
            "外用調剤",
            "調剤基本",
            "薬剤情報提供",

    };

    private static String[] bottomItems = new String[]{
            "向精神薬",
            "心電図",
            "骨塩定量",
    };

    public static String[] getLeftItems(){
        return leftItems;
    }

    public static String[] getRightItems(){
        return rightItems;
    }

    public static String[] getBottomItems(){
        return bottomItems;
    }

}
