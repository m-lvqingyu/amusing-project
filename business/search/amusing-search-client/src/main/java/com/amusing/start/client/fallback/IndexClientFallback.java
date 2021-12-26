package com.amusing.start.client.fallback;

import com.amusing.start.client.api.IndexClient;
import com.amusing.start.client.input.IndexCreateInput;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IndexClientFallback implements FallbackFactory<IndexClient> {

    @Override
    public IndexClient create(Throwable throwable) {
        return new IndexClient() {

            @Override
            public Boolean create(IndexCreateInput input) {
                log.error("[search]-create index fallback! param:{}", input);
                return null;
            }

            @Override
            public Boolean exist(String index) {
                log.error("[search]-check index exist fallback! param:{}", index);
                return null;
            }
        };
    }
}
