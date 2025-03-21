package kulagoSA.CDRPOST.model;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class CDRcall {
    private String call;
    private String iniciate_Number;
    private String accept_Number;
    private String DateStart;
    private String DateEnd;


    public String getCall() {
        return call;
    }

    // Сеттер для поля call
    public void setCall(String call) {
        this.call = call;
    }

    // Геттер для поля iniciate_Number
    public String getIniciate_Number() {
        return iniciate_Number;
    }

    // Сеттер для поля iniciate_Number
    public void setIniciate_Number(String iniciate_Number) {
        this.iniciate_Number = iniciate_Number;
    }

    // Геттер для поля accept_Number
    public String getAccept_Number() {
        return accept_Number;
    }

    // Сеттер для поля accept_Number
    public void setAccept_Number(String accept_Number) {
        this.accept_Number = accept_Number;
    }

    // Геттер для поля DateStart
    public String getDateStart() {
        return DateStart;
    }

    // Сеттер для поля DateStart
    public void setDateStart(String DateStart) {
        this.DateStart = DateStart;
    }

    // Геттер для поля DateEnd
    public String getDateEnd() {
        return DateEnd;
    }

    // Сеттер для поля DateEnd
    public void setDateEnd(String DateEnd) {
        this.DateEnd = DateEnd;
    }
}