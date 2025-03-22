package kulagoSA.CDRPOST.repository;

import kulagoSA.CDRPOST.model.CDRcall;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class InMemoryCDRcallDAO {
    private final List<CDRcall> CDRNOTE = new ArrayList<>();

    public List<CDRcall> findAllCDRData() {
        return  CDRNOTE;
    }

    public CDRcall saveCDR(CDRcall CDRcallNote) {
        CDRNOTE.add(CDRcallNote);
        return CDRcallNote;
    }

    public CDRcall findByNumber(String Number) {
        return CDRNOTE.stream()
                .filter(element -> element.getIniciate_Number().equals(Number))
                .findFirst()
                .orElse(null);
    }

    public CDRcall updateNumber(CDRcall CDRcallNote) {
        var CDRnoteIndex = IntStream.range(0, CDRNOTE.size())
                .filter(index -> CDRNOTE.get(index).getIniciate_Number().equals(CDRcallNote.getIniciate_Number()))
                .findFirst()
                .orElse(-1);
        if (CDRnoteIndex > -1){
            CDRNOTE.set(CDRnoteIndex, CDRcallNote);
            return CDRcallNote;
        }
        return null;
    }

    public void deleteCDR(String Number) {
        var CDRcallNote = findByNumber(Number);
        if (CDRcallNote != null){
            CDRNOTE.remove(CDRcallNote);
        }
    }

    public List<CDRcall> findByIniciateNumberOrAcceptNumber(String msisdn, String s) {
        return CDRNOTE.stream()
                .filter(call -> call.getIniciate_Number().equals(msisdn) || call.getAccept_Number().equals(msisdn))
                .collect(Collectors.toList());
    }

    // Метод для поиска записей по iniciateNumber или acceptNumber и диапазону дат
    public List<CDRcall> findByIniciateNumberOrAcceptNumberAndDateStartBetween(
            String msisdn, String s, LocalDateTime startDate, LocalDateTime endDate) {
        return CDRNOTE.stream()
                .filter(call -> (call.getIniciate_Number().equals(msisdn) || call.getAccept_Number().equals(msisdn)) &&
                        call.getDateStartAsLocalDateTime().isAfter(startDate) &&
                        call.getDateStartAsLocalDateTime().isBefore(endDate))
                .collect(Collectors.toList());
    }

    // Метод для поиска записей по диапазону дат
    public List<CDRcall> findByDateStartBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return CDRNOTE.stream()
                .filter(call -> call.getDateStartAsLocalDateTime().isAfter(startDate) &&
                        call.getDateStartAsLocalDateTime().isBefore(endDate))
                .collect(Collectors.toList());
    }

}
