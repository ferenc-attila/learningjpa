package employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    EmployeeDao employeeDao;

    ParkingPlaceDao parkingPlaceDao;


    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        employeeDao = new EmployeeDao(entityManagerFactory);
        parkingPlaceDao = new ParkingPlaceDao(entityManagerFactory);
    }

    @Test
    void testSaveThenFindById() {
        Employee employee = new Employee("John Doe");
        employeeDao.saveEmployee(employee);

        long id = employee.getId();
        Employee another = employeeDao.findEmployeeById(id);
//        Employee another = employeeDao.findEmployeeById(employee.getDepName(), id);

        assertEquals("John Doe", another.getName());
    }

    @Test
    void testSaveThenListAll() {
        employeeDao.saveEmployee(new Employee("John Doe"));
        employeeDao.saveEmployee(new Employee("Jane Doe"));
//        employeeDao.save(new Employee("x", 1L, "John Doe"));
//        employeeDao.save(new Employee("x", 2L, "Jane Doe"));

        List<Employee> employees = employeeDao.listEmployees();
        assertEquals(List.of("Jane Doe", "John Doe"),
                employees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    @Test
    void testChangeName() {
        Employee employee = new Employee("John Doe");
//        Employee employee = new Employee("x", 1L, "John Doe");
        employeeDao.saveEmployee(employee);

        long id = employee.getId();

        employeeDao.updateEmployeeName(id, "Jack Doe");
//        employeeDao.updateEmployeeName("x", 1L, "Jack Doe");
        Employee another = employeeDao.findEmployeeById(id);
//        Employee modifiedEmployee = employeeDao.findEmployeeById("x", 1L);

        assertEquals("Jack Doe", another.getName());
//        assertEquals("Jack Doe", modifiedEmployee.getName());
    }

    @Test
    void testDelete() {
        Employee employee = new Employee("John Doe");
        employeeDao.saveEmployee(employee);
        long id = employee.getId();
        employeeDao.deleteEmployee(id);

        List<Employee> employees = employeeDao.listEmployees();
        assertTrue(employees.isEmpty());
    }

    @Test
    void testIllegalId() {
        Employee employee = employeeDao.findEmployeeById(12L);
//        Employee employee = employeeDao.findEmployeeById("x", 12L);

        assertNull(employee);
    }

    @Test
    void testEmployeeWithAttributes() {
//        employeeDao.save(new Employee("John Doe", Employee.EmployeeType.HALF_TIME,
//                LocalDate.of(2000, 1, 1)));
        for (int i = 0; i < 10; i++) {
            employeeDao.saveEmployee(new Employee("John Doe", Employee.EmployeeType.HALF_TIME,
                    LocalDate.of(2000, 1, 1)));
        }

        Employee employee = employeeDao.listEmployees().get(0);
        assertEquals(LocalDate.of(2000, 1, 1), employee.getDateOfBirth());
    }

    @Test
    void testSaveEmployeeChangeState() {
        Employee employee = new Employee("John Doe");
        employeeDao.saveEmployee(employee);

        employee.setName("Jack Doe");
        Employee modifiedEmployee = employeeDao.findEmployeeById(employee.getId());

        assertEquals("John Doe", modifiedEmployee.getName());
        assertNotSame(employee, modifiedEmployee);
    }

    @Test
    void testMerge() {
        Employee employee = new Employee("John Doe");
        employeeDao.saveEmployee(employee);
        employee.setName("Jack Doe");
        employeeDao.updateEmployee(employee);

        Employee modifiedEmployee = employeeDao.findEmployeeById(employee.getId());
        assertEquals("Jack Doe", modifiedEmployee.getName());
    }

    @Test
    public void testFlush() {
        for (int i = 0; i < 10; i++) {
            employeeDao.saveEmployee(new Employee("John Doe" + i));
        }
        employeeDao.updateEmployeeNames();
    }

    @Test
    void nicknamesTest() {
        Employee employee = new Employee("John Doe");
        employee.setNicknames(Set.of("Johnny", "J"));
        employeeDao.saveEmployee(employee);

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithNickNames(employee.getId());
        assertEquals(Set.of("J", "Johnny"), anotherEmployee.getNicknames());
    }

    @Test
    void vacationsTest() {
        Employee employee = new Employee("John Doe");
        employee.setVacationBookings(Set.of(new VacationEntry(LocalDate.of(2018, 1, 1), 4),
                new VacationEntry(LocalDate.of(2018, 2, 10), 2)));
        employeeDao.saveEmployee(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithVacations(employee.getId());
        System.out.println(anotherEmployee.getVacationBookings());
        assertEquals(2, anotherEmployee.getVacationBookings().size());
    }

//    @Test
//    void testPhoneNumbers() {
//        Employee employee = new Employee("John Doe");
//        employee.setPhoneNumbers(Map.of("home", "1234", "work", "4321"));
//
//        employeeDao.saveEmployee(employee);
//
//        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
//        assertEquals("1234", anotherEmployee.getPhoneNumbers().get("home"));
//        assertEquals("4321", anotherEmployee.getPhoneNumbers().get("work"));
//    }

    @Test
    void phoneNumberTest() {
        PhoneNumber phoneNumberHome = new PhoneNumber("home", "1234");
        PhoneNumber phoneNumberWork = new PhoneNumber("work", "4321");

        Employee employee = new Employee("John Doe");

        employee.addPhoneNumber(phoneNumberWork);
        employee.addPhoneNumber(phoneNumberHome);
        employeeDao.saveEmployee(employee);

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals(2, anotherEmployee.getPhoneNumbers().size());
        assertEquals("work", anotherEmployee.getPhoneNumbers().get(0).getType());
    }

    @Test
    void addPhoneNumberTest() {
        Employee employee = new Employee("John Doe");

        employeeDao.saveEmployee(employee);

        employeeDao.addPhoneNumber(employee.getId(), new PhoneNumber("home", "1234"));
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals(1, anotherEmployee.getPhoneNumbers().size());
    }

    @Test
    void removeTest() {
        Employee employee = new Employee("John Doe");
        employee.addPhoneNumber(new PhoneNumber("home", "1111"));
        employee.addPhoneNumber(new PhoneNumber("work", "2222"));

        employeeDao.saveEmployee(employee);

        employeeDao.deleteEmployee(employee.getId());
    }

//    @Test
//    void employeeTestWithAddress() {
//        Employee employee = new Employee("John Doe");
//        Address address = new Address("H-1301", "Budapest", "Fő utca");
//        employee.setAddress(address);
//        employeeDao.saveEmployee(employee);
//
//        Employee anotherEmployee = employeeDao.findEmployeeById(employee.getId());
//        assertEquals("H-1301", anotherEmployee.getAddress().getZip());
//    }

    @Test
    void employeeWithAddressAttributesTest() {
        Employee employee = new Employee("John Doe");
        employee.setZip("H-1301");
        employee.setCity("Budapest");
        employee.setLine1("Fő utca");

        employeeDao.saveEmployee(employee);
        Employee anotherEmployee = employeeDao.findEmployeeById(employee.getId());
        assertEquals("H-1301", anotherEmployee.getZip());
    }

    @Test
    void testSaveCompanyEmployee() {
        Employee employee = new Employee("John Doe");
        employeeDao.saveEmployee(employee);

        employeeDao.saveEmployee(new CompanyEmployee("Jack Doe", 25));
        employeeDao.saveEmployee(new ContractEmployee("Jane Doe", 100_000));

        long id = employee.getId();
        Employee another = employeeDao.findEmployeeById(id);
//        Employee another = employeeDao.findEmployeeById(employee.getDepName(), id);

        assertEquals("John Doe", another.getName());

        Employee companyEmployee = employeeDao.findEmployeeByName("Jack Doe");
        assertEquals("Jack Doe", companyEmployee.getName());
        assertEquals(25, ((CompanyEmployee) companyEmployee).getVacation());

        Employee contractedEmployee = employeeDao.findEmployeeByName("Jane Doe");
        assertEquals(100_000, ((ContractEmployee) contractedEmployee).getDailyRate());
    }

    @Test
    void findEmployeeByNameTest() {
        employeeDao.saveEmployee(new Employee("John Doe"));

        Employee employee = employeeDao.findEmployeeByName("John Doe");
        assertEquals("John Doe", employee.getName());
    }

    @Test
    void testPaging() {
        for (int i = 100; i < 300; i++) {
            Employee employee = new Employee("John Doe" + i);
            employeeDao.saveEmployee(employee);
        }
        List<Employee> employees = employeeDao.listEmployees(50, 20);
        assertEquals("John Doe150", employees.get(0).getName());
        assertEquals(20, employees.size());
    }

    @Test
    void findParkingPlaceByNameTest() {
        Employee employee = new Employee("John Doe");
        ParkingPlace parkingPlace = new ParkingPlace(101);
        parkingPlaceDao.saveParkingPlace(parkingPlace);
        employee.setParkingPlace(parkingPlace);
        employeeDao.saveEmployee(employee);
        int number = employeeDao.findParkingPlaceNumberByEmployeeName("John Doe");
        assertEquals(101, number);
    }

    @Test
    void baseDataTest() {
        Employee john = new Employee("John Doe");
        Employee jack = new Employee("Jack Doe");
        employeeDao.saveEmployee(john);
        employeeDao.saveEmployee(jack);

        List<Object[]> listOfBaseData = employeeDao.listEmployeeBaseData();
        assertEquals(2, listOfBaseData.size());
        assertEquals("Jack Doe", listOfBaseData.get(1)[1]);
    }

    @Test
    void baseDataDTOTest() {
        Employee john = new Employee("John Doe");
        Employee jack = new Employee("Jack Doe");
        employeeDao.saveEmployee(john);
        employeeDao.saveEmployee(jack);

        List<EmployeeBaseDataDTO> listOfBaseData = employeeDao.listEmployeeBaseDataDTO();
        assertEquals(2, listOfBaseData.size());
        assertEquals("Jack Doe", listOfBaseData.get(0).getName());
    }

    @Test
    void updateToTypeTest() {
        employeeDao.saveEmployee(new Employee("John Doe", Employee.EmployeeType.FULL_TIME, LocalDate.of(1980, 1, 1)));
        employeeDao.saveEmployee(new Employee("Jack Doe", Employee.EmployeeType.FULL_TIME, LocalDate.of(1990, 1, 1)));
        employeeDao.saveEmployee(new Employee("Jane Doe", Employee.EmployeeType.FULL_TIME, LocalDate.of(2000, 1, 1)));

        employeeDao.updateToType(LocalDate.of(1985, 1, 1), Employee.EmployeeType.HALF_TIME);

        List<Employee> employees = employeeDao.listEmployees();
        assertEquals(Employee.EmployeeType.HALF_TIME, employees.get(0).getEmployeeType());
        assertEquals(Employee.EmployeeType.FULL_TIME, employees.get(2).getEmployeeType());
    }

    @Test
    void deleteWithoutTypeTest() {
        employeeDao.saveEmployee(new Employee("John Doe", Employee.EmployeeType.FULL_TIME, LocalDate.of(1980, 1, 1)));
        employeeDao.saveEmployee(new Employee("Jack Doe", null, LocalDate.of(1990, 1, 1)));
        employeeDao.saveEmployee(new Employee("Jane Doe", Employee.EmployeeType.FULL_TIME, LocalDate.of(2000, 1, 1)));

        employeeDao.deleteWithoutType();

        List<Employee> employees = employeeDao.listEmployees();
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(1).getName());
    }
}