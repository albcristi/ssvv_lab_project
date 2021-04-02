package org.example;

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

import static org.junit.Assert.assertEquals;

public class AppTestLab3 {

    private Service service;
    private StudentXMLRepository studentXMLRepository;
    private TemaXMLRepository fileRepository2;
    private String invalidIdString;
    private String validId;

    private String description;
    private Integer deadlineGood;
    private Integer startLineGood;


    @Before
    public void beforeTest() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        service = new Service(studentXMLRepository, fileRepository2, fileRepository3);
        invalidIdString = "used id";
        validId = "super id";
        description = "super tema";
        deadlineGood = 10;
        startLineGood = 9;
    }

    @Test
    public void firstTestWbt(){
        // cover path: 1 , 2, 3, 4 , 6
        if(fileRepository2.findOne(validId)!=null){
            fileRepository2.delete(validId);
        }
        assertEquals(1, service.saveTema(validId, description, deadlineGood, startLineGood));
    }

    @Test
    public void secondTestWbt(){
        // cover path: 1, 2 , 3, 5, 6
        if(fileRepository2.findOne(invalidIdString) == null){
            fileRepository2.save(new Tema(invalidIdString, description, deadlineGood, startLineGood));
        }
        assertEquals(0, service.saveTema(invalidIdString, description, deadlineGood, startLineGood));
    }

    @Test
    public void thirdTestWbt(){
        if(fileRepository2.findOne(validId)!=null){
            fileRepository2.delete(validId);
        }
        assertEquals(0, service.saveTema(validId, description, startLineGood, deadlineGood));
    }

    @Test
    public void fourthTestWbt(){
        if(fileRepository2.findOne(validId)!=null){
            fileRepository2.delete(validId);
        }
        assertEquals(1, service.saveTema(validId, description, deadlineGood, deadlineGood));
    }

    @Test
    public void fifthTestWbt(){
        if(fileRepository2.findOne(validId)!=null){
            fileRepository2.delete(validId);
        }
        assertEquals(0, service.saveTema(validId, null, deadlineGood, deadlineGood));
    }

    @Test
    public void sixthTestWbt(){
        assertEquals(0, service.saveTema("", description, deadlineGood, deadlineGood));
    }
}
