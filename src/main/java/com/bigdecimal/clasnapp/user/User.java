package com.bigdecimal.clasnapp.user;

import java.util.UUID;

import com.bigdecimal.clasnapp.group.Group;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @Column(updatable = false)
    private UUID id;

    //TODO Add username property. Value will be generated in service class.

    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Group group; // attempt fetch group using user

    public User(Role role) {
        this.role = role;
    }

}
