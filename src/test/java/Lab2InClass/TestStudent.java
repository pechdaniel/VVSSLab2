package Lab2InClass;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestStudent {

    Service service;

    private void CreateService(){
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    private void clearAllAssignments(){
        List<Tema> lst = new ArrayList<Tema>((Collection<? extends Tema>) service.getAllTeme());
        lst.forEach(a->service.deleteTema(a.getID()));
    }

    private void clearAllGrades(){
        List<Nota> lst = new ArrayList<Nota>((Collection<? extends Nota>) service.getAllNote());
        lst.forEach(g->{if(g.getID() != null && !g.getID().isEmpty())service.deleteNota(g.getID());});
        lst = new ArrayList<Nota>((Collection<? extends Nota>) service.getAllNote());
    }

    private void clearAllStudents(){
        List<Student> lst = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());
        lst.forEach(s->service.deleteStudent(s.getID()));
    }
    
    @Before
    public void init(){
        CreateService();
        clearAllGrades();
        clearAllAssignments();
        clearAllStudents();
    }

    @Test
    public void testAddStudentValid() {
        Student stud = new Student("67", "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("67"));
        service.addStudent(stud);
        assertNotNull(service.findStudent("67"));
    }

    @Test
    public void testAddStudentIdNull() {

        Student stud = new Student(null, "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.addStudent(stud));
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentIdEmpty() {
        String id = "";
        Student stud = new Student(id, "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent(id));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentIdNegative() {

        Student stud = new Student("-2", "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("-2"));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentIdNaN() {
        String id = "not";
        Student stud = new Student(id, "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent(id));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentNameNotValid() {
        Student stud = new Student("35", ":;;:", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test
    public void testAddStudentNameNull() {
        Student stud = new Student("47", null, 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.addStudent(stud));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentGroupNegativeNumber() {
        Student stud = new Student("35", "Dani", -2, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentGroupInvalid() {
        Student stud = new Student("35", "Dani", 11, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void addStudentEmailInvalid() {
        Student stud = new Student("35", "Dani", 935, "ii");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test
    public void addStudentEmailNull() {
        Student stud = new Student("35", "Dani", 935, null);
        assertNull(service.addStudent(stud));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void testAddStudentEmailEmpty() {

        Student stud = new Student("67", "Dani", 935, "");
        assertNull(service.findStudent("67"));
        service.addStudent(stud);
    }

    @After
    public void clearTests() {
        clearAllGrades();
        clearAllAssignments();
        clearAllStudents();
    }
}
