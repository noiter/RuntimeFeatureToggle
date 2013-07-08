package com.noiter.org;

public class FooAction {

    private FooService service;

    public FooAction() {
    }

    public FooAction(FooService service) {
        this.service = service;
    }

    public FooService getService() {
        return service;
    }

    public void setService(FooService service) {
        this.service = service;
    }

    @Brand("Brand_Two")
    public String getBrandName() {
        return service.getFinalGoal();
    }
}
