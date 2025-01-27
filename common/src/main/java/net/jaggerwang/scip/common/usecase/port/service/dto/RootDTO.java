package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RootDTO {
    private String code;
    private String message;
    private Map<String, Object> data;

    public RootDTO(String code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RootDTO(String code, String message) {
        this(code, message, new HashMap<>());
    }

    public RootDTO(Map<String, Object> data) {
        this("ok", "", data);
    }

    public RootDTO() {
        this("ok", "", new HashMap<>());
    }

    public RootDTO addDataEntry(String key, Object value) {
        data.put(key, value);
        return this;
    }
}