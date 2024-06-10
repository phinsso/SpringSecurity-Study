package com.example.TestSecurity.service;

import com.example.TestSecurity.dto.JoinDTO;
import com.example.TestSecurity.entity.UserEntity;
import com.example.TestSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {

        // db에 이미 동일한 username을 가진 회원이 존재하는지 검사
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser) {
            return;
        }

        // dto -> entity
        UserEntity data = new UserEntity();

        // setter로 값 주입
        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); // 입력된 비밀번호를 암호화하여 데이터베이스에 저장
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);
    }
}
