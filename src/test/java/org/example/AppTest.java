package org.example;

import static org.junit.Assert.assertEquals;

import org.example.domain.Nota;
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

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private String invalidIdString;
    private String validId;

    private String validStudentName;
    private String invalidStudentNameEmpty;

    private Integer validGroup;
    private Integer invalidGroupUpper;
    private Integer invalidGroupLower;

    @Before
    public void beforeTest(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        service = new Service(studentXMLRepository, fileRepository2, fileRepository3);
        invalidIdString = "";
        validId = "some valid id";

        validStudentName = "some name";
        invalidStudentNameEmpty = "";
        validGroup = 900;
        invalidGroupLower = 109;
        invalidGroupUpper = 939;
    }

    @Test
    public void saveStudent_success_valid_student_id() {
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(1, service.saveStudent(validId, validStudentName, validGroup));
    }

    @Test
    public void saveStudent_fail_invalid_student_id(){
        assertEquals(0,  service.saveStudent(invalidIdString, validStudentName,validGroup));
        assertEquals(0, service.saveStudent(null, validStudentName, validGroup));
    }

    @Test
    public void saveStudent_fail_invalid_student_name(){
        System.out.println("YAY");
    }

    @Test
    public void saveStudent_fail_unique_id(){
        // add valid student
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(1, service.saveStudent(validId, validStudentName, validGroup));
        assertEquals(0, service.saveStudent(validId, validStudentName,validGroup));
    }

    @Test
    public void saveStudent_invalid_name1(){
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(0, service.saveStudent(validId, invalidStudentNameEmpty, validGroup));
    }

    @Test
    public void saveStudent_invalid_name2(){
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(0, service.saveStudent(validId, null, validGroup));
    }

    @Test
    public void saveStudent_invalid_group1(){
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(0, service.saveStudent(validId, validStudentName, invalidGroupLower));
    }

    @Test
    public void saveStudent_invalid_group2(){
        if(studentXMLRepository.findOne(validId) != null)
            studentXMLRepository.delete(validId);
        assertEquals(0, service.saveStudent(validId, validStudentName, invalidGroupUpper));
    }

}
