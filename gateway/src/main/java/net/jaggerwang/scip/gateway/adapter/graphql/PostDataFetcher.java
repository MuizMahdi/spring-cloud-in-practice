package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PostDataFetcher extends AbstractDataFetchers {
    public DataFetcher user() {
        return env -> {
            PostDto postDto = env.getSource();
            return userService.info(postDto.getUserId());
        };
    }

    public DataFetcher images() {
        return env -> {
            PostDto postDto = env.getSource();
            return fileService.infos(postDto.getImageIds(), false);
        };
    }

    public DataFetcher video() {
        return env -> {
            PostDto postDto = env.getSource();
            if (postDto.getVideoId() == null) {
                return Mono.empty();
            }

            return fileService.info(postDto.getVideoId());
        };
    }

    public DataFetcher stat() {
        return env -> {
            PostDto postDto = env.getSource();
            return statService.ofPost(postDto.getId());
        };
    }

    public DataFetcher liked() {
        return env -> {
            PostDto postDto = env.getSource();
            return postService.isLiked(postDto.getId());
        };
    }
}