package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.services.iservices.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        //Kiểm tra xem số điện thoại đã tồn tại chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw  new DataIntegrityViolationException("Phone number already exists");
        }

        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        //securrity, làm sau
        return null;
    }
}
