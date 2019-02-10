package jp.chang.myclinic.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CmdArgs {

    //private static Logger logger = LoggerFactory.getLogger(CmdArgs.class);

    private String serverUrl;
    private Integer managementPort;

    CmdArgs(String[] args) {
        int i = 0;
        loop: for(;i<args.length;i++){
            String arg = args[i];
            switch(arg){
                case "--management": {
                    i += 1;
                    if( i >= args.length ){
                        System.err.println("Cannot find value for --management option");
                        System.exit(1);
                    }
                    this.managementPort = Integer.parseInt(args[i]);
                    break;
                }
                default:
                    break loop;
            }
        }
        if( i == args.length - 1 ){
            serverUrl = args[i];
        } else if( i >= args.length ){
            serverUrl = System.getenv("MYCLINIC_SERVICE");
        } else {
            System.err.println("Too many arguments.");
            System.exit(1);
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public Integer getManagementPort() {
        return managementPort;
    }
}
