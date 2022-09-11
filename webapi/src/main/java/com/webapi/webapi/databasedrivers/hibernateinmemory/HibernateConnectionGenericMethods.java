package com.webapi.webapi.databasedrivers.hibernateinmemory;

import com.webapi.webapi.model.ID;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

//todo: refactor methods to use a lambda that opens and closes a session before and after it
public class HibernateConnectionGenericMethods {
    private HibernateConnectionGenericMethods() {
    }

    public static <T> void genericSave(T obj) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.persist(obj);
        session.getTransaction().commit();
        session.close();
    }

    public static <T extends ID> Long genericSaveReturnID(T obj) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.persist(obj);
        session.getTransaction().commit();
        session.close();
        return obj.getId();
    }

    public static <T> T genericGet(Class<T> classOfObj, Long id) throws ClassNotFoundException {

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        T obj = session.get(classOfObj, id);
        session.getTransaction().commit();
        session.close();
        return obj;
    }

    public static <T extends ID> List<T> genericGetByName(Class<T> classOfObject, String name) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();

        Query<T> tQuery = session.createQuery(
                "from " + genericGetTableNameFromClass(classOfObject) + " c where c.name = '" + name + "'", classOfObject);

        List<T> resultList = tQuery.getResultList();
        session.close();
        return resultList;
    }

    private static <T extends ID> String genericGetTableNameFromClass(Class<T> classOfObject) {
        String[] packageLocation = classOfObject.getName().split("\\.");
        return packageLocation[packageLocation.length - 1];
    }

    public static <T extends ID> void genericUpdate(T object) {

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.merge(object);
        session.getTransaction().commit();
        session.close();
    }

    public static <T extends ID> void genericDelete(T object) {

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.remove(object);
        session.getTransaction().commit();
        session.close();
    }

    public static <T extends ID> List<T> genericGetAll(Class<T> classOfObject) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();

        Query<T> tQuery = session.createQuery(
                "from " + genericGetTableNameFromClass(classOfObject), classOfObject);

        List<T> resultList = tQuery.getResultList();
        session.close();
        return resultList;
    }
}
