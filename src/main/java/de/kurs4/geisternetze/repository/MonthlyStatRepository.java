package de.kurs4.geisternetze.repository;

import de.kurs4.geisternetze.entity.MonthlyStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyStatRepository extends JpaRepository<MonthlyStat, Long> {
    Optional<MonthlyStat> findByYearMonth(String yearMonth);
    boolean existsByYearMonth(String yearMonth);
}
