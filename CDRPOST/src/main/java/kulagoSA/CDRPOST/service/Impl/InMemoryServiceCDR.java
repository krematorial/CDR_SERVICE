package kulagoSA.CDRPOST.service.Impl;

import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.repository.InMemoryCDRcallDAO;
import kulagoSA.CDRPOST.service.serviceCDR;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  InMemoryServiceCDR implements serviceCDR {
    private final InMemoryCDRcallDAO repository;

    public InMemoryServiceCDR(InMemoryCDRcallDAO repository) {
        this.repository = repository;
    }

    public List<CDRcall> findAllCDRData() {
        return  repository.findAllCDRData();
    }

    @Override
    public CDRcall saveCDR(CDRcall CDRcallNote) {
        return repository.saveCDR(CDRcallNote);
    }

    @Override
    public CDRcall findByNumber(String Number) {
       return repository.findByNumber(Number);
    }

    @Override
    public CDRcall updateNumber(CDRcall CDRcallNote) {
        return repository.updateNumber(CDRcallNote);
    }

    @Override
    public void deleteCDR(String Number) {
        repository.deleteCDR(Number);
        }
}
