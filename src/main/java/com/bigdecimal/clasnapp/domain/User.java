package com.bigdecimal.clasnapp.domain;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @JsonProperty("User ID")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @JsonProperty("First Name")
    @Column(nullable = false)
    private String firstName;
    
     @JsonProperty("Last Name")
    @Column(nullable = false)
     private String lastName;

     @JsonProperty("Username")
    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    
     @JsonProperty("Roles")
    @ManyToMany(fetch = FetchType.EAGER)
     private List<Role> roles;

     @JsonProperty("Group")
    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    public User(String firstName, String lastName, String username, String password, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
        this.username = username;
    }

    


    

}
