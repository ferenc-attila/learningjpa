package employee;

import javax.persistence.*;

@Entity
@Table(name = "parking_places")
public class ParkingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    public ParkingPlace() {
    }

    public ParkingPlace(int number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
