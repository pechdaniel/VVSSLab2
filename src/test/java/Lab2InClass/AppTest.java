package Lab2InClass;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{
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

    private void clearAllTemas(){
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
        clearAllTemas();
        clearAllStudents();
    }

    @Test
    public void testTemaValid() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 3;
        Tema t = new Tema("1", "description", deadlineWeek, deliverWeek);
        assertNull(service.findTema("1"));
        service.addTema(t);
        assertNotNull(service.findTema("1"));
    }

    @Test(expected = ValidationException.class)
    public void testTemaIdNull() {
        Integer deadlineWeek = 4;
        Integer deliverWeek = 3;
        Tema t = new Tema(null, "description", deliverWeek, deadlineWeek);
        assertNull(service.findTema(null));
        service.addTema(t);
    }

    @Test(expected = ValidationException.class)
    public void testTemaIdSmallerThan1() {
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void studentValid() {
        Student stud = new Student("67", "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("67"));
        service.addStudent(stud);
        assertNotNull(service.findStudent("67"));
    }

    @Test
    public void studentIdNull() {

        Student stud = new Student(null, "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.addStudent(stud));
    }

    @Test(expected = ValidationException.class)
    public void studentIdEmpty() {
        String id = "";
        Student stud = new Student(id, "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent(id));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void studentIdNegative() {

        Student stud = new Student("-2", "Dani", 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("-2"));
        service.addStudent(stud);
    }

    //@Test(expected = ValidationException.class)
    //public void studentIdNaN() {
    //    String id = "not";
    //    Student stud = new Student(id, "Dani", 935, "peie2390@scs.ubbcluj.com");
//
    //    assertNull(service.findStudent(id));
    //    service.addStudent(stud);
    //}

    //@Test(expected = ValidationException.class)
    //public void studentNameNotValid() {
    //    Student stud = new Student("35", ":;;:", 935, "peie2390@scs.ubbcluj.com");
//
    //    assertNull(service.findStudent("35"));
    //    service.addStudent(stud);
    //}

    @Test
    public void studentNameNull() {
        Student stud = new Student("47", null, 935, "peie2390@scs.ubbcluj.com");

        assertNull(service.addStudent(stud));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void studentGroupNegativeNumber() {
        Student stud = new Student("35", "Dani", -2, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void studentGroupInvalid() {
        Student stud = new Student("35", "Dani", 11, "peie2390@scs.ubbcluj.com");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void studentEmailInvalid() {
        Student stud = new Student("35", "Dani", 935, "ii");

        assertNull(service.findStudent("35"));
        service.addStudent(stud);
    }

    @Test
    public void studentEmailNull() {
        Student stud = new Student("35", "Dani", 935, null);
        assertNull(service.addStudent(stud));
        service.addStudent(stud);
    }

    @Test(expected = ValidationException.class)
    public void studentEmailEmpty() {

        Student stud = new Student("67", "Dani", 935, "");
        assertNull(service.findStudent("67"));
        service.addStudent(stud);
    }

    @After
    public void clearTests() {
        clearAllGrades();
        clearAllTemas();
        clearAllStudents();
    }
//
    //@Test
    //public void TestAddStudent(){
    //    this.CreateService();
//
    //    clearAllStudents();
//
    //    this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
//
    //    assertEquals("Nicolae", this.service.findStudent("690").getNume());
    //}
//
    //@Test
    //public void TestAddStudentWhiteBoxForIntergration(){
    //    this.CreateService();
//
    //    clearAllStudents();
//
    //    this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
//
    //    assertEquals("Nicolae", this.service.findStudent("690").getNume());
    //}
//
    //@Test
    //public void TestAddTemaWhiteBoxForIntergration(){
    //    this.CreateService();
//
    //    clearAllTemas();
//
    //    this.service.addTema(new Tema("2","Tema", 2, 3));
//
    //    assertEquals("Tema", this.service.findTema("2").getDescriere());
    //}
//
    //@Test
    //public void TestAddGradeWhiteBoxForIntergration(){
    //    this.CreateService();
//
    //    clearAllStudents();
    //    clearAllTemas();
    //    clearAllGrades();
//
    //    List<Tema> lst1 = new ArrayList<Tema>((Collection<? extends Tema>) service.getAllTeme());
    //    List<Nota> lst2 = new ArrayList<Nota>((Collection<? extends Nota>) service.getAllNote());
    //    List<Student> lst3 = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());
//
//
    //    this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
    //    this.service.addTema(new Tema("2","Tema", 3, 2));
    //    Nota nota = new Nota("2","690", "2", 10, LocalDate.now());
    //    nota.setID(nota.getIdTema());
    //    this.service.addNota(nota, "De nota 10");
//
    //    assertEquals("690", this.service.findNota("2").getIdStudent());
    //}
//
    //@Test
    //public void TestBigBang(){
    //    TestAddStudentWhiteBoxForIntergration();
    //    TestAddTemaWhiteBoxForIntergration();
    //    TestAddGradeWhiteBoxForIntergration();
    //}
//
    //@Test
    //public void TestAddStudentWhiteBoxForIntergrationHomework(){
    //    this.CreateService();
//
    //    clearAllStudents();
//
    //    this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
//
    //    assertEquals("Nicolae", this.service.findStudent("690").getNume());
    //}
//
    //@Test
    //public void TestAddTemaIntergrationHomework(){
    //    this.CreateService();
//
    //    clearAllTemas();
//
    //    TestAddStudentWhiteBoxForIntergrationHomework();
    //    this.service.addTema(new Tema("1","Tema", 2, 3));
//
    //    assertEquals("Tema", this.service.findTema("1").getDescriere());
    //}
//
    //@Test
    //public void TestAddGradeIntergrationHomework(){
    //    this.CreateService();
//
    //    clearAllGrades();
//
    //    TestAddStudentWhiteBoxForIntergrationHomework();
    //    TestAddTemaWhiteBoxForIntergration();
    //    Nota nota = new Nota("2","690", "2", 10, LocalDate.now());
    //    nota.setID(nota.getIdTema());
    //    this.service.addNota(nota, "De nota 10");
//
    //    assertEquals("690", this.service.findNota("2").getIdStudent());
    //}
//
    //@Test
    //public void TestIntegrationHomework(){
    //    TestAddStudentWhiteBoxForIntergrationHomework();
    //    TestAddTemaIntergrationHomework();
    //    TestAddGradeIntergrationHomework();
    //}
//
    //@Test
    //public void TestAddStudentAlreadyExists(){
    //    this.CreateService();
//
    //    List<Student> lst = new ArrayList<Student>((Collection<? extends Student>) service.getAllStudenti());
    //    lst.forEach(s->service.deleteStudent(s.getID()));
//
    //    this.service.addStudent(new Student("690","Nicolae", 935, "george@snitel.com"));
//
    //    this.service.addStudent(new Student("690","George", 935, "nicolae@snitel.com"));
//
    //    assertNotEquals("George", this.service.findStudent("690").getNume());
    //}
//
    //@Test
    //public void TestStudentHasInvalidGroup(){
    //    StudentValidator sv = new StudentValidator();
    //    boolean hasError = false;
    //    try {
    //        sv.validate(new Student("690", "Mihai", -1, "george@snitel.com"));
    //    }catch (ValidationException ex){
    //        hasError = true;
    //    }
//
    //    assertTrue(hasError);
    //}
//
    //private void removeAllTeme(){
    //    List<Tema> lst = new ArrayList<Tema>((Collection<? extends Tema>) service.getAllTeme());
    //    lst.forEach(s->service.deleteTema(s.getID()));
    //}
//
    //@Test
    //public void TestConditionsAddTemaThrowsException(){
    //    CreateService();
    //    List<Tema> teme = new ArrayList<>();
    //    teme=addTemeErrorToList(teme);
//
    //    for (Tema t:teme){
    //        assertFalse(TestAddTema(t));
    //    }
    //}
//
    //@Test
    //public void TestConditionsAddTemaWorks(){
    //    CreateService();
    //    List<Tema> teme = new ArrayList<>();
    //    teme=addTemeGoodToList(teme);
//
    //    for (Tema t:teme){
    //        assertTrue(TestAddTema(t));
    //    }
    //}
//
    //private List<Tema> addTemeErrorToList(List<Tema> teme){
    //    teme.add(new Tema("","Desc",12,13));//empty tema id
    //    teme.add(new Tema("10","",12,13));//empty description
    //    teme.add(new Tema("10","Desc",0,13));//deadline < 1
    //    teme.add(new Tema("10","Desc",12,15));//deadline >14
    //    teme.add(new Tema("10","Desc",0,13));//rimire < 1
    //    teme.add(new Tema("10","Desc",12,15));//primire >14
//
    //    return teme;
    //}
//
    //private List<Tema> addTemeGoodToList(List<Tema> teme){
    //    teme.add(new Tema("10","Desc",12,13));
//
    //    return teme;
    //}
//
    //private boolean TestAddTema(Tema assignment){
    //    removeAllTeme();
//
    //    boolean worked;
    //    try{
    //        service.addTema(assignment);
    //        worked=true;
    //    }catch (ValidationException ex){
    //        worked=false;
    //    }
//
    //    return worked;
    //}
}
