package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;
import reactor.core.publisher.Mono;

public interface StatAsyncService {
    Mono<UserStatDto> ofUser(Long userId);

    Mono<PostStatDto> ofPost(Long postId);
}
