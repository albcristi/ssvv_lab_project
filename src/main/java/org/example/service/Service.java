package org.example.service;

import org.example.domain.*;
import org.example.repository.*;
import org.example.validation.ValidationException;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Service {
    private StudentXMLRepository studentXmlRepo;
    private TemaXMLRepository temaXmlRepo;
    private NotaXMLRepository notaXmlRepo;

    public Service(StudentXMLRepository studentXmlRepo, TemaXMLRepository temaXmlRepo, NotaXMLRepository notaXmlRepo) {
        this.studentXmlRepo = studentXmlRepo;
        this.temaXmlRepo = temaXmlRepo;
        this.notaXmlRepo = notaXmlRepo;
    }

    public Iterable<Student> findAllStudents() { return studentXmlRepo.findAll(); }

    public Iterable<Tema> findAllTeme() { return temaXmlRepo.findAll(); }

    public Iterable<Nota> findAllNote() { return notaXmlRepo.findAll(); }

    public int saveStudent(String id, String nume, int grupa) {
        Student student = new Student(id, nume, grupa);
        // new approach: use try/catch
        try {
            Student result = studentXmlRepo.save(student);

            if (result == null) {
                return 1;
            }
           return 0;
        }
        catch (ValidationException e){
            return 0;
        }
    }

    public int saveTema(String id, String descriere, int deadline, int startline) { // 1
        Tema tema = new Tema(id, descriere, deadline, startline); // 2
        // NEW APPROACH
        Tema result = null;
        try {
            result = temaXmlRepo.save(tema); // 2
        }
        catch (Exception e){
            return 0;
        }

        if (result == null) { // 3
            return 1; // 4
        }
        return 0; // 5
    } // 6

    public int saveNota(String idStudent, String idTema, double valNota, int predata, String feedback) {
        if(idStudent == null || idTema == null)
            return 0; //new modifications
        if (studentXmlRepo.findOne(idStudent) == null || temaXmlRepo.findOne(idTema) == null) {
            return 0; // error: return -1
        }
        else {
            int deadline = temaXmlRepo.findOne(idTema).getDeadline();
            // ERROR FOR GRADE COMPUTING
            if(predata > deadline+2)
                valNota = 1;
            if(predata>deadline && predata <= deadline+2)
                valNota = valNota -2.5*(predata-deadline);

//            if (predata - deadline > 2) {
//                valNota =  1;
//            } else {
//                valNota =  valNota - 2.5 * (predata - deadline);
//            }
            Nota nota = new Nota(new Pair(idStudent, idTema), valNota, predata, feedback);
            Nota result = null;
            try {
                 result = notaXmlRepo.save(nota);
            }
            catch (ValidationException e){
                return 0;
            }

            if (result == null) {
                return 1;
            }
            return 0;
        }
    }

    public int deleteStudent(String id) {
        Student result = studentXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int deleteTema(String id) {
        Tema result = temaXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateStudent(String id, String numeNou, int grupaNoua) {
        Student studentNou = new Student(id, numeNou, grupaNoua);
        Student result = studentXmlRepo.update(studentNou);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateTema(String id, String descriereNoua, int deadlineNou, int startlineNou) {
        Tema temaNoua = new Tema(id, descriereNoua, deadlineNou, startlineNou);
        Tema result = temaXmlRepo.update(temaNoua);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int extendDeadline(String id, int noWeeks) {
        Tema tema = temaXmlRepo.findOne(id);

        if (tema != null) {
            LocalDate date = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int currentWeek = date.get(weekFields.weekOfWeekBasedYear());

            if (currentWeek >= 39) {
                currentWeek = currentWeek - 39;
            } else {
                currentWeek = currentWeek + 12;
            }

            if (currentWeek <= tema.getDeadline()) {
                int deadlineNou = tema.getDeadline() + noWeeks;
                return updateTema(tema.getID(), tema.getDescriere(), deadlineNou, tema.getStartline());
            }
        }
        return 0;
    }

    public void createStudentFile(String idStudent, String idTema) {
        Nota nota = notaXmlRepo.findOne(new Pair(idStudent, idTema));

        notaXmlRepo.createFile(nota);
    }
}
