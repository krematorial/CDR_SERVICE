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
public class UDRAggregationService {

    @Autowired
    private InMemoryCDRcallDAO cdrCallRepository;

    // Получение UDR для одного абонента за весь период
    public UDR getUDRForSubscriber(String msisdn) {
        List<CDRcall> calls = cdrCallRepository.findByIniciateNumberOrAcceptNumber(msisdn, msisdn);
        return aggregateUDR(calls, msisdn);
    }

    // Получение UDR для одного абонента за конкретный месяц
    public UDR getUDRForSubscriberByMonth(String msisdn, int month, int year) {
        LocalDateTime startDate = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime endDate = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        List<CDRcall> calls = cdrCallRepository.findByIniciateNumberOrAcceptNumberAndDateStartBetween(
                msisdn, msisdn, startDate, endDate);
        return aggregateUDR(calls, msisdn);
    }

    // Получение UDR для всех абонентов за конкретный месяц
    public List<UDR> getUDRForAllSubscribersByMonth(int month, int year) {
        LocalDateTime startDate = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime endDate = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        List<CDRcall> calls = cdrCallRepository.findByDateStartBetween(startDate, endDate);
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