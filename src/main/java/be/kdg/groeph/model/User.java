package be.kdg.groeph.model;

import be.kdg.groeph.model.Null.NullUser;
import be.kdg.groeph.model.Null.Nullable;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * To change this template use File | Settings | File Templates.
 */



@Entity
@Table(name="t_user")
public class User implements Nullable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "{id} {notempty}")
    private int id;
    @NotEmpty(message = "{firstName} {notempty}")
    @Length(max=50, message = "{firstName} {length}")
    @NotNull(message = "{firstName} {notempty}")
    @Column(name="firstName", nullable = false, length = 100)
    private String firstName;
    @NotEmpty(message = "{lastName} {notempty}")
    @Length(max=50, message = "{lastName} {length}")
    @Column (name="lastName", nullable = false, length = 50)
    private String lastName;
    @NotNull(message = "{dateOfBirth} {notempty}")
    @Past(message = "{dateOfBirth} {past}")
    @Column(name="dateOfBirth", nullable = true, length = 100)
    private Date dateOfBirth;
    @Length(max=30, message = "{phoneNumber} {length}")
    @Column(name="phoneNumber", nullable = true, length = 100)
    private String phoneNumber;
    @Column(name="gender", nullable = true, length = 100)
    private char gender;
    //@NotEmpty(message = "{email} {notempty}")
    //@Email(message = "{email} {validEmail}")
    //@Length(max=100, message = "{email} {length}")
    @Column(name="email", nullable = true, length = 100)
    private String email;
    //@NotEmpty(message = "{password} {notempty}")
    @Column(name="password", nullable = true, length = 100)
    private String password;
    //@NotEmpty(message = "{firstName} {notempty}")
    @Column(name="dateRegistered", nullable = true, length = 100)
    private Date dateRegistered;

    //@Valid
    //@ManyToOne
    //@JoinColumn(name = "address")
    //@Cascade({org.hibernate.annotations.CascadeType.ALL})
    //private Address address;

    /*
        @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Trip> trips = new ArrayList<Car>();
     */


    public User() {
    }

    public User(String firstName, String lastName, Date dateOfBirth, String phoneNumber, char gender, String email, String password, Address address, Date dateRegistered) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.email = email;
        this.password = password;
        //this.address = address;
        this.dateRegistered = dateRegistered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    */

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int hashCode() {
        int result = 31 * (firstName != null ? firstName.hashCode() : 0);
        result += 31 * (lastName != null ? lastName.hashCode() : 0);
        result += 31 * (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result += 17 * (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result += 31 * (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dob = "";
        String userdob = "";

        try {
            dob = sdf.format(dateOfBirth);
            userdob = sdf.format(user.dateOfBirth);
        } catch (NullPointerException e) {
            //TODO hier nog deftige exception message zetten...
        }

        return !(firstName != null ? !firstName.equals(user.getFirstName()) : user.getFirstName() != null)
                && !(lastName != null ? !lastName.equals(user.getLastName()) : user.getLastName() != null)
                && !(dob != null ? !dob.equals(userdob) : userdob != null)
                && !(email != null ? !email.equals(user.getEmail()) : user.getEmail() != null);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public static User INVALID_USER() {
        return new NullUser();
    }
}