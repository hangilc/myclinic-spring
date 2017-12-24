package jp.chang.myclinic.hotline;

public enum User {
    Practice("診察"),
    Pharmacy("薬局"),
    Reception("受付");

    private String name;
    private String dispName;

    User(String dispName){
        this.name = toString();
        this.dispName = dispName;
    }

    public String getName() {
        return name;
    }

    public String getDispName() {
        return dispName;
    }

    public static User fromName(String name){
        for(User user: values()){
            if( user.getName().equals(name) ){
                return user;
            }
        }
        return null;
    }

    public static User fromNameIgnoreCase(String name){
        for(User user: values()){
            if( user.getName().equalsIgnoreCase(name) ){
                return user;
            }
        }
        return null;
    }
}
