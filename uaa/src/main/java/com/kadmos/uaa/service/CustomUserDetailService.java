package com.kadmos.uaa.service;

import com.kadmos.uaa.domain.User;
import com.kadmos.uaa.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {
  final UserRepository userRepository;

  public CustomUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User loadedUser;
    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isPresent()) loadedUser = optionalUser.get();
    else throw new UsernameNotFoundException("No user found with username ".concat(username));

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    optionalUser.ifPresent(
        user ->
            user.getPrivileges()
                .forEach(
                    privilege ->
                        grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()))));

    return new org.springframework.security.core.userdetails.User(
        loadedUser.getUsername(),
        loadedUser.getPassword(),
        true,
        true,
        true,
        true,
        grantedAuthorities);
  }
}
