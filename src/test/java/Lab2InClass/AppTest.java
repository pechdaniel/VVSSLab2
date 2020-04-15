package Lab2InClass;

import domain.Nota;
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

import java.time.LocalDate;
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

    @Test
    public void TestAddStudent(){
        this.CreateService();

        clearAllStudents();

        this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));

        assertEquals("Nicolae", this.service.findStudent("690").getNume());
    }

    @Test
    public void TestAddStudentWhiteBoxForIntergration(){
        this.CreateService();

        clearAllStudents();

        this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));

        assertEquals("Nicolae", this.service.findStudent("690").getNume());
    }

    @Test
    public void TestAddAssignmentWhiteBoxForIntergration(){
        this.CreateService();

        clearAllAssignments();

        this.service.addTema(new Tema("1","Tema", 2, 3));

        assertEquals("Tema", this.service.findTema("1").getDescriere());
    }

    @Test
    public void TestAddGradeWhiteBoxForIntergration(){
        this.CreateService();

        clearAllStudents();
        clearAllAssignments();
        clearAllGrades();

        List<Tema> lst1 = new ArrayList<Tema>((Collection<? extends Tema>) service.getAllTeme());
        List<Nota> lst2 = new ArrayList<Nota>((Collection<? extends Nota>) service.getAllNote());
        List<Student> lst3 = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());


        this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
        this.service.addTema(new Tema("2","Tema", 3, 2));
        Nota nota = new Nota("2","690", "2", 10, LocalDate.now());
        nota.setID(nota.getIdTema());
        this.service.addNota(nota, "De nota 10");

        assertEquals("690", this.service.findNota("2").getIdStudent());
    }

    @Test
    public void TestBigBang(){
        TestAddStudentWhiteBoxForIntergration();
        TestAddAssignmentWhiteBoxForIntergration();
        TestAddGradeWhiteBoxForIntergration();
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
