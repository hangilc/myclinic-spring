package jp.chang.myclinic.reception.tracker;

import javafx.application.Platform;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.reception.tracker.model.Patient;
import jp.chang.myclinic.reception.tracker.model.PharmaQueue;
import jp.chang.myclinic.reception.tracker.model.Wqueue;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

class ActionHook implements DispatchAction {

    private static Logger logger = LoggerFactory.getLogger(ActionHook.class);

    private ModelRegistry registry;
    private DispatchHook hook;

    ActionHook(ModelRegistry registry, DispatchHook hook) {
        this.registry = registry;
        this.hook = hook;
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        registry.createVisit(created)
                .thenAccept(visit -> Platform.runLater(() -> hook.onVisitCreated(visit, toNext)))
                .exceptionally(HandlerFX.exceptionally());
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        registry.deleteVisit(deleted.visitId);
        hook.onVisitDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        Wqueue wqueue = registry.createWqueue(created);
        hook.onWqueueCreated(wqueue, toNext);
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb) {
        Wqueue wqueue = registry.getWqueue(updated.visitId);
        if (wqueue != null) {
            wqueue.setWaitState(updated.waitState);
            hook.onWqueueUpdated(wqueue, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        registry.deleteWqueue(deleted.visitId);
        hook.onWqueueDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext) {
        PharmaQueue pharmaQueue = registry.createPharmaQueue(created);
        hook.onPharmaQueueCreated(pharmaQueue, toNext);
    }

    @Override
    public void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext) {
        registry.deletePharmaQueue(deleted.visitId);
        hook.onPharmaQueueDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onPatientUpdated(PatientDTO prev, PatientDTO updated, Runnable cb) {
        Patient patient = registry.findPatient(updated.patientId);
        if( patient != null ){
            patient.setLastName(updated.lastName);
            patient.setFirstName(updated.firstName);
            patient.setLastNameYomi(updated.lastNameYomi);
            patient.setFirstNameYomi(updated.firstNameYomi);
            try {
                patient.setBirthday(LocalDate.parse(updated.birthday));
            } catch(Exception ex) {
                GuiUtil.alertError("生年月日が不適切です。");
            }
            Sex sex = Sex.fromCode(updated.sex);
            if( sex != null ){
                patient.setSex(sex);
            } else {
                GuiUtil.alertError("性別が不適切です。");
            }
        }
        cb.run();
    }
}
