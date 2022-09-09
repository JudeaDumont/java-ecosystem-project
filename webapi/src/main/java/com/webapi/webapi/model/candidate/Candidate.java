package com.webapi.webapi.model.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.webapi.webapi.model.ID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "Candidate")
public class Candidate implements ID {
    private Long id;
    private String name;

    public Candidate() {
        // this form used by Hibernate
    }

    public Candidate(@JsonProperty("id") Long id,
                     @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public Candidate(String name) {
        this.name = name;
    }

    //todo: make a model/class where a GUID is used for the id column
    //todo: make a model/class where a UUID is used for the id column
    // is that even possible? google "UUID/GUID as primary key postgresql"
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
