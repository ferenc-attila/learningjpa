package employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ProjectDao {

    private EntityManagerFactory entityManagerFactory;

    public ProjectDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void saveProject(Project project) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(project);
        entityManager.getTransaction().commit();
        entityManager.close();
    }



    public Project findProjectById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Project project = entityManager.find(Project.class, id);
        entityManager.close();
        return project;
    }

    public Project findProjectByIdWithEmployees(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Project project = entityManager.createQuery("select p from Project p join fetch p.employees where p.id = :id",
                Project.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
        return project;
    }
//
//    public Project findProjectByNameWithEmployees(String name) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Project project = entityManager.createQuery("select p from Project p join fetch p.employees where p.name = :name",
//                        Project.class)
//                .setParameter("name", name)
//                .getSingleResult();
//        entityManager.close();
//        return project;
//    }
}
