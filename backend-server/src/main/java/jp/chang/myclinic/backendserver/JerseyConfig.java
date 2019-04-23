package jp.chang.myclinic.backendserver;

import jp.chang.myclinic.backenddb.DbBackend;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
class JerseyConfig extends ResourceConfig {

    private static class Binder extends AbstractBinder {

        private DbBackend dbBackend;

        private Binder(DbBackend dbBackend){
            this.dbBackend = dbBackend;
        }

        @Override
        protected void configure() {
            bind(dbBackend).to(DbBackend.class);
        }
    }

    JerseyConfig(DbBackend dbBackend) {
        register(RestServer.class);
        register(new Binder(dbBackend));
    }
}
