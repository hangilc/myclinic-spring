package jp.chang.myclinic.kouhatsukasan;

class YakuzaiEntry {

    String name;
    String yakkacode;
    Kubun kubun;

    @Override
    public String toString() {
        return "YakuzaiEntry{" +
                "name='" + name + '\'' +
                ", yakkacode='" + yakkacode + '\'' +
                ", kubun=" + kubun +
                '}';
    }
}
