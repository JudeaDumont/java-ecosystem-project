package com.webapi.webapi.databasedrivers.hibernateinmemory.services;

import com.webapi.webapi.databasedrivers.CandidateDao;
import com.webapi.webapi.databasedrivers.hibernateinmemory.HibernateConnection;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateCandidate")
public class HibernateCandidateDaoService implements CandidateDao<Candidate, Long, NonExistentCandidateException> {

    //todo: notice the wetness of these methods, need a method we can pass a lambda into and refactor this file to be 1/5
    //todo: whole project should be checked for wetness, especially tests

    @Override
    public int save(Candidate candidate) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.persist(candidate);
        session.getTransaction().commit();
        session.close();
        return 1;
    }

    @Override
    public Long saveReturnID(Candidate candidate) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.persist(candidate);
        session.getTransaction().commit();
        session.close();
        return candidate.getId();
    }

    @Override
    public int delete(Candidate candidate) throws NonExistentCandidateException {
        Candidate exists = get(candidate.getId());
        if (exists == null) {
            return 0;
        }

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.remove(candidate);
        session.getTransaction().commit();
        session.close();
        return 1;
    }

    @Override
    public int update(Candidate candidate) throws NonExistentCandidateException {
        Candidate exists = get(candidate.getId());
        if (exists == null) {
            return 0;
        }

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.merge(candidate);
        session.getTransaction().commit();
        session.close();
        return 1;
    }

    @Override
    public Candidate get(Long id) throws NonExistentCandidateException {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        Candidate retrieved = session.get(Candidate.class, id);
        session.getTransaction().commit();
        session.close();
        return retrieved;
    }

    @Override
    public List<Candidate> getByName(String name) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        Query<Candidate> query = session.createQuery("from Candidate where name=:name", Candidate.class);
        query.setParameter("name", name);
        List<Candidate> candidates = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return candidates;
    }

    @Override
    public List<Candidate> getAll() {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        Query<Candidate> tQuery = session.createQuery(
                "from Candidate c", Candidate.class);
        List<Candidate> resultList = tQuery.getResultList();

        session.close();
        return resultList;
    }
}
