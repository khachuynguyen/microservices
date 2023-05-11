package userservice.userservice.Controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import userservice.userservice.Config.GetTokenFromBearToken;
import userservice.userservice.Config.JwtUtils;
import userservice.userservice.DTO.UserDTO;
import userservice.userservice.Entities.User;
import userservice.userservice.Requests.CreateUserRequest;
import userservice.userservice.Requests.LoginRequest;
import userservice.userservice.Requests.UpdateUserRequest;
import userservice.userservice.Services.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private GetTokenFromBearToken getTokenFromBearToken;

    @PostMapping("api/auth/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest userRequest) throws Exception {
        UserDTO userDTO = userService.createUser(userRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("api/user/{id}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User auth = userService.getUserByUserName(user.getUsername());
        User found = userService.getUserById(userRequest.getId());
        if (auth.getId() == userRequest.getId() || auth.getRole().toUpperCase().equalsIgnoreCase("ADMIN")) {
            User userUpdate = userService.updateUser(found, userRequest);
            return new ResponseEntity<>(userUpdate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //    @GetMapping("api/user/{userName}")
//    public ResponseEntity<Object> getInfor(@PathVariable("userName") String userName){
//        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User auth = userService.getUserByUserName(user.getUsername());
//        User found = userService.getUserByUserName(userName);
//        if(auth.getRole().toUpperCase().equalsIgnoreCase("ADMIN") || auth.getUserName().equalsIgnoreCase(userName))
//            return new ResponseEntity<>(found, HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
    @GetMapping("api/user/{id}")
    public ResponseEntity<Object> getInfor(@PathVariable("id") String userName, @RequestHeader("Authorization") String token) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User auth = userService.getUserByUserName(user.getUsername());
        int userId = getTokenFromBearToken.getUserId(token);
        User found = userService.getUserById(userId);
//        if (auth.getRole().toUpperCase().equalsIgnoreCase("ADMIN") || auth.getUserName().equalsIgnoreCase(userName))
        return new ResponseEntity<>(found, HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("api/auth/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByUserName(loginRequest.getUserName());
        UserDTO userDTO = new UserDTO(user.getUserName(), jwtUtils.generateJwtToken(user), user.getRole());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("api/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
}
