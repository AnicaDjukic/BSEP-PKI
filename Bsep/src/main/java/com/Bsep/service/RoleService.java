package com.Bsep.service;

import java.util.List;

import com.Bsep.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
