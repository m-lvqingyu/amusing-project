package com.amusing.start.search.controller.inward;

import com.amusing.start.client.api.IndexClient;
import com.amusing.start.client.input.IndexCreateInput;
import com.amusing.start.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexInwardController implements IndexClient {

    private final IndexService indexService;

    @Autowired
    public IndexInwardController(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public Boolean create(IndexCreateInput input) {
        return indexService.create(input.getIndex(), input.getProperties());
    }

    @Override
    public Boolean exist(String index) {
        return indexService.exist(index);
    }

}
