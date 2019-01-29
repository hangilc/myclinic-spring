package jp.chang.myclinic.reception.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MgmtServer {

    private static Logger logger = LoggerFactory.getLogger(MgmtServer.class);
    private Executor executor = Executors.newSingleThreadExecutor();
    private Server server;

    public MgmtServer(int port) {
        this.server = ServerBuilder.forPort(port)
                .executor(executor)
                .addService(new ReceptionMgmtImpl())
                .build();
    }

    public void start(){
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    public void stop(){
        server.shutdownNow();
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
