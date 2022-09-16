package com.webapi.webapi.databasedrivers;

import com.webapi.webapi.model.NonExistentEntityException;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;

import java.util.Collection;
import java.util.List;

// This is essentially the standardization by which an object mapper (to/from serialized objects) is implemented
public interface CandidateDao<T, I, E extends NonExistentEntityException> {
    T get(I id) throws E, DuplicatePrimaryKeyException;

    Collection<T> getAll();

    int save(T t);

    Long saveReturnID(T t);

    List<Candidate> getByName(String name);

    int update(T t) throws NonExistentCandidateException;

    int delete(T t) throws NonExistentCandidateException;
}