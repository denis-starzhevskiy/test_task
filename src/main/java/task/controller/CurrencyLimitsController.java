package task.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.service.CurrencyLimitService;

@RestController
@RequestMapping("/cryptocurrencies")
public class CurrencyLimitsController {

    @Autowired
    private CurrencyLimitService currencyLimitService;

    @GetMapping("/minprice")
    public ResponseEntity<Object> getMinPrice(@RequestParam(value = "name") String currencyName){
        return currencyLimitService.getMinPriceByCurrencyName(currencyName);
    }

    @GetMapping("/maxprice")
    public ResponseEntity<Object> getMaxPrice(@RequestParam(value = "name") String currencyName){
        return currencyLimitService.getMaxPriceByCurrencyName(currencyName);
    }

    @GetMapping
    public ResponseEntity<Object> getPageElements(@RequestParam(value = "name") String currencyName,
                                              @RequestParam(value = "page", required = false) Integer page_number,
                                              @RequestParam(value = "size", required = false) Short page_size){
        return currencyLimitService.getPageElements(currencyName, page_number, page_size);
    }

    @GetMapping("/csv")
    @SneakyThrows
    public ResponseEntity<Object> getCsvReport() {
        return currencyLimitService.getCsv();
    }

}
