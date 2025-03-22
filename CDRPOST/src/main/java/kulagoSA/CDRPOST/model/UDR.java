package kulagoSA.CDRPOST.model;

public class UDR {
    private String msisdn;
    private CallDetail incomingCall;
    private CallDetail outcomingCall;

    // Геттеры и сеттеры
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public CallDetail getIncomingCall() {
        return incomingCall;
    }

    public void setIncomingCall(CallDetail incomingCall) {
        this.incomingCall = incomingCall;
    }

    public CallDetail getOutcomingCall() {
        return outcomingCall;
    }

    public void setOutcomingCall(CallDetail outcomingCall) {
        this.outcomingCall = outcomingCall;
    }
}