package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.FileAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileAsyncServiceImpl extends InternalAsyncService implements FileAsyncService {
    public FileAsyncServiceImpl(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                                ObjectMapper objectMapper) {
        super(webClient, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<List<FileDTO>> upload(String region, String bucket, String path,
                                      List<MultipartFile> files) {
        var params = new LinkedMultiValueMap<String, Object>();
        params.add("region", region);
        params.add("bucket", bucket);
        params.add("path", path);
        params.addAll("file", files);
        return postData("/file/upload", params)
                .map(data -> objectMapper.convertValue(data.get("files"), new TypeReference<>(){}));
    }

    @Override
    public Mono<List<FileDTO>> upload(List<MultipartFile> files) {
        return upload(null, null, null, files);
    }

    @Override
    public Mono<FileDTO> info(Long id) {
        return getData("/file/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("file"), FileDTO.class));
    }

    @Override
    public Mono<List<FileDTO>> infos(List<Long> ids, Boolean keepNull) {
        return getData("/file/infos", Map.of(
                    "ids", String.join(",", ids.stream()
                        .map(id -> id.toString())
                        .collect(Collectors.toList())),
                    "keepNull", keepNull.toString()))
                .map(data -> objectMapper.convertValue(data.get("files"), new TypeReference<>(){}));
    }
}
