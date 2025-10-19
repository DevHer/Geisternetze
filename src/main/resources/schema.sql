CREATE TABLE IF NOT EXISTS monthly_stat (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  recovered_count BIGINT,
  reported_count BIGINT,
  `year_month` VARCHAR(255)
) ENGINE=InnoDB;
