package de.kurs4.geisternetze;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.kurs4.geisternetze.entity.GhostNet;
import de.kurs4.geisternetze.entity.Person;
import de.kurs4.geisternetze.entity.MonthlyStat;
import de.kurs4.geisternetze.repository.GhostNetRepository;
import de.kurs4.geisternetze.repository.PersonRepository;
import de.kurs4.geisternetze.repository.MonthlyStatRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(GhostNetRepository ghostRepo, PersonRepository personRepo, MonthlyStatRepository statRepo) {
        return args -> {
            if (personRepo.count() == 0) {
                Person p1 = personRepo.save(new Person(null, "Anna Müller", "+49-170-1111111"));
                Person p2 = personRepo.save(new Person(null, "Max Mustermann", "+49-170-2222222"));
                Person resc = personRepo.save(new Person(null, "Sea Shepherd Team", "+49-170-3333333"));

                ghostRepo.save(new GhostNet(null, 54.320, 10.123, "klein", "Gemeldet", p1, null, LocalDateTime.now().minusDays(2)));
                ghostRepo.save(new GhostNet(null, 54.322, 10.130, "groß", "Gemeldet", null, null, LocalDateTime.now().minusDays(10)));
                ghostRepo.save(new GhostNet(null, 52.520, 13.405, "mittel", "Bergung bevorstehend", p2, resc, LocalDateTime.now().minusDays(1)));
                ghostRepo.save(new GhostNet(null, 53.0, 9.0, "groß", "Geborgen", p1, resc, LocalDateTime.now().minusDays(5)));
                ghostRepo.save(new GhostNet(null, 53.5, 9.2, "klein", "Verschollen", p2, null, LocalDateTime.now().minusDays(20)));
            }
            YearMonth ym = YearMonth.now();
            if (!statRepo.existsByYearMonth(ym.toString())) {
                statRepo.save(new MonthlyStat(null, ym.toString(), 2L, 1L));
            }
        };
    }
}
