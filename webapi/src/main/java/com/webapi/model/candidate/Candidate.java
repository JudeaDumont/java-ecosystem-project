package com.webapi.model.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webapi.model.ID;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Table;

//todo: make a model/class where a GUID is used for the id column
//todo: make a model/class where a UUID is used for the id column

@Entity
@Table(appliesTo = "Candidate")
public class Candidate implements ID {
    @Id
    @SequenceGenerator(
            name = "candidate_sequence",
            sequenceName = "candidate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "candidate_sequence"
    )
    private Long id;
    private String name;

    public Candidate() {
        // this form is used by Hibernate
    }

    public Candidate(@JsonProperty("id") Long candidateID,
                     @JsonProperty("name") String candidateName) {
        this.id = candidateID;
        this.name = candidateName;
    }

    public Candidate(String candidateName) {
        this.name = candidateName;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long candidateID) {
        this.id = candidateID;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String candidateName) {
        this.name = candidateName;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
