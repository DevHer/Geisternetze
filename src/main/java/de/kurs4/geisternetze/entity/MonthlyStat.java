package de.kurs4.geisternetze.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "monthly_stat")
public class MonthlyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String yearMonth;

    private Long reportedCount;
    private Long recoveredCount;

    public MonthlyStat() {}

    public MonthlyStat(Long id, String yearMonth, Long reportedCount, Long recoveredCount) {
        this.id = id;
        this.yearMonth = yearMonth;
        this.reportedCount = reportedCount;
        this.recoveredCount = recoveredCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getYearMonth() { return yearMonth; }
    public void setYearMonth(String yearMonth) { this.yearMonth = yearMonth; }
    public Long getReportedCount() { return reportedCount; }
    public void setReportedCount(Long reportedCount) { this.reportedCount = reportedCount; }
    public Long getRecoveredCount() { return recoveredCount; }
    public void setRecoveredCount(Long recoveredCount) { this.recoveredCount = recoveredCount; }
}
