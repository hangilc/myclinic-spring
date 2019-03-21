package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.mockdata.MockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

class TesterBase {

    Backend backend;
    MockData mock = new MockData();

    TesterBase(Backend backend) {
        this.backend = backend;
    }

    public void test(){
        try {
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(DbTest.class)) {
                    method.invoke(this);
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    void confirm(boolean ok){
        if( !ok ){
            throw new RuntimeException("Confirmation failed.");
        }
    }

}
