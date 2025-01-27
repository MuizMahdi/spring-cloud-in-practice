package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController extends AbstractController {
    private UserAsyncService userAsyncService;

    public AuthController(UserAsyncService userAsyncService) {
        this.userAsyncService = userAsyncService;
    }

    @PostMapping("/login")
    public Mono<RootDTO> login(@RequestBody UserDTO userDto) {
        String username = null;
        if (userDto.getUsername() != null)  {
            username = userDto.getUsername();
        } else if (userDto.getMobile() != null) {
            username = userDto.getMobile();
        } else if (userDto.getEmail() != null) {
            username = userDto.getEmail();
        }
        if (StringUtils.isEmpty(username)) {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }
        var password = userDto.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new UsecaseException("密码不能为空");
        }

        return loginUser(username, password)
                .flatMap(loggedUser -> userAsyncService.info(loggedUser.getId()))
                .map(user -> new RootDTO().addDataEntry("user", user));
    }

    @GetMapping("/logout")
    public Mono<RootDTO> logout() {
        return logoutUser()
                .flatMap(loggedUser -> userAsyncService.info(loggedUser.getId()))
                .map(user -> new RootDTO().addDataEntry("user", user))
                .defaultIfEmpty(new RootDTO().addDataEntry("user", null));
    }

    @GetMapping("/logged")
    public Mono<RootDTO> logged() {
        return loggedUserId()
                .flatMap(userId -> userAsyncService.info(userId))
                .map(user -> new RootDTO().addDataEntry("user", user))
                .defaultIfEmpty(new RootDTO().addDataEntry("user", null));
    }
}
