package jp.chang.myclinic.integraltest;

class CompositeEquals {

    //private static Logger logger = LoggerFactory.getLogger(CompositeEquals.class);
    private boolean isEqual = true;

    CompositeEquals() {

    }

    private CompositeEquals and(boolean b){
        if( isEqual ){
            this.isEqual = b;
        }
        return this;
    }

    CompositeEquals and(int a, int b){
        return and(a == b);
    }

    CompositeEquals and(String a, String b){
        return and((a == null && b == null) || (a != null && a.equals(b)));
    }

    boolean isEqual(){
        return isEqual;
    }

}
