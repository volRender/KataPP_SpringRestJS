package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void addOrUpdateUser(User user, Long id) {
        user.setId(id);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void setPasswordEncoder(User user) {
        Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");
        if(!user.getPassword().matches(String.valueOf(BCRYPT_PATTERN))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

    @Transactional
    @Override
    public User getUser(Long id) {
        User user = new User();
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()) {
            user = userOpt.get();
        }
        return user;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByFirstName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with name " + username + " не найден!");
        }
        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
