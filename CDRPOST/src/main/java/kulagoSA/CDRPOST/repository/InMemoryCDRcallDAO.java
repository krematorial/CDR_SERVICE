package kulagoSA.CDRPOST.repository;

import kulagoSA.CDRPOST.model.CDRcall;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryCDRcallDAO {
    private final List<CDRcall> CDRNOTE = new ArrayList<>();

    public List<CDRcall> findAllCDRData() {

        //CDRcall call1 = new CDRcall();
        // CDRcall call2 = new CDRcall();

       // call1.setCall("01");
       // call1.setIniciate_Number("123-456-7890");
       // call1.setAccept_Number("987-654-3210");
       // call1.setDateStart("2023-10-01 12:00:00");
       // call1.setDateEnd("2023-10-01 12:05:00");

        //call2.setCall("02");
        //call2.setIniciate_Number("111-222-3333");
       // call2.setAccept_Number("444-555-6666");
        //call2.setDateStart("2023-10-02 14:00:00");
        //call2.setDateEnd("2023-10-02 14:10:00");

       // return List.of(call1, call2);

        return  CDRNOTE;
    }

    public CDRcall saveCDR(CDRcall CDRcallNote) {
        CDRNOTE.add(CDRcallNote);
        return CDRcallNote;
    }

    public CDRcall findByNumber(String Number) {
        return CDRNOTE.stream()
                .filter(element -> element.getCall().equals(Number))
                .findFirst()
                .orElse(null);
    }

    public CDRcall updateNumber(CDRcall CDRcallNote) {
        var CDRnoteIndex = IntStream.range(0, CDRNOTE.size())
                .filter(index -> CDRNOTE.get(index).getCall().equals(CDRcallNote.getCall()))
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
}
