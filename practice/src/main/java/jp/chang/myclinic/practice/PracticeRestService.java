package jp.chang.myclinic.practice;

import jp.chang.myclinic.practice.javafx.MainPaneRequirement;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenRequirement;
import jp.chang.myclinic.practice.javafx.text.TextRequirement;

public interface PracticeRestService extends
        TextRequirement.TextRestService,
        ShohousenRequirement.ShohousenRestService,
        MainPaneRequirement.MainPaneRestService {
}
