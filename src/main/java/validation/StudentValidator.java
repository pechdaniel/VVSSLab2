package validation;

import domain.Student;
public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getID().equals("")){
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
        if(entity.getNume() == null || entity.getNume().length() == 0){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Email incorect!");
        }
    }
}
