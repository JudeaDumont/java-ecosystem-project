package persistentdatatests;

import com.webapi.databasedrivers.hibernateinmemory.HibernateConnectionGenericMethods;
import com.webapi.model.candidate.Candidate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGenericHibernateMethodsViaCandidate {

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() {
        String uuid = UUID.randomUUID().toString();

        HibernateConnectionGenericMethods.genericSave((new Candidate(uuid)));

        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid));

        HibernateConnectionGenericMethods.genericDelete(candidatesMatchingName.get(0));

        List<Candidate> candidatesMatchingName2 = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid);
        assert (candidatesMatchingName2.size() == 0);
    }

    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        List<Candidate> candidates = HibernateConnectionGenericMethods.genericGetAll(Candidate.class);
        int candidatesSize = candidates.size();

        HibernateConnectionGenericMethods.genericSave(new Candidate(uuid1));
        HibernateConnectionGenericMethods.genericSave(new Candidate(uuid2));

        List<Candidate> candidatesAfterInsert = HibernateConnectionGenericMethods.genericGetAll(Candidate.class);
        assert (candidatesAfterInsert.size() == candidatesSize + 2);

        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid1);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid1));

        List<Candidate> candidatesMatchingName2 = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid2);
        assert (candidatesMatchingName2.size() == 1);
        assert (Objects.equals(candidatesMatchingName2.get(0).getName(), uuid2));

        HibernateConnectionGenericMethods.genericDelete(candidatesMatchingName.get(0));
        HibernateConnectionGenericMethods.genericDelete(candidatesMatchingName2.get(0));

        List<Candidate> candidatesAfterDelete = HibernateConnectionGenericMethods.genericGetAll(Candidate.class);
        assert (candidatesAfterDelete.size() == candidatesSize);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws ClassNotFoundException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = HibernateConnectionGenericMethods.genericSaveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = HibernateConnectionGenericMethods.genericGet(Candidate.class, id);
        assert (candidate != null);

        HibernateConnectionGenericMethods.genericDelete(candidate);
        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid1);
        assert (candidatesMatchingName.size() == 0);
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws ClassNotFoundException {
        String uuid1 = UUID.randomUUID().toString();
        String changeUuid1 = UUID.randomUUID().toString();

        Long id = HibernateConnectionGenericMethods.genericSaveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = HibernateConnectionGenericMethods.genericGet(Candidate.class, id);
        assert (candidate != null);

        candidate.setName(changeUuid1);

        HibernateConnectionGenericMethods.genericUpdate(candidate);

        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid1);
        assert (candidatesMatchingName.size() == 0);

        List<Candidate> candidatesMatchingChangeName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, changeUuid1);
        assert (candidatesMatchingChangeName.size() == 1);
        assert (Objects.equals(candidatesMatchingChangeName.get(0).getName(), changeUuid1));

        HibernateConnectionGenericMethods.genericDelete(candidate);

        List<Candidate> candidatesMatchingName2 = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid1);
        assert (candidatesMatchingName2.size() == 0);

    }

    @Test
    @Order(5)
    void test_SaveID_Get_Del_Del() throws ClassNotFoundException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = HibernateConnectionGenericMethods.genericSaveReturnID(new Candidate(uuid1));
        assert (id != null && id != 0);

        Candidate candidate = HibernateConnectionGenericMethods.genericGet(Candidate.class, id);
        assert (candidate != null);

        HibernateConnectionGenericMethods.genericDelete(candidate);

        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, uuid1);
        assert (candidatesMatchingName.size() == 0);
    }

    @Test
    @Order(6)
    void test_SaveReturnID() {
        Candidate kraken = new Candidate("kraken");
        Long id = HibernateConnectionGenericMethods.genericSaveReturnID(kraken);
        assert (id != null && id != 0);

        HibernateConnectionGenericMethods.genericDelete(kraken);

        List<Candidate> candidatesMatchingName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, "kraken");
        assert (candidatesMatchingName.size() == 0);
    }

    @Test
    @Order(7)
    void test_BadUpdate() {
        String uuid1 = UUID.randomUUID().toString();
        HibernateConnectionGenericMethods.genericUpdate(new Candidate(0L, uuid1));
    }
}
