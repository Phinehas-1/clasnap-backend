package com.bigdecimal.clasnapp;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.bigdecimal.clasnapp.group.GroupRepository;
import com.bigdecimal.clasnapp.user.Role;
import com.bigdecimal.clasnapp.user.RoleName;
import com.bigdecimal.clasnapp.user.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ClasnappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClasnappApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roles, GroupRepository groups) {
		return arg -> {
			createRole(roles);
		};
	}

	@Transactional
	private void createRole(RoleRepository roles) {
		List<Role> roleList = roles.findAll();
		log.info("Number of existent roles is {}", roleList.size());
		if (roleList.size() < 4) {
			Role user = roles.findByName(RoleName.USER).orElse(new Role(RoleName.USER));
			Role groupLeader = roles.findByName(RoleName.GROUP_LEADER).orElse(new Role(RoleName.GROUP_LEADER));
			Role supervisor = roles.findByName(RoleName.SUPERVISOR).orElse(new Role(RoleName.SUPERVISOR));
			Role md = roles.findByName(RoleName.MD).orElse(new Role(RoleName.MD));
			roles.saveAll(Arrays.asList(user, groupLeader, supervisor, md));
		}
	}
}
