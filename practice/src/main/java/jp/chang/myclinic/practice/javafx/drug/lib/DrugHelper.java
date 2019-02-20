package jp.chang.myclinic.practice.javafx.drug.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

class DrugHelper {

    String formatAmount(double amountValue){
        DecimalFormat amountFormatter = new DecimalFormat("###.##");
        return amountFormatter.format(amountValue);
    }

}
