package de.kurs4.geisternetze.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ghostnet")
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private String sizeEstimate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Person reporter;

    @ManyToOne
    @JoinColumn(name = "rescuer_id")
    private Person rescuer;

    private LocalDateTime createdAt;

    public GhostNet() {}

    public GhostNet(Long id, Double latitude, Double longitude, String sizeEstimate, String status, Person reporter, Person rescuer, LocalDateTime createdAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sizeEstimate = sizeEstimate;
        this.status = status;
        this.reporter = reporter;
        this.rescuer = rescuer;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getSizeEstimate() { return sizeEstimate; }
    public void setSizeEstimate(String sizeEstimate) { this.sizeEstimate = sizeEstimate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Person getReporter() { return reporter; }
    public void setReporter(Person reporter) { this.reporter = reporter; }
    public Person getRescuer() { return rescuer; }
    public void setRescuer(Person rescuer) { this.rescuer = rescuer; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
