package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.PostStatBO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatDTO {
    private Long id;

    private Long postId;

    @Builder.Default
    private Long likeCount = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostStatDTO fromEntity(PostStatBO postStatBO) {
        return PostStatDTO.builder().id(postStatBO.getId()).postId(postStatBO.getPostId())
                .likeCount(postStatBO.getLikeCount()).createdAt(postStatBO.getCreatedAt())
                .updatedAt(postStatBO.getUpdatedAt()).build();
    }

    public PostStatBO toEntity() {
        return PostStatBO.builder().id(id).postId(postId).likeCount(likeCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
