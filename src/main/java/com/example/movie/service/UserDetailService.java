package com.example.movie.service;

import com.example.movie.config.PrincipalDetails;
import com.example.movie.dto.UserDto;
import com.example.movie.repository.UserRepository;
import com.example.movie.entity.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNo) throws UsernameNotFoundException {

        if (StringUtils.isBlank(userNo)) {
            throw new UsernameNotFoundException("사용자 아이디가 유효하지 않습니다.");
        }

        Optional<User> account = userRepository.findByUserId(userNo);
        if (account.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User userAccount = account.get();
        return new PrincipalDetails(userAccount);
    }


}

