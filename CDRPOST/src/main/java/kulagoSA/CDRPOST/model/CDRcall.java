package kulagoSA.CDRPOST.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;


public class CDRcall {
    @Id
    private int id;


    private String call;
    private String iniciate_Number;
    private String accept_Number;
    private String dateStart;
    private String dateEnd;


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getIniciate_Number() {
        return iniciate_Number;
    }

    public void setIniciate_Number(String iniciate_Number) {
        this.iniciate_Number = iniciate_Number;
    }

    public String getAccept_Number() {
        return accept_Number;
    }

    public void setAccept_Number(String accept_Number) {
        this.accept_Number = accept_Number;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    // Метод для преобразования dateStart в LocalDateTime
    public LocalDateTime getDateStartAsLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.dateStart, formatter);
    }

    // Метод для преобразования dateEnd в LocalDateTime
    public LocalDateTime getDateEndAsLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.dateEnd, formatter);
    }
}