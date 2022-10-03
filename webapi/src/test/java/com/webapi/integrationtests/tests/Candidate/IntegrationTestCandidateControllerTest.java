package com.webapi.integrationtests.tests.Candidate;

import com.webapi.model.candidate.Candidate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCandidateControllerTest {

    @Autowired
    private ServerProperties serverProperties;
    private CandidateTestHelperFunctions candidateTestHelper;

    //todo: check to see if there is an easy way to run tests in parallel
    // foreshadowing: how many connections can postgresql handle?


    @BeforeEach
    void setUp() {
        this.candidateTestHelper = new CandidateTestHelperFunctions(serverProperties.getPort());
    }

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() throws IOException, InterruptedException {

        Candidate candidate = candidateTestHelper.saveReturnCandidate();

        List<Candidate> candidatesMatchingName = candidateTestHelper.getCandidatesByNameReturn1(candidate.getName());

        candidateTestHelper.deleteCandidateReturn1(candidatesMatchingName.get(0));

        candidateTestHelper.getCandidatesReturn0(candidate);
    }

    //todo: give a good explanation of each test, and why this one in particular seems so elaborate
    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() throws IOException, InterruptedException {

        int candidatesSizePriorToAnyOperation = candidateTestHelper.getAllCandidatesReturnSize();

        Candidate candidate1 = candidateTestHelper.saveReturnCandidate();
        Candidate candidate2 = candidateTestHelper.saveReturnCandidate();

        assert (candidateTestHelper.getAllCandidatesReturnSize() == candidatesSizePriorToAnyOperation + 2);

        List<Candidate> candidatesByName1 = candidateTestHelper.getCandidatesByNameReturn1(candidate1.getName());
        List<Candidate> candidatesByName2 = candidateTestHelper.getCandidatesByNameReturn1(candidate2.getName());

        candidateTestHelper.deleteCandidateReturn1(candidatesByName1.get(0));
        candidateTestHelper.deleteCandidateReturn1(candidatesByName2.get(0));

        assert (candidateTestHelper.getAllCandidatesReturnSize() == candidatesSizePriorToAnyOperation);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws IOException, InterruptedException {
        Candidate candidate = candidateTestHelper.saveReturnCandidate();

        candidateTestHelper.deleteCandidateReturn1(candidate);
    }

    //todo: need a once over on all of the names, ensure they match the result they are actually getting
    // start a naming convention standard here in this project

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws IOException, InterruptedException {
        // Candidate name is a UUID that will need to be changed to a new UUID
        String changeUuid1 = UUID.randomUUID().toString();

        // Save & expose a new Candidate, set the ID in the original object
        Candidate candidate = candidateTestHelper.saveReturnCandidate();

        // Pull the candidate we just posted to the API
        Candidate candidateFromID = candidateTestHelper.getCandidateByIDReturnCandidate(candidate.getId());

        assert (Objects.equals(candidate.getId(), candidateFromID.getId()));
        assert (Objects.equals(candidate.getName(), candidateFromID.getName()));

        // Make a change
        candidateFromID.setName(changeUuid1);
        String updateResponse = candidateTestHelper.updateCandidateReturn1(candidateFromID);

        // Get the changed candidate by the change we made
        List<Candidate> candidateAfterChange = candidateTestHelper.getCandidatesByNameReturn1(changeUuid1);

        // Ensure that Attempting to get by previous value doesn't work
        candidateTestHelper.getCandidatesByNameReturn0(candidate.getName());

        // Check ID consistency
        assert (Objects.equals(candidate.getId(), candidateFromID.getId()));
        assert (Objects.equals(candidate.getId(), candidateAfterChange.get(0).getId()));

        // Delete candidate and make sure you can't pull it with our change
        candidateTestHelper.deleteCandidateReturn1(candidateFromID);

        candidateTestHelper.deleteCandidateReturn0(candidate);

        //todo: add comments for each on of these sequences of http requests, and make each comment match the name of the test
        // name should be short and descriptive, test 1 thing, and it should use domain specific language
    }

    @Test
    @Order(5)
    void test_SaveID_Del_Del() throws IOException, InterruptedException {
        Candidate candidate = candidateTestHelper.saveReturnCandidate();
        candidateTestHelper.deleteCandidateReturn1(candidate);
        candidateTestHelper.deleteCandidateReturn0(candidate);
    }

    @Test
    @Order(6)
    void test_SaveReturnID() throws IOException, InterruptedException {
        Candidate candidate = candidateTestHelper.saveReturnCandidate();
        candidateTestHelper.deleteCandidateReturn1(candidate);
    }

    @Test
    @Order(7)
    void test_BadUpdate() throws IOException, InterruptedException {
        String uuid1 = UUID.randomUUID().toString();
        candidateTestHelper.updateCandidateReturn0(new Candidate(0L, uuid1));
    }
}
