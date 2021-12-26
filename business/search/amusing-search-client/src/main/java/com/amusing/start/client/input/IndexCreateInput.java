package com.amusing.start.client.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lv.qingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexCreateInput {

    /**
     * 索引名称
     */
    private String index;

    /**
     * 映射
     * <p>
     * Map<String, Object> message = new HashMap<>();
     * message.put("type", "text");
     * Map<String, Object> properties = new HashMap<>();
     * properties.put("message", message);
     */
    private Map<String, Object> properties;

}
