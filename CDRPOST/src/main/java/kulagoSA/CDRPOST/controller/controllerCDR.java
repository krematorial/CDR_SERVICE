package kulagoSA.CDRPOST.controller;

import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.service.serviceCDR;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/CDR")

public class controllerCDR {

    private final serviceCDR service;

    public controllerCDR(serviceCDR service) {
        this.service = service;
    }

    //todo
    @GetMapping
    public List<CDRcall> findAllCDRData() {
        return service.findAllCDRData();
    }

    @PostMapping("save_CDR")
    public CDRcall saveCDR(@RequestBody CDRcall CDRcallNote){
        return service.saveCDR(CDRcallNote);
    }

    @GetMapping("/(Number)")
    public  CDRcall findByNumber(@PathVariable String Number){
        return service.findByNumber(Number);
    }

    @PutMapping("update_CDR")
    public  CDRcall updateCDR(CDRcall CDRcallNote){
        return service.updateNumber(CDRcallNote);
    }

    @DeleteMapping("delete_CDR/(Number)")
    public void deleteCDR (@PathVariable String Number){
        service.deleteCDR(Number);
    }
}
