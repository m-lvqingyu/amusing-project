package com.amusing.start.client.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotEmpty(message = "索引名称不能为空")
    private String index;

    /**
     * 映射
     * <p>
     * Map<String, Object> message = new HashMap<>();
     * message.put("type", "text");
     * Map<String, Object> properties = new HashMap<>();
     * properties.put("message", message);
     */
    @NotNull(message = "索引映射不能为空")
    @Size(min = 1, max = 100, message = "索引映射长度超过限制")
    private Map<String, Object> properties;

}
