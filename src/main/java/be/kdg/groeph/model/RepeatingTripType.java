package be.kdg.groeph.model;

import be.kdg.groeph.model.Null.Nullable;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_repeatingTripType")
public class RepeatingTripType implements Serializable, Nullable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "repeatingType", nullable = false, length = 100)
    private String repeatingType;


    public RepeatingTripType() {

    }

    public String getRepeatingType() {
        return repeatingType;
    }

    public void setRepeatingType(String repeatingType) {
        this.repeatingType = repeatingType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public String toString() {
        return repeatingType;
    }

}
