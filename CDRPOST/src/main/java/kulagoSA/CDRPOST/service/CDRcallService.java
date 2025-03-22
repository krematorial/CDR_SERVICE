package kulagoSA.CDRPOST.service;

import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.model.CallDetail;
import kulagoSA.CDRPOST.model.UDR;
import kulagoSA.CDRPOST.repository.InMemoryCDRcallDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CDRcallService {

    @Autowired
    private InMemoryCDRcallDAO inMemoryCDRcallDAO;

    // Метод для получения всех CDR записей
    public List<CDRcall> findAllCDRData() {
        return inMemoryCDRcallDAO.findAllCDRData();
    }

    // Метод для сохранения CDR записи
    public CDRcall saveCDR(CDRcall CDRcallNote) {
        return inMemoryCDRcallDAO.saveCDR(CDRcallNote);
    }

    // Метод для поиска CDR записи по номеру
    public CDRcall findByNumber(String Number) {
        return inMemoryCDRcallDAO.findByNumber(Number);
    }

    // Метод для обновления CDR записи
    public CDRcall updateNumber(CDRcall CDRcallNote) {
        return inMemoryCDRcallDAO.updateNumber(CDRcallNote);
    }

    // Метод для удаления CDR записи
    public void deleteCDR(String Number) {
        inMemoryCDRcallDAO.deleteCDR(Number);
    }

    // Метод для получения UDR по одному абоненту за весь период
    public UDR getUDRForSubscriber(String msisdn) {
        List<CDRcall> calls = inMemoryCDRcallDAO.findAllCDRData().stream()
                .filter(call -> call.getIniciate_Number().equals(msisdn) || call.getAccept_Number().equals(msisdn))
                .toList();
        return aggregateUDR(calls, msisdn);
    }

    // Метод для получения UDR по одному абоненту за конкретный месяц
    public UDR getUDRForSubscriberByMonth(String msisdn, int month, int year) {
        LocalDateTime startDate = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime endDate = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        List<CDRcall> calls = inMemoryCDRcallDAO.findAllCDRData().stream()
                .filter(call -> (call.getIniciate_Number().equals(msisdn) || call.getAccept_Number().equals(msisdn)) &&
                        call.getDateStartAsLocalDateTime().isAfter(startDate) &&
                        call.getDateEndAsLocalDateTime().isBefore(endDate))
                .toList();
        return aggregateUDR(calls, msisdn);
    }

    // Метод для получения UDR по всем абонентам за конкретный месяц
    public List<UDR> getUDRForAllSubscribersByMonth(int month, int year) {
        LocalDateTime startDate = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime endDate = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        List<CDRcall> calls = inMemoryCDRcallDAO.findAllCDRData().stream()
                .filter(call -> call.getDateStartAsLocalDateTime().isAfter(startDate) &&
                        call.getDateEndAsLocalDateTime().isBefore(endDate))
                .toList();

        Map<String, UDR> udrMap = new HashMap<>();

        for (CDRcall call : calls) {
            String msisdn = call.getCall().equals("01") ? call.getIniciate_Number() : call.getAccept_Number();
            udrMap.computeIfAbsent(msisdn, k -> new UDR()).setMsisdn(msisdn);
            aggregateCallDetails(udrMap.get(msisdn), call);
        }

        return List.copyOf(udrMap.values());
    }

    // Агрегация данных для UDR
    private UDR aggregateUDR(List<CDRcall> calls, String msisdn) {
        UDR udr = new UDR();
        udr.setMsisdn(msisdn);

        for (CDRcall call : calls) {
            aggregateCallDetails(udr, call);
        }

        return udr;
    }

    // Агрегация данных о звонках
    private void aggregateCallDetails(UDR udr, CDRcall call) {
        Duration duration = Duration.between(call.getDateStartAsLocalDateTime(), call.getDateEndAsLocalDateTime());
        String totalTime = formatDuration(duration);

        if (call.getCall().equals("01")) {
            if (udr.getOutcomingCall() == null) {
                udr.setOutcomingCall(new CallDetail());
            }
            udr.getOutcomingCall().setTotalTime(addDuration(udr.getOutcomingCall().getTotalTime(), totalTime));
        } else {
            if (udr.getIncomingCall() == null) {
                udr.setIncomingCall(new CallDetail());
            }
            udr.getIncomingCall().setTotalTime(addDuration(udr.getIncomingCall().getTotalTime(), totalTime));
        }
    }

    // Форматирование длительности
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Сложение длительностей
    private String addDuration(String currentTime, String newTime) {
        if (currentTime == null) {
            return newTime;
        }

        String[] currentParts = currentTime.split(":");
        String[] newParts = newTime.split(":");

        long totalSeconds = Integer.parseInt(currentParts[2]) + Integer.parseInt(newParts[2]);
        long totalMinutes = Integer.parseInt(currentParts[1]) + Integer.parseInt(newParts[1]) + totalSeconds / 60;
        long totalHours = Integer.parseInt(currentParts[0]) + Integer.parseInt(newParts[0]) + totalMinutes / 60;

        totalSeconds %= 60;
        totalMinutes %= 60;

        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds);
    }
}