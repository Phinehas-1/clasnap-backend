package com.bigdecimal.clasnapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bigdecimal.clasnapp.group.Group;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    
    private String lastName;

    @ManyToOne
    private Role role;

    @OneToOne(mappedBy = "leader")
    private Group leader;

    @ManyToMany
    List<Group> group = new ArrayList<Group>();

    public User(Role role) {
        this.role = role;
    }

}
