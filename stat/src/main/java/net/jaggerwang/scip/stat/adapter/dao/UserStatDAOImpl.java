package net.jaggerwang.scip.stat.adapter.dao;

import java.util.Optional;

import net.jaggerwang.scip.stat.adapter.dao.jpa.entity.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.stat.adapter.dao.jpa.UserStatRepository;
import net.jaggerwang.scip.common.entity.UserStatBO;
import net.jaggerwang.scip.stat.usecase.port.dao.UserStatDAO;

@Component
public class UserStatDAOImpl implements UserStatDAO {
    @Autowired
    private UserStatRepository userStatRepository;

    @Override
    public UserStatBO save(UserStatBO userStatBO) {
        return userStatRepository.save(UserStat.fromEntity(userStatBO)).toEntity();
    }

    @Override
    public Optional<UserStatBO> findById(Long id) {
        return userStatRepository.findById(id).map(userStat -> userStat.toEntity());
    }

    @Override
    public Optional<UserStatBO> findByUserId(Long userId) {
        return userStatRepository.findByUserId(userId).map(userStat -> userStat.toEntity());
    }
}
