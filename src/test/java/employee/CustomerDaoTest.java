package employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {

    private CustomerDao customerDao;

    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        customerDao = new CustomerDao(entityManagerFactory);
    }

    @Test
    void saveCustomerTest() {
//        Customer customer = new Customer("Smith and Co. Ltd.", new Address("H-1301", "Budapest", "Fő utca"));
        Customer customer = new Customer("Smith and Co. Ltd.");
        customerDao.saveCustomer(customer);

        Customer anotherCustomer = customerDao.findCustomerById(customer.getId());
//        assertEquals("Budapest", anotherCustomer.getAddress().getCity());
        assertEquals("Smith and Co. Ltd.", anotherCustomer.getName());
    }
}