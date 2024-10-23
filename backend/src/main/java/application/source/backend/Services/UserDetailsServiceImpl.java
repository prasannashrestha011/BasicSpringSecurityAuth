package application.source.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import application.source.backend.Entities.UserEntity;
import application.source.backend.Repositories.UserServiceRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserServiceRepository userServiceRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity targetedUser = userServiceRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(targetedUser.getUsername())
                .password(targetedUser.getPassword())
                .roles(targetedUser.getRoles().get(0))
                .build();
    }

}
