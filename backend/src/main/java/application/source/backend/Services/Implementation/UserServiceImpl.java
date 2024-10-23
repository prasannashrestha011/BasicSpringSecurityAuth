package application.source.backend.Services.Implementation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import application.source.backend.Entities.UserEntity;
import application.source.backend.Repositories.UserServiceRepository;
import application.source.backend.Responses.ApiResponses;
import application.source.backend.Services.Interface.UserService;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserServiceRepository userServiceRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    private List<String> roles; // As

    @Override
    public UserEntity loadByUsername(String username) {
        UserEntity findUser = userServiceRepository.findByUsername(username);
        return findUser;
    }

    @Override
    public ApiResponses registerNewUser(UserEntity newUser) {
        roles = new ArrayList<String>();
        roles.add("ADMIN");
        roles.add("USER");
        newUser.setCreated_at(new Date());
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userServiceRepository.save(newUser);

        return new ApiResponses("message", newUser.getUsername() + " has been registered");
    }

}
