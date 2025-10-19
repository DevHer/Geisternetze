package de.kurs4.geisternetze.service;

import de.kurs4.geisternetze.entity.MonthlyStat;
import de.kurs4.geisternetze.repository.MonthlyStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class MonthlyStatService {

    private final MonthlyStatRepository repo;

    public MonthlyStatService(MonthlyStatRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void incrementReported() {
        String ym = YearMonth.now().toString();
        MonthlyStat s = repo.findByYearMonth(ym).orElseGet(() -> new MonthlyStat(null, ym, 0L, 0L));
        s.setReportedCount(s.getReportedCount() == null ? 1L : s.getReportedCount() + 1);
        repo.save(s);
    }

    @Transactional
    public void incrementRecovered() {
        String ym = YearMonth.now().toString();
        MonthlyStat s = repo.findByYearMonth(ym).orElseGet(() -> new MonthlyStat(null, ym, 0L, 0L));
        s.setRecoveredCount(s.getRecoveredCount() == null ? 1L : s.getRecoveredCount() + 1);
        repo.save(s);
    }

    public List<MonthlyStat> listAll() {
        return repo.findAll();
    }
}
