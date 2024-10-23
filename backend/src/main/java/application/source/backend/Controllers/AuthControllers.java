package application.source.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.source.backend.Entities.UserEntity;
import application.source.backend.Jwt.JwtService;
import application.source.backend.Responses.ApiResponses;
import application.source.backend.Services.Implementation.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/account")
public class AuthControllers {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponses> signUpUser(@RequestBody UserEntity newUser) {
        ApiResponses response = userService.registerNewUser(newUser);

        return new ResponseEntity<ApiResponses>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponses> signInUser(@RequestBody UserEntity authUser) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword()));
            var authToken = jwtService.generateToken(authUser);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + authToken);
            ApiResponses response = new ApiResponses("message", "authenticated");
            return new ResponseEntity<ApiResponses>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponses err = new ApiResponses("error:", e);
            return new ResponseEntity<ApiResponses>(err, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/home")
    public String getMethodName() {
        return "home page ";
    }

}
