package com.example.springbootpetproject.security;

import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.entity.UserRole;
import com.example.springbootpetproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(String.format("User with name '%s' dose not find",username));
        }
        boolean enabled = !user.isAccountVerified();
       /* return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword()
                ,mapRolesToAuthority(user.getUserRole()));*/
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(enabled)
                .authorities(mapRolesToAuthority(user.getUserRole())).build();

        return userDetails;
    }

    //Collection<UserRole> roles
    /*private Collection<? extends GrantedAuthority> mapRolesToAuthority(Collection<UserRole> roles){
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getAuthority())).collect(Collectors.toList());
    }*/
    private Collection<? extends GrantedAuthority> mapRolesToAuthority(UserRole roles){
        return List.of(new SimpleGrantedAuthority(roles.getAuthority()));
    }
}
