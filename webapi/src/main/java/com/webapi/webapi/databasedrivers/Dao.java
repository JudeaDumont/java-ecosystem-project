package com.webapi.webapi.databasedrivers;

import com.webapi.webapi.model.NonExistentEntityException;

import java.util.Collection;

// This is used to standardize the way DB objects are imported/exported from java
// Would be unnecessary if there was an object mapper
// This is essentially the standardization by which an object mapper is implemented
public interface Dao<T, I, E extends NonExistentEntityException> {
    T get(I id) throws E;

    Collection<T> getAll();

    int save(T t);

    int update(T t);

    int delete(T t);
}