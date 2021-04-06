package org.example;

import org.example.domain.Nota;
import org.example.domain.Pair;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepository;
import org.example.repository.StudentXMLRepository;
import org.example.repository.TemaXMLRepository;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class AppTestLab4 {

    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository temaXMLRepository;
    private NotaXMLRepository notaXMLRepository;
    private String validStudentId;
    private String validStudentName;
    private Integer validStudentGroup;

    private String validAssignmentId;
    private String validAssignmentDescription;
    private Integer validAssignmentDeadline;
    private Integer getValidAssignmentStartLine;



    @Before
    public void beforeTest() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        temaXMLRepository = new TemaXMLRepository(temaValidator, "teme.xml");
        notaXMLRepository = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);

        validStudentId = "student valid id for test";
        validStudentName = "test student name";
        validStudentGroup = 921;

        validAssignmentId = "assignment valid id for test";
        validAssignmentDescription = "test big bang assignment";
        validAssignmentDeadline = 10;
        getValidAssignmentStartLine = 5;
    }


    @Test
    public void addStudentOk(){
        try {
            if (studentXMLRepository.findOne(validStudentId) != null)
                studentXMLRepository.delete(validStudentId);
            assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
        }
        catch (Exception e){
            assert false;
        }
    }

    @Test
    public void addAssignmentOk(){
       try{
           if(temaXMLRepository.findOne(validAssignmentId) !=  null)
               temaXMLRepository.delete(validAssignmentId);
           assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription, validAssignmentDeadline,
                   getValidAssignmentStartLine));
       }
       catch (Exception e){
           assert  false;
       }
    }

    @Test
    public void addGradeOk(){
        try{
            assertEquals(0, service.saveNota(null, null, 10, 6, ""));
        }
        catch (Exception e){
            assert false;
        }
    }

    @Test
    public void integrationTestOk(){
        try{
            if (studentXMLRepository.findOne(validStudentId) != null)
                studentXMLRepository.delete(validStudentId);
            assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
            if(temaXMLRepository.findOne(validAssignmentId) !=  null)
                temaXMLRepository.delete(validAssignmentId);
            assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription, validAssignmentDeadline,
                    getValidAssignmentStartLine));
            if(notaXMLRepository.findOne(new Pair<>(validStudentId, validAssignmentId))!=null)
                notaXMLRepository.delete(new Pair<>(validStudentId, validAssignmentId));
            assertEquals(1, service.saveNota(validStudentId, validAssignmentId, 9, 6, "aa"));
        }
        catch (Exception e){
            assert false;
        }
    }
}
