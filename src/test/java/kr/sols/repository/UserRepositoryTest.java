package kr.sols.repository;

import jakarta.transaction.Transactional;
import kr.sols.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void TestUser() throws Exception {
        //given
        User user = new User();
        user.setUsername("userA");

        //when
        Long savedId = userRepository.save(user);
        User findUser = userRepository.find(savedId);
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(findUser).isEqualTo(user);
    }

}