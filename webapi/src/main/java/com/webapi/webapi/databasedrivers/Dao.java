package com.webapi.webapi.databasedrivers;

import com.webapi.webapi.model.NonExistentEntityException;

import java.util.Collection;

// This is essentially the standardization by which an object mapper (to/from serialized objects) is implemented
public interface Dao<T, I, E extends NonExistentEntityException> {
    T get(I id) throws E;

    Collection<T> getAll();

    int save(T t);

    int update(T t);

    int delete(T t);
}