package com.rtcc.demo.services;

import com.rtcc.demo.model.Coordinator;
import com.rtcc.demo.repository.CoordinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CoordinatorRepository coordinatorRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Coordinator coordinator = coordinatorRepository.findByUsername(username);
//        System.out.println("Coordinator: " + coordinator.getUsername());
//        System.out.println("Coordinator: " + coordinator.getPassword());
//        System.out.println("Coordinator: " + coordinator.getEmail());
//        System.out.println("Coordinator: " + coordinator.getName());
//        System.out.println("Coordinator: " + coordinator.getId());
        return UserDetailImpl.build(coordinator);
    }
}
