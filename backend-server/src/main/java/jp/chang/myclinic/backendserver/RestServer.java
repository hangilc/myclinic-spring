package jp.chang.myclinic.backendserver;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.PatientDTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class RestServer {

    private DbBackend dbBackend;

    RestServer(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
    }

    @GET
    @Path("get-patient")
    @Produces(MediaType.APPLICATION_JSON)
    public PatientDTO getPatient(@QueryParam("patient-id") int patientId){
        return dbBackend.query(backend -> backend.getPatient(patientId));
    }

}
