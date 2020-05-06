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

public class TestAssignment {

    ///Homework lab3WBT

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
    public void testAssignmentValid() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 3;
        Tema t = new Tema("1", "description", deadlineWeek, deliverWeek);
        assertNull(service.findTema("1"));
        service.addTema(t);
        assertNotNull(service.findTema("1"));
    }

    @Test(expected = ValidationException.class)
    public void testAssignmentIdNull() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 3;
        Tema t = new Tema(null, "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema(null));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testAssignmentIdSmallerThan1() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 3;
        Tema t = new Tema("-2", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("-1"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeadlineWeekBiggerThan14() {
        Integer deadlineWeek = 15;
        Integer deliverWeek = 3;
        Tema t = new Tema("2", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("2"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeadlineWeekSmallerThan1() {
        Integer deadlineWeek = -1;
        Integer deliverWeek = 3;
        Tema t = new Tema("2", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("2"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeadlineWeekSmallerThanDeliverWeek() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 13;
        Tema t = new Tema("2", "description", deadlineWeek, deliverWeek);
        assertNull(service.findTema("3"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeliverWeekBiggerThan14() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 15;
        Tema t = new Tema("2", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("3"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeliverWeekSmallerThan1() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = -1;
        Tema t = new Tema("2", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("3"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDescriptionNull() {
        Integer deadlineWeek = 4;
        Tema t = new Tema("2", null, 1, deadlineWeek);
        assertNull(service.addTema(t));
    }

    @Test(expected = ValidationException.class)
    public void testEmptyDescription() {
        Integer deadlineWeek = 4;
        Tema t = new Tema("2", "", 1, deadlineWeek);
        assertNull(service.findTema("3"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testIdDeadlineInvalid() {
        String id = null;
        Integer deadlineWeek = -4;
        Tema t = new Tema(id, "description", 1, deadlineWeek);
        assertNull(service.findTema(id));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testIdDeliverInvalid() {
        String id = null;
        Integer deadlineWeek = 4;
        Integer deliverWeek = -1;
        Tema t = new Tema(id, "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema(id));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testDeadlineDeliverInvalid() {
        Integer deadlineWeek = -4;
        Integer deliverWeek = -1;
        Tema t = new Tema("1", "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema("1"));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testAllInvalid() {
        String id = null;
        Tema t = new Tema(id, "", 7, 4);
        assertNull(service.findTema(id));
        service.addTema(t);
    }

    @After
    public void clearTests() {
        clearAllGrades();
        clearAllAssignments();
        clearAllStudents();
    }
}
