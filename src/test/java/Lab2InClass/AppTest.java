package Lab2InClass;

import domain.Student;
import domain.Tema;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentFileRepository;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;
import view.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    private Service service;

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

    @Test
    public void TestAddStudent(){
        this.CreateService();

        List<Student> lst = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());
        lst.forEach(s->service.deleteStudent(s.getID()));

        this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));

        assertEquals("Nicolae", this.service.findStudent("690").getNume());
    }

    @Test
    public void TestAddStudentAlreadyExists(){
        this.CreateService();

        List<Student> lst = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());
        lst.forEach(s->service.deleteStudent(s.getID()));

        this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));

        this.service.addStudent(new Student("690","George", 935, "nicolae@snitel.com"));

        assertNotEquals("George", this.service.findStudent("690").getNume());
    }

    @Test
    public void TestStudentHasInvalidGroup(){
        StudentValidator sv = new StudentValidator();
        boolean hasError = false;
        try {
            sv.validate(new Student("690", "Mihai", -1, "george@snitel.com"));
        }catch (ValidationException ex){
            hasError = true;
        }

        assertTrue(hasError);
    }

    private void removeAllTeme(){
        List<Tema> lst = new ArrayList<Tema>((Collection<? extends Tema>) service.getAllTeme());
        lst.forEach(s->service.deleteTema(s.getID()));
    }

    @Test
    public void TestConditionsAddAssignmentThrowsException(){
        CreateService();
        List<Tema> teme = new ArrayList<>();
        teme=addTemeErrorToList(teme);

        for (Tema t:teme){
            assertFalse(TestAddAssignment(t));
        }
    }

    @Test
    public void TestConditionsAddAssignmentWorks(){
        CreateService();
        List<Tema> teme = new ArrayList<>();
        teme=addTemeGoodToList(teme);

        for (Tema t:teme){
            assertTrue(TestAddAssignment(t));
        }
    }

    private List<Tema> addTemeErrorToList(List<Tema> teme){
        teme.add(new Tema("","Desc",12,13));//empty tema id
        teme.add(new Tema("10","",12,13));//empty description
        teme.add(new Tema("10","Desc",0,13));//deadline < 1
        teme.add(new Tema("10","Desc",12,15));//deadline >14
        teme.add(new Tema("10","Desc",0,13));//rimire < 1
        teme.add(new Tema("10","Desc",12,15));//primire >14

        return teme;
    }

    private List<Tema> addTemeGoodToList(List<Tema> teme){
        teme.add(new Tema("10","Desc",12,13));

        return teme;
    }

    private boolean TestAddAssignment(Tema assignment){
        removeAllTeme();

        boolean worked;
        try{
            service.addTema(assignment);
            worked=true;
        }catch (ValidationException ex){
            worked=false;
        }

        return worked;
    }
}
