package org.example.repository;

import org.example.domain.*;
import org.example.validation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NotaRepository extends AbstractCRUDRepository<Pair<String, String>, Nota> {
    public NotaRepository(Validator<Nota> validator) {
        super(validator);
    }
}
