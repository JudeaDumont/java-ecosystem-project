package com.webapi.webapi.databasedrivers.hibernateinmemory.services;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.databasedrivers.hibernateinmemory.HibernateConnection;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hibernateCandidate")
public class HibernateCandidateDaoService implements Dao<Candidate, Long, NonExistentCandidateException> {

    @Override
    public Long save(Candidate candidate) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        Long saved = (Long) session.save(candidate);
        session.getTransaction().commit();
        session.close();
        return saved;
    }

    @Override
    public boolean delete(Candidate candidate) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.delete(candidate);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Candidate candidate) {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        session.update(candidate);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public Candidate get(Long id) throws NonExistentCandidateException {

        Session session = HibernateConnection.getSession();
        session.beginTransaction();
        Candidate retrieved = session.get(Candidate.class, id);
        session.getTransaction().commit();
        session.close();
        return retrieved;
    }

    @Override
    public List<Candidate> getAll() {
        Session session = HibernateConnection.getSession();
        session.beginTransaction();

        Query tQuery = session.createQuery(
                "from Candidate c");
        List resultList = tQuery.getResultList();

        session.close();
        return resultList;
    }
}
