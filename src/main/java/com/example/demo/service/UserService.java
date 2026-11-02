package com.example.demo.service;

import com.example.demo.Entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

private final UserRepository userRepository;

public UserService(UserRepository userRepository) {
this.userRepository = userRepository;
}

public List<User> findAll() {
return userRepository.findAll();
}

public User findById(Long id) {
return userRepository.findById(id)
.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
}

public User create(User user) {
if (userRepository.existsByEmail(user.getEmail())) {
throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
}
user.setId(null);
return userRepository.save(user);
}

public User update(Long id, User data) {
User u = findById(id);
u.setFirstName(data.getFirstName());
u.setLastName(data.getLastName());
u.setEmail(data.getEmail());
u.setPhone(data.getPhone());
return userRepository.save(u);
}

public void delete(Long id) {
userRepository.delete(findById(id));
}
}
