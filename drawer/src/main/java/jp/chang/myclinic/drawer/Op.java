package jp.chang.myclinic.drawer;

public abstract class Op {

    private OpCode opCode;

    Op(OpCode opCode){
        this.opCode = opCode;
    }

    public OpCode getOpCode(){
        return opCode;
    }

}
