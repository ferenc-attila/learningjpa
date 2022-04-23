package employee;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employees")
@SecondaryTable(name = "emp_addresses", pkJoinColumns = @PrimaryKeyJoinColumn(name = "emp_id"))
@NamedQuery(name = "listEmployees", query = "select e from Employee e order by e.name")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Employee {

    public enum EmployeeType {FULL_TIME, HALF_TIME}

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "emp_name", length = 200, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType = EmployeeType.FULL_TIME;

    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "nicknames", joinColumns = @JoinColumn(name = "emp_id"))
    @Column(name = "nickname")
    private Set<String> nicknames;

    @ElementCollection
    @CollectionTable(name = "bookings", joinColumns = @JoinColumn(name = "emp_id"))
    @AttributeOverride(name = "startDate", column = @Column(name = "start_date"))
    @AttributeOverride(name = "daysTaken", column = @Column(name = "days"))
    private Set<VacationEntry> vacationBookings;

//    @ElementCollection
//    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "emp_id"))
//    @MapKeyColumn(name = "phone_type")
//    @Column(name = "phone_number")
//    private Map<String, String> phoneNumbers;

    @OneToOne
    private ParkingPlace parkingPlace;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "employee")
//    @OrderBy("type")
    @OrderColumn(name = "pos")
    private List<PhoneNumber> phoneNumbers;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<>();

//    @Embedded
////    @OneToOne
//    private Address address;

    @Column(table = "emp_addresses")
    private String zip;

    @Column(table = "emp_addresses")
    private String city;

    @Column(table = "emp_addresses")
    private String line1;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Employee(String cardNumber, String name) {
        this.cardNumber = cardNumber;
        this.name = name;
    }

    public Employee(String name, EmployeeType employeeType, LocalDate dateOfBirth) {
        this.name = name;
        this.employeeType = employeeType;
        this.dateOfBirth = dateOfBirth;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        if (phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        phoneNumbers.add(phoneNumber);
        phoneNumber.setEmployee(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(Set<String> nicknames) {
        this.nicknames = nicknames;
    }

    public Set<VacationEntry> getVacationBookings() {
        return vacationBookings;
    }

    public void setVacationBookings(Set<VacationEntry> vacationBookings) {
        this.vacationBookings = vacationBookings;
    }

//    public Map<String, String> getPhoneNumbers() {
//        return phoneNumbers;
//    }
//
//    public void setPhoneNumbers(Map<String, String> phoneNumbers) {
//        this.phoneNumbers = phoneNumbers;
//    }

    public ParkingPlace getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(ParkingPlace parkingPlace) {
        this.parkingPlace = parkingPlace;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
