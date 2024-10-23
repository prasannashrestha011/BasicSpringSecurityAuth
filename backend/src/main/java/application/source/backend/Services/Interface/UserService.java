package application.source.backend.Services.Interface;

import application.source.backend.Entities.UserEntity;
import application.source.backend.Responses.ApiResponses;

public interface UserService {
    UserEntity loadByUsername(String username);

    ApiResponses registerNewUser(UserEntity newUser);
}
