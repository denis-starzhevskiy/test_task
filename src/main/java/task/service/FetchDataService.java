package task.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import task.model.CurrencyLimit;

import java.io.DataInput;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class FetchDataService {

    private final RestTemplate restTemplate;
    private final CurrencyLimitService currencyLimitService;

    public FetchDataService(RestTemplateBuilder restTemplateBuilder, CurrencyLimitService currencyLimitService){
        this.restTemplate = restTemplateBuilder.build();
        this.currencyLimitService = currencyLimitService;
    }

    public ResponseEntity<Object> getCurrencyLimits() {
        String resourceUrl = "https://cex.io/api/currency_limits";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent", "Application");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET, entity, String.class);

            JSONObject jsonObject = new JSONObject(responseEntity.getBody());

            if (jsonObject.get("ok").equals("ok")) {
                log.info("Success");

                JSONArray pairs = jsonObject.getJSONObject("data").getJSONArray("pairs");

                ObjectMapper objectMapper = new ObjectMapper();
                ArrayList<CurrencyLimit> currencyLimits = new ArrayList<>();

                for (int i = 0; i < pairs.length(); i++) {
                    CurrencyLimit currencyLimit = objectMapper.readValue(pairs.getString(i), CurrencyLimit.class);
                    // filter (BTC|ETH|XRP)/USD currencies
                    if(currencyLimit.getSymbol2().equals("USD")){
                        if(currencyLimit.getSymbol1().equals("BTC") ||
                                currencyLimit.getSymbol1().equals("ETH") ||
                                currencyLimit.getSymbol1().equals("XRP")){
                            currencyLimits.add(currencyLimit);
                        }
                    }
                }

                return currencyLimitService.saveCurrenciesLimit(currencyLimits);
            }
            else {
                log.error("Server error");
                return null;
            }
        }catch (RestClientException | JSONException | IOException restClientException){
            log.error(restClientException.getMessage());
            return null;
        }
    }

}
