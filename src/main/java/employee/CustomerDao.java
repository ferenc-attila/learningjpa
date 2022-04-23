package employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CustomerDao {

    private EntityManagerFactory entityManagerFactory;

    public CustomerDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void saveCustomer(Customer customer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Customer findCustomerById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, id);
        entityManager.close();
        return customer;
    }

//    public Customer findCustomerByIdWithAddress(Long id) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Customer customer = entityManager.createQuery("select c from Customer c join fetch Address a where c.id = :id",
//                Customer.class)
//                .setParameter("id", id)
//                .getSingleResult();
//        entityManager.close();
//        return customer;
//    }
}
