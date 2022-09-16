package com.webapi.databasedrivers;

import com.webapi.model.candidate.Candidate;

import java.util.Collection;
import java.util.List;

// This is essentially the standardization by which an object mapper (to/from serialized objects) is implemented
public interface CandidateDao<T, I> {
    T get(I id) throws DuplicatePrimaryKeyException;

    Collection<T> getAll();

    int save(T t);

    Long saveReturnID(T t);

    List<Candidate> getByName(String name);

    int update(T t);

    int delete(T t);
}