package data.hibernateinmemory;

import data.enforcedclassextension.ID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateInMemorySessionFactory {
    private HibernateInMemorySessionFactory() {
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

    // does not return an EnforcedClassExtension.ID of the thing you just saved
    // you shouldn't need the id after saving the thing, you should need it before
    public static <T> void save(T obj) {
        Session session = HibernateInMemorySessionFactory.getSession();
        session.beginTransaction();
        //todo: figure out what the non-deprecated version of this is in hibernate docs
        session.save(obj);
        session.getTransaction().commit();
        session.close();
    }

    public static <T> T getByClassAndID(Class<T> classOfObj, Long id) throws ClassNotFoundException {

        Session session = HibernateInMemorySessionFactory.getSession();
        session.beginTransaction();
        T obj = session.get(classOfObj, id);
        session.getTransaction().commit();
        session.close();
        return obj;
    }

    public static <T extends ID> List<T> getByName(Class<T> classOfObject, String name) {
        Session session = HibernateInMemorySessionFactory.getSession();
        session.beginTransaction();

        Query<T> tQuery = session.createQuery(
                "from " + getTableNameFromClass(classOfObject) + " c where c.name = '" + name + "'", classOfObject);

        return tQuery.getResultList();
    }

    private static <T extends ID> String getTableNameFromClass(Class<T> classOfObject) {
        String[] packageLocation = classOfObject.getName().split("\\.");
        return packageLocation[packageLocation.length - 1];
    }
}
