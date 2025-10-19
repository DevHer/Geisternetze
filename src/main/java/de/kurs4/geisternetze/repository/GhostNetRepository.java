package de.kurs4.geisternetze.repository;

import de.kurs4.geisternetze.entity.GhostNet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {
    List<GhostNet> findByStatus(String status);

    @Query("select g from GhostNet g where (g.status = 'Geborgen' or g.status = 'Verschollen') and g.createdAt >= :since order by g.createdAt desc")
    List<GhostNet> findRecoveredOrMissingSince(LocalDateTime since);

    long countByStatusAndCreatedAtBetween(String status, LocalDateTime start, LocalDateTime end);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
