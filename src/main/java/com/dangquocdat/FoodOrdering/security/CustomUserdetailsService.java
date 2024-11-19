package com.dangquocdat.FoodOrdering.security;

import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserdetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userFromDB = userRepository.findByEmail(username)
                            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"Username is not exists in DB"));

        return new CustomUserDetails(userFromDB);
    }
}
