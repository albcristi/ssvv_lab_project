package org.example;

import org.example.domain.Nota;
import org.example.domain.Pair;
import org.example.domain.Student;
import org.example.domain.Tema;
import org.example.repository.NotaXMLRepository;
import org.example.repository.StudentXMLRepository;
import org.example.repository.TemaXMLRepository;
import org.example.service.Service;
import org.example.validation.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class IncrementalTest {
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

    // Test Add Student features
    @Test
    public void addStudentIncremental(){
        try{
            if(studentXMLRepository.findOne(validStudentId)!=null)
                studentXMLRepository.delete(validStudentId);

            assertEquals(0, service.saveStudent(validStudentId, validStudentName, -100));
        }
        catch (Exception e){
            assert false;
        }
    }

    // Test Add Assignment Incremental (add student + add assignment)
    @Test
    public void addAssignmentIncremental(){
        try{
            if(studentXMLRepository.findOne(validStudentId)!=null)
                studentXMLRepository.delete(validStudentId);
            assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
            if(temaXMLRepository.findOne(validAssignmentId)!=null)
                temaXMLRepository.delete(validAssignmentId);
            assertEquals(0, service.saveTema(validAssignmentId, validAssignmentDescription, 10, 11));
        }
        catch (Exception e){
            assert false;
        }
    }

    // Test Add Grade Incremental (add student + add assignment + add grade)
    @Test
    public void addGradeIncremental(){
//        try{
            if(studentXMLRepository.findOne(validStudentId)!=null)
                studentXMLRepository.delete(validStudentId);
            assertEquals(1, service.saveStudent(validStudentId, validStudentName, validStudentGroup));
            if(temaXMLRepository.findOne(validAssignmentId)!=null)
                temaXMLRepository.delete(validAssignmentId);
            assertEquals(1, service.saveTema(validAssignmentId, validAssignmentDescription,
                    validAssignmentDeadline, getValidAssignmentStartLine));
            if(notaXMLRepository.findOne(new Pair<>(validStudentId, validAssignmentId))!=null)
                notaXMLRepository.delete(new Pair<>(validStudentId, validAssignmentId));
            assertEquals(1, service.saveNota(validStudentId, validAssignmentId, 10,
                    getValidAssignmentStartLine, "no bravo baiatu :))"));
//        }
//        catch (Exception e){
//            assert false;
//        }
    }
}
