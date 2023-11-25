package com.bigdecimal.clasnapp;

import com.bigdecimal.clasnapp.domain.group.Group;
import com.bigdecimal.clasnapp.domain.group.GroupName;
import com.bigdecimal.clasnapp.domain.group.GroupRepository;
import com.bigdecimal.clasnapp.domain.role.Role;
import com.bigdecimal.clasnapp.domain.role.RoleName;
import com.bigdecimal.clasnapp.domain.role.RoleRepository;
import com.bigdecimal.clasnapp.domain.user.data.User;
import com.bigdecimal.clasnapp.domain.user.data.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Slf4j
public class ClasnappApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClasnappApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
    RoleRepository roles,
    GroupRepository groups,
    UserRepository users,
    PasswordEncoder encoder
  ) {
    return arg -> {
      initializeRoles(roles);
      initializeGroups(groups);
      initializeAdminAccount(users, roles, encoder);
    };
  }

  @Transactional
  private void initializeRoles(RoleRepository roles) {
    List<Role> roleList = roles.findAll();
    log.info("Number of existent roles is {}", roleList.size());
    if (roleList.size() < 4) {
      Role user = roles
        .findByName(RoleName.USER)
        .orElse(new Role(RoleName.USER));
      Role groupLeader = roles
        .findByName(RoleName.GROUP_LEADER)
        .orElse(new Role(RoleName.GROUP_LEADER));
      Role supervisor = roles
        .findByName(RoleName.SUPERVISOR)
        .orElse(new Role(RoleName.SUPERVISOR));
      Role md = roles.findByName(RoleName.MD).orElse(new Role(RoleName.MD));
      roles.saveAll(Arrays.asList(user, groupLeader, supervisor, md));
    }
  }

  @Transactional
  private void initializeGroups(GroupRepository groups) {
    List<Group> groupList = groups.findAll();
    log.info("Number of existent groups is {}", groupList.size());
    if (groupList.size() < 12) {
      Group year1 = groups
        .findByName(GroupName.YEAR_1)
        .orElse(new Group(GroupName.YEAR_1));
      Group year2 = groups
        .findByName(GroupName.YEAR_2)
        .orElse(new Group(GroupName.YEAR_2));
      Group year3 = groups
        .findByName(GroupName.YEAR_3)
        .orElse(new Group(GroupName.YEAR_3));
      Group year4 = groups
        .findByName(GroupName.YEAR_4)
        .orElse(new Group(GroupName.YEAR_4));
      Group year5 = groups
        .findByName(GroupName.YEAR_5)
        .orElse(new Group(GroupName.YEAR_5));
      Group year6 = groups
        .findByName(GroupName.YEAR_6)
        .orElse(new Group(GroupName.YEAR_6));
      Group year7 = groups
        .findByName(GroupName.YEAR_7)
        .orElse(new Group(GroupName.YEAR_7));
      Group year8 = groups
        .findByName(GroupName.YEAR_8)
        .orElse(new Group(GroupName.YEAR_8));
      Group year9 = groups
        .findByName(GroupName.YEAR_9)
        .orElse(new Group(GroupName.YEAR_9));
      Group year10 = groups
        .findByName(GroupName.YEAR_10)
        .orElse(new Group(GroupName.YEAR_10));
      Group year11 = groups
        .findByName(GroupName.YEAR_11)
        .orElse(new Group(GroupName.YEAR_11));
      Group year12 = groups
        .findByName(GroupName.YEAR_12)
        .orElse(new Group(GroupName.YEAR_12));
      groups.saveAll(
        Arrays.asList(
          year1,
          year2,
          year3,
          year4,
          year5,
          year6,
          year7,
          year8,
          year9,
          year10,
          year11,
          year12
        )
      );
    }
  }

  @Transactional
  private void initializeAdminAccount(
    UserRepository users,
    RoleRepository roles,
    PasswordEncoder encoder
  ) {
    Role mdRole = roles
      .findByName(RoleName.MD)
      .orElseThrow(() ->
        new EntityNotFoundException("Can't find MD role in database.")
      );
    User user = new User(
      "Stephen",
      "Imade",
      "admin",
      encoder.encode("adminPword"),
      Arrays.asList(mdRole)
    );
    users.save(user);
  }
}
