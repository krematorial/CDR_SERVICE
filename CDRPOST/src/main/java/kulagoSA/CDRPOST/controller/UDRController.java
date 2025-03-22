package kulagoSA.CDRPOST.controller;

import kulagoSA.CDRPOST.model.UDR;
import kulagoSA.CDRPOST.service.UDRAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/udr")
public class UDRController {

    @Autowired
    private UDRAggregationService udrAggregationService;

    // Метод для получения UDR по одному абоненту за месяц или за весь период
    @GetMapping("/{msisdn}")
    public UDR getUDRBySubscriber(
            @PathVariable String msisdn,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (month != null && year != null) {
            return udrAggregationService.getUDRForSubscriberByMonth(msisdn, month, year);
        } else {
            return udrAggregationService.getUDRForSubscriber(msisdn);
        }
    }

    // Метод для получения UDR по всем абонентам за месяц
    @GetMapping("/all")
    public List<UDR> getUDRForAllSubscribers(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return udrAggregationService.getUDRForAllSubscribersByMonth(month, year);
    }
}