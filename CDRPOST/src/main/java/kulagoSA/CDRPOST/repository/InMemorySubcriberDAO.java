package kulagoSA.CDRPOST.repository;
import kulagoSA.CDRPOST.model.Subscriber;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemorySubcriberDAO {
    private final List<Subscriber> SUBNOTE = new ArrayList<>();

    public List<Subscriber> findAllSUBData() {
        return  SUBNOTE;
    }

    public Subscriber saveSUB(Subscriber SUBNote) {
        SUBNOTE.add(SUBNote);
        return SUBNote;
    }

    public Subscriber findByNumber(String Number) {
        return SUBNOTE.stream()
                .filter(element -> element.getPhoneNumber().equals(Number))
                .findFirst()
                .orElse(null);
    }

    public Subscriber updateNumber(Subscriber SUBNote) {
        var SUBIndex = IntStream.range(0, SUBNOTE.size())
                .filter(index -> SUBNOTE.get(index).getPhoneNumber().equals(SUBNote.getPhoneNumber()))
                .findFirst()
                .orElse(-1);
        if (SUBIndex > -1){
            SUBNOTE.set(SUBIndex, SUBNote);
            return SUBNote;
        }
        return null;
    }

    public void deleteSUB(String Number) {
        var SUBNote = findByNumber(Number);
        if (SUBNote != null){
            SUBNOTE.remove(SUBNote);
        }
    }
}

