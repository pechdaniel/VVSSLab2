package validation;

import domain.Student;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import sun.misc.Regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        Pattern r1 = Pattern.compile("[0-9]");
        Matcher m1 = r1.matcher(entity.getID());
        if(entity.getID().equals("") || !m1.find()){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getID() == null || entity.getID().length() == 0 || Integer.parseInt(entity.getID())<0){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getNume() == ""){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getGrupa() <= 110 || entity.getGrupa() >937) {
            throw new ValidationException("Grupa incorecta!");
        }
        if(entity.getEmail() == null || !entity.getEmail().contains("@")){
            throw new ValidationException("Email incorect!");
        }
        Pattern r = Pattern.compile("[ a-zA-Z]");
        Matcher m = r.matcher(entity.getNume());
        if(entity.getNume() == null || entity.getNume().length() == 0 || !m.find()){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
    }
}
