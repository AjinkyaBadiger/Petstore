package io.swagger.api.factories;

import io.swagger.api.StoreApiService;
import io.swagger.api.impl.StoreApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-11-23T09:44:41.219Z")
public class StoreApiServiceFactory {
    private final static StoreApiService service = new StoreApiServiceImpl();

    public static StoreApiService getStoreApi() {
        return service;
    }
}
