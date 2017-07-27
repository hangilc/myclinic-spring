package jp.chang.myclinic.hotline;

enum User {
    Practice("診察"),
    Pharmacy("薬局"),
    Reception("受付");

    private String name;
    private String dispName;

    User(String dispName){
        this.name = toString();
        this.dispName = dispName;
    }

    String getName() {
        return name;
    }

    String getDispName() {
        return dispName;
    }
}
