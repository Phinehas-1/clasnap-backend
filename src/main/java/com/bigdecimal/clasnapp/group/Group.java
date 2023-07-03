package com.bigdecimal.clasnapp.group;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bigdecimal.clasnapp.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private GroupName name;

    @OneToOne
    private User leader;

    @ManyToMany
    private List<User> users = new ArrayList<User>();
}
