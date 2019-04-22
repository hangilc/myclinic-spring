package jp.chang.myclinic.backendserver;

import picocli.CommandLine.*;

@Command(name = "backend-server")
class CmdOpts {

    @Option(names = "--port", description = "TCP/IP port to listen to.")
    int port = 38080;

}
