package task.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import task.model.CurrencyLimit;

import java.util.Optional;

@Repository
public interface CurrencyLimitRepository extends JpaRepository<CurrencyLimit, Long> {

    Boolean existsBySymbol1(String name);

    @Query("select c from CurrencyLimit c where c.symbol1 = ?1 ORDER BY c.minPrice")
    Slice<CurrencyLimit> getMinPrice(String name, Pageable pageable);

    @Query("select c from CurrencyLimit c where c.symbol1 = ?1 ORDER BY c.maxPrice desc ")
    Slice<CurrencyLimit> getMaxPrice(String name, Pageable pageable);

    Slice<CurrencyLimit> getCurrencyLimitsBySymbol1(String name, Pageable pageable);
}
