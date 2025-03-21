package kulagoSA.CDRPOST.service;

import kulagoSA.CDRPOST.model.CDRcall;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface serviceCDR {

     List<CDRcall> findAllCDRData();

     CDRcall saveCDR(CDRcall CDRcallNote);

     CDRcall findByNumber(String Number);

     CDRcall updateNumber(CDRcall CDRcallNote);

     void deleteCDR(String Number);

}
