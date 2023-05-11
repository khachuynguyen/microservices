package userservice.userservice.Services;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import userservice.userservice.AdviceHandle.BadRequestException;
import userservice.userservice.Config.JwtUtils;
import userservice.userservice.DTO.UserDTO;
import userservice.userservice.Entities.User;
import userservice.userservice.Repositories.UserRepository;
import userservice.userservice.Requests.CreateUserRequest;
import userservice.userservice.Requests.UpdateUserRequest;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    public UserDTO createUser(CreateUserRequest userRequest) throws Exception {
        if(userRepository.findUsersByUserName(userRequest.getUserName()).isPresent())
            throw new BadRequestException("User name has already existed");
        User user = new User(userRequest.getUserName(), userRequest.getFullName(), userRequest.getEmail(), passwordEncoder.encode(userRequest.getPassword()) , userRequest.getPhone(), "USER", userRequest.getAddress());
        User tmp = userRepository.save(user);
        UserDTO dto = new UserDTO(tmp.getUserName(), jwtUtils.generateJwtToken(user), tmp.getRole());
        return dto;
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserByUserName(String userName) {
        if(userRepository.findUsersByUserName(userName).isPresent())
            return userRepository.findUsersByUserName(userName).get();
        return null;
    }

    public User updateUser(User userFound, UpdateUserRequest userRequest) {
        userFound.setEmail(userRequest.getEmail());
        userFound.setAddress(userRequest.getAddress());
        userFound.setPhone(userRequest.getPhone());
        userFound.setFullName(userRequest.getFullName());
        return userRepository.save(userFound);
    }

    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }
}
