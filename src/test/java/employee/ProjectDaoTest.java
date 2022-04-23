package employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectDaoTest {

    ProjectDao projectDao;

    EmployeeDao employeeDao;

    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        projectDao = new ProjectDao(entityManagerFactory);
        employeeDao = new EmployeeDao(entityManagerFactory);
    }

//    @Test
//    void saveProjectTest() {
//        Employee john = new Employee("John Doe");
//        Employee jane = new Employee("Jane Doe");
//        Employee jack = new Employee("Jack Doe");
//
//        employeeDao.saveEmployee(john);
//        employeeDao.saveEmployee(jane);
//        employeeDao.saveEmployee(jack);
//
//        Project java = new Project("Java");
//        Project dotNet = new Project("dotNet");
//        Project python = new Project("Python");
//
//        java.addEmployee(john);
//        java.addEmployee(jane);
//        python.addEmployee(john);
//        python.addEmployee(jack);
//        dotNet.addEmployee(jack);
//
//        projectDao.saveProject(java);
//        projectDao.saveProject(python);
//        projectDao.saveProject(dotNet);
//
//        Project project = projectDao.findProjectByNameWithEmployees("Java");
////        System.out.println(project);
//        assertEquals(Set.of("John Doe", "Jane Doe"),
//                project.getEmployees().stream().map(Employee::getName).collect(Collectors.toSet()));
//    }

    @Test
    void saveThanFindTest() {
        Project java = new Project("Java");

        java.getEmployees().put("java_1", new Employee("c123", "John Doe"));
        java.getEmployees().put("java_2", new Employee("c456", "Jane Doe"));

        projectDao.saveProject(java);

        Project anotherProject = projectDao.findProjectByIdWithEmployees(java.getId());
        assertEquals("Jane Doe", anotherProject.getEmployees().get("java_2").getName());
    }
}