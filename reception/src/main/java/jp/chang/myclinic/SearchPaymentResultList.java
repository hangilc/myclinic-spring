package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.dto.PaymentVisitPatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hangil on 2017/05/20.
 */
public class SearchPaymentResultList extends JList<PaymentVisitPatientDTO> {

    public SearchPaymentResultList(){
        setCellRenderer(new Renderer());
    }

    private static class Renderer extends JLabel implements ListCellRenderer<PaymentVisitPatientDTO> {

        Renderer(){
            setOpaque(true);
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends PaymentVisitPatientDTO> list, PaymentVisitPatientDTO value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            PatientDTO patient = value.patient;
            VisitDTO visit = value.visit;
            PaymentDTO payment = value.payment;
            String label = String.format("[%d] %s %s %d円　%s", patient.patientId, patient.lastName, patient.firstName,
                    payment.amount, DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt, DateTimeUtil.kanjiFormatter3, null));
            setText(label);
            if( isSelected ){
                setBackground(list.getSelectionBackground());
            } else {
                setBackground(list.getBackground());
            }
            return this;
        }
    }

}
