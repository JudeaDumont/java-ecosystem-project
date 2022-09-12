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

    //todo: whole project should be checked for wetness, especially tests

    private static Session session = null;

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

    @Override
    public int save(Candidate candidate) {
        return runInSession(() -> {
            session.persist(candidate);
            return 1;
        });
    }

    @Override
    public Long saveReturnID(Candidate candidate) {
        return runInSession(() -> {
            session.persist(candidate);
            return candidate.getId();
        });
    }

    @Override
    public int delete(Candidate candidate) throws NonExistentCandidateException {
        Candidate exists = get(candidate.getId());
        if (exists == null) {
            return 0;
        }
        return runInSession(() -> {
            session.remove(candidate);
            return 1;
        });
    }

    @Override
    public int update(Candidate candidate) throws NonExistentCandidateException {
        Candidate exists = get(candidate.getId());
        if (exists == null) {
            return 0;
        }
        return runInSession(() -> {
            session.merge(candidate);
            return 1;
        });
    }

    @Override
    public Candidate get(Long id) throws NonExistentCandidateException {
        return runInSession(() -> session.get(Candidate.class, id));
    }

    @Override
    public List<Candidate> getByName(String name) {
        return runInSession(() -> {
            Query<Candidate> query = session.createQuery("from Candidate where name=:name", Candidate.class);
            query.setParameter("name", name);
            return query.getResultList();
        });
    }

    @Override
    public List<Candidate> getAll() {
        return runInSession(() -> {
            Query<Candidate> tQuery = session.createQuery(
                    "from Candidate c", Candidate.class);
            return tQuery.getResultList();
        });
    }
}
