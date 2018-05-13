package jp.chang.myclinic.rcpt.data;

class Xml {

    //private static Logger logger = LoggerFactory.getLogger(Xml.class);

    private String indentStr = "";

    Xml() {

    }

    void prelude(){
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    void element(String tag, Runnable inner){
        System.out.printf("%s<%s>\n", indentStr, tag);
        indent();
        inner.run();
        unindent();
        System.out.printf("%s</%s>\n", indentStr, tag);
    }

    void element(String tag, int value){
        System.out.printf("%s<%s>%d</%s>\n", indentStr, tag, value, tag);
    }

    void element(String tag, String value){
        System.out.printf("%s<%s>%s</%s>\n", indentStr, tag, value, tag);
    }

    void element(String tag, char value){
        System.out.printf("%s<%s>%c</%s>\n", indentStr, tag, value, tag);
    }

    void element(String tag, String fmt, double value){
        System.out.printf("%s<%s>" + fmt + "</%s>\n", indentStr, tag, value, tag);
    }

    void indent(){
        indentStr += "  ";
    }

    void unindent(){
        if( indentStr.length() >= 2 ) {
            indentStr = indentStr.substring(2);
        }
    }

}
