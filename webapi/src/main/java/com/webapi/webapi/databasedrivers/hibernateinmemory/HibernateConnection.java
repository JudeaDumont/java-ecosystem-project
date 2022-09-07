package com.webapi.webapi.databasedrivers.hibernateinmemory;

import com.webapi.webapi.model.ID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateConnection {
    private HibernateConnection() {
    }

    private static SessionFactory hibernateSessionFactory = null;

    public static Session getSession() {
        if (hibernateSessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            try {
                hibernateSessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
        return hibernateSessionFactory.openSession();
    }

    public static <T> void genericSave(T obj) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.persist(obj);
        session.getTransaction().commit();
        session.close();
    }

    public static <T> T genericGetByClassAndID(Class<T> classOfObj, Long id) throws ClassNotFoundException {

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
}
