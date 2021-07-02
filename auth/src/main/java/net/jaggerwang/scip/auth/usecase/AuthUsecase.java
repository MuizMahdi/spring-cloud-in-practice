package net.jaggerwang.scip.auth.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.util.encoder.PasswordEncoder;
import net.jaggerwang.scip.common.util.generator.RandomGenerator;
import net.jaggerwang.scip.common.entity.RoleBO;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.common.entity.UserBO;
import net.jaggerwang.scip.auth.usecase.port.dao.RoleDAO;
import net.jaggerwang.scip.auth.usecase.port.dao.UserDAO;
import org.springframework.stereotype.Component;

@Component
public class AuthUsecase {
    private static HashMap<String, String> mobileVerifyCodes = new HashMap<>();
    private static HashMap<String, String> emailVerifyCodes = new HashMap<>();

    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private RandomGenerator randomGenerator = new RandomGenerator();
    private PasswordEncoder passwordEncoder = new PasswordEncoder();

    public AuthUsecase(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    public UserBO register(UserBO userBO) {
        if (userDAO.findByUsername(userBO.getUsername()).isPresent()) {
            throw new UsecaseException("用户名重复");
        }

        var user = UserBO.builder().username(userBO.getUsername())
                .password(passwordEncoder.encode(userBO.getPassword())).build();
        return userDAO.save(user);
    }

    public Boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public UserBO modify(Long id, UserBO userBO) {
        var user = userDAO.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("用户未找到");
        }

        if (userBO.getUsername() != null) {
            if (userDAO.findByUsername(userBO.getUsername()).isPresent()) {
                throw new UsecaseException("用户名重复");
            }
            user.setUsername(userBO.getUsername());
        }
        if (userBO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userBO.getPassword()));
        }
        if (userBO.getMobile() != null) {
            if (userDAO.findByMobile(userBO.getMobile()).isPresent()) {
                throw new UsecaseException("手机重复");
            }
            user.setMobile(userBO.getMobile());
        }
        if (userBO.getEmail() != null) {
            if (userDAO.findByEmail(userBO.getEmail()).isPresent()) {
                throw new UsecaseException("邮箱重复");
            }
            user.setEmail(userBO.getEmail());
        }
        if (userBO.getAvatarId() != null) {
            user.setAvatarId(userBO.getAvatarId());
        }
        if (userBO.getIntro() != null) {
            user.setIntro(userBO.getIntro());
        }

        return userDAO.save(user);
    }

    public String sendMobileVerifyCode(String type, String mobile) {
        var key = String.format("%s_%s", type, mobile);
        if (mobileVerifyCodes.get(key) == null) {
            var code = randomGenerator.numberString(6);
            mobileVerifyCodes.put(key, code);
        }
        return mobileVerifyCodes.get(key);
    }

    public Boolean checkMobileVerifyCode(String type, String mobile, String code) {
        var key = String.format("%s_%s", type, mobile);
        if (code != null && code.equals(mobileVerifyCodes.get(key))) {
            mobileVerifyCodes.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public String sendEmailVerifyCode(String type, String email) {
        var key = String.format("%s_%s", type, email);
        if (emailVerifyCodes.get(key) == null) {
            var code = randomGenerator.numberString(6);
            emailVerifyCodes.put(key, code);
        }
        return emailVerifyCodes.get(key);
    }

    public Boolean checkEmailVerifyCode(String type, String email, String code) {
        var key = String.format("%s_%s", type, email);
        if (code != null && code.equals(emailVerifyCodes.get(key))) {
            emailVerifyCodes.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserBO> info(Long id) {
        return userDAO.findById(id);
    }

    public Optional<UserBO> infoByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public Optional<UserBO> infoByMobile(String mobile) {
        return userDAO.findByMobile(mobile);
    }

    public Optional<UserBO> infoByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public List<RoleBO> roles(String username) {
        return roleDAO.rolesOfUser(username);
    }
}
