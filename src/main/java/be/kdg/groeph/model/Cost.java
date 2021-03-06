package be.kdg.groeph.model;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * Date: 28/02/13
 * Time: 15:03
 */


@Entity
@Table(name="t_cost")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="text", nullable = false, length = 100)
    String text;

    @Column(name="costValue", nullable = false, length = 100)
    Double costValue;

    @ManyToOne
    @JoinColumn(name = "tripUser")
    //@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    TripUser tripUser;

    @ManyToOne
    @JoinColumn(name = "trip")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    Trip trip;

    public Cost() {
    }

    public Cost(String text, Double costValue, TripUser tripUser, Trip trip) {
        this.text = text;
        this.costValue = costValue;
        this.tripUser = tripUser;
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getCostValue() {
        return costValue;
    }

    public void setCostValue(Double costValue) {
        this.costValue = costValue;
    }

    public TripUser getTripUser() {
        return tripUser;
    }

    public void setTripUser(TripUser tripUser) {
        this.tripUser = tripUser;
    }
}
