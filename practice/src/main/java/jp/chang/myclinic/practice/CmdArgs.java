package jp.chang.myclinic.practice;

class CmdArgs {

    //private static Logger logger = LoggerFactory.getLogger(CmdArgs.class);

    private String serverUrl;
    private boolean testGui;

    CmdArgs(String[] args) {
        int i = 0;
        loop: for(;i<args.length;i++){
            String arg = args[i];
            switch(arg){
                case "--test-gui": {
                    this.testGui = true;
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

    String getServerUrl() {
        return serverUrl;
    }

    boolean isTestGui() {
        return testGui;
    }
}
