package jp.chang.myclinic.kouhatsukasan;

class YakuzaiEntry {

    String kubun;
    String name;
    String yakkacode;
    SenpatsuKouhatsuKubun senpatsuKouhatsuKubun;

    @Override
    public String toString() {
        return "YakuzaiEntry{" +
                "kubun='" + kubun + '\'' +
                ", name='" + name + '\'' +
                ", yakkacode='" + yakkacode + '\'' +
                ", senpatsuKouhatsuKubun=" + senpatsuKouhatsuKubun +
                '}';
    }
}
