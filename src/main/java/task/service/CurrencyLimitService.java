package task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import task.model.CurrencyLimit;
import task.repository.CurrencyLimitRepository;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CurrencyLimitService {

    @Autowired
    private CurrencyLimitRepository currencyLimitRepository;

    public ResponseEntity<Object> getMinPriceByCurrencyName(String name){
        try {
            if(currencyLimitRepository.existsBySymbol1(name)) {
                Slice<CurrencyLimit> pagedResult = currencyLimitRepository.getMinPrice(name, PageRequest.of(0, 1));
                return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Wrong currency name", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getMaxPriceByCurrencyName(String name){
        try {
            if(currencyLimitRepository.existsBySymbol1(name)) {
                Slice<CurrencyLimit> pagedResult = currencyLimitRepository.getMaxPrice(name, PageRequest.of(0, 1));
                return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Wrong currency name", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getPageElements(String name, Integer pageNumber, Short pageSize) {
        try {
            if(currencyLimitRepository.existsBySymbol1(name)) {
                String sortBy = "maxPrice";
                Pageable paging = PageRequest.of(pageNumber != null ? pageNumber : 1, pageSize != null ? pageSize : 10, Sort.by(sortBy).descending());
                Slice<CurrencyLimit> pagedResult = currencyLimitRepository.getCurrencyLimitsBySymbol1(name, paging);
                return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Wrong currency name", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> saveCurrenciesLimit(ArrayList<CurrencyLimit> currencyLimits) {
        try{
            List<CurrencyLimit> currencyLimit = currencyLimitRepository.saveAll(currencyLimits);
            return new ResponseEntity<>(currencyLimit, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> getCsv() throws IOException {
        List<CurrencyLimit> currencyLimits = currencyLimitRepository.findAll();
        if(currencyLimits.size() == 0){
            return new ResponseEntity<>("Empty List", HttpStatus.OK);
        }
        File csvReport = printToCsv(currencyLimits.stream().map(CurrencyLimit::toString).collect(Collectors.joining("\n")));
        Path path = Paths.get(csvReport.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .contentLength(csvReport.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    public File printToCsv(String result) throws IOException {
        File csvOutputFile = new File("csvReport.csv");
        FileWriter fileWriter = new FileWriter(csvOutputFile, false);
        fileWriter.write(result);
        fileWriter.close();
        return csvOutputFile;
    }

}
