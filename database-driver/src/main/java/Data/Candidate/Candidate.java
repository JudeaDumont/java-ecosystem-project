package Data.Candidate;

import Data.EnforcedClassExtension.ID;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
public class Candidate implements ID {
    private Long id;
    private String name;

    public Candidate() {
        // this form used by Hibernate
    }

    public Candidate(Long id, String name) {
        // for application use, to create new Employees
        this.id = id;
        this.name = name;
    }

    public Candidate(String name) {
        // for application use, to create new Employees
        this.name = name;
    }

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
}
