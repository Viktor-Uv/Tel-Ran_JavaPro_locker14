package com.example.locker14;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// источник UserDetail
// получает пользователей из их хранилища и мапит на UserDetail
@Service
public class UserDetailSource implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // (UserData implements UserDetails)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + "not found");
        }
        return new UserData(user);
    }
}
