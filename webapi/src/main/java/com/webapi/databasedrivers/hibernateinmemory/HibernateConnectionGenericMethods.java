package com.webapi.databasedrivers.hibernateinmemory;

import com.webapi.model.ID;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateConnectionGenericMethods {

    //Methods that use runInSession need a reference to the session
    private static Session session = null;

    private HibernateConnectionGenericMethods() {

    }

    private static <T extends ID> String genericGetTableNameFromClass(Class<T> classOfObject) {
        String[] packageLocation = classOfObject.getName().split("\\.");
        return packageLocation[packageLocation.length - 1];
    }

    private interface FeedMeLambdasIllInferTheReturnType<R> {
        R op();
    }

    public static <R> R runInSession(FeedMeLambdasIllInferTheReturnType<R> feedMeLambdasIllInferTheReturnType) {
        session = HibernateConnection.getSession();
        session.beginTransaction();
        R result = feedMeLambdasIllInferTheReturnType.op();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static <T> void genericSave(T obj) {
        runInSession(() -> {
            session.persist(obj);
            return null;
        });
    }

    public static <T extends ID> Long genericSaveReturnID(T obj) {
        runInSession(() -> {
            session.persist(obj);
            return null;
        });
        //todo: hibernate knows to cram the object with the ID that gets generated, we have to tell the sql result from postgres to do this
        return obj.getId();
    }

    public static <T> T genericGet(Class<T> classOfObj, Long id) {
        return runInSession(() -> session.get(classOfObj, id));
    }

    public static <T extends ID> List<T> genericGetByName(Class<T> classOfObject, String name) {
        return runInSession(() -> {
            Query<T> tQuery = session.createQuery(
                    "from " + genericGetTableNameFromClass(classOfObject) + " c where c.name = '" + name + "'", classOfObject);

            return tQuery.getResultList();
        });
    }

    public static <T extends ID> void genericUpdate(T obj) {
        runInSession(() -> {
            session.merge(obj);
            return null;
        });
    }

    public static <T extends ID> void genericDelete(T obj) {
        runInSession(() -> {
            session.remove(obj);
            return null;
        });
    }

    public static <T extends ID> List<T> genericGetAll(Class<T> classOfObject) {
        return runInSession(() -> {
            Query<T> tQuery = session.createQuery(
                    "from " + genericGetTableNameFromClass(classOfObject), classOfObject);

            return tQuery.getResultList();
        });
    }
}
