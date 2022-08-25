package com.webapi.webapi.model.candidate;

import com.webapi.webapi.model.NonExistentEntityException;

import java.io.Serial;

public class NonExistentCandidateException extends NonExistentEntityException {

    @Serial
    private static final long serialVersionUID = 8633588908169766368L;

    public NonExistentCandidateException() {
        super("Candidate does not exist");
    }
}