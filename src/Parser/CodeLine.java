package Parser;

public class CodeLine {
    private String opc;
    private String op1;
    private String op2;
    private String res;

    public CodeLine() {
    }

    public CodeLine(String opc, String op1, String op2, String res) {
        this.opc = opc;
        this.op1 = op1;
        this.op2 = op2;
        this.res = res;
    }

    public CodeLine(String opc, String op1, String op2) {
        this.opc = opc;
        this.op1 = op1;
        this.op2 = op2;
    }

    public CodeLine(String opc, String op1) {
        this.opc = opc;
        this.op1 = op1;
    }

    @Override
    public String toString() {
        return opc + "  :  " + op1 + "  ,  " + op2 + "  =>  " + res;
    }

    public String getOpc() {
        return opc;
    }

    public void setOpc(String opc) {
        this.opc = opc;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
