package com.webapi.databasedrivers;

import java.util.Collection;
import java.util.List;

// This is essentially the standardization by which an object mapper (to/from serialized objects) is implemented
// I suppose the reason for keeping this generic is that it would be easy to copy into another dao for another model
public interface CandidateDao<T, I> {
    T get(I id) throws DuplicatePrimaryKeyException;

    Collection<T> getAll();

    int save(T t);

    Long saveReturnID(T t);

    List<T> getByName(String name);

    int update(T t);

    int delete(T t);
}