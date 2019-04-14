package jp.chang.myclinic.dto;

public class EnterConductKizaiByNamesRequestDTO {

    public String name;
    public double amount;

    @Override
    public String toString() {
        return "EnterConductKizaiByNamesRequestDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
