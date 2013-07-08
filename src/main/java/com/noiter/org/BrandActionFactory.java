package com.noiter.org;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BrandActionFactory {

    private final String brand;

    private BrandActionFactory(String brand) {
        this.brand = brand;
    }

    public static BrandActionFactory brand(String brand) {
        return new BrandActionFactory(brand);
    }

    public <T> T generateAction(Class<T> actionClass) {
        return generateAction(actionClass, new Object[0]);
    }

    public <T> T generateAction(Class<T> actionClass, Object[] parameters) {

        Class<?>[] constructorTypes = extractTypes(parameters);

        ProxyFactory proxy = new ProxyFactory();
        proxy.setSuperclass(actionClass);
        proxy.setFilter(new MethodFilterImpl());

        try {
            return (T) proxy.create(constructorTypes, parameters, new ActionHandlerImpl(this.brand));
        } catch (NoSuchMethodException e) {
            throw new SampleException("No Method Found");
        } catch (InstantiationException e) {
            throw new SampleException("No Constructor Found");
        } catch (InvocationTargetException e) {
            throw new SampleException("No Constructor Found");
        } catch (IllegalAccessException e) {
            throw new SampleException("Do not have access");
        }
    }

    private Class<?>[] extractTypes(Object[] parameters) {
        Class<?>[] types = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].getClass();
        }
        return types;
    }

    private class MethodFilterImpl implements MethodFilter {

        @Override
        public boolean isHandled(Method method) {
            return method.getAnnotation(Brand.class) != null;
        }
    }

    private class ActionHandlerImpl implements MethodHandler {

        private String Brand;

        public ActionHandlerImpl(String brand) {
            this.Brand = brand;
        }

        @Override
        public Object invoke(Object destination, Method method, Method process, Object[] arguments) throws Throwable {

            String[] value = method.getAnnotation(Brand.class).value();
            Set<String> set = putBrandsIntoSet(value);

            if(set.contains(this.Brand)) {
                return null;
            }

            return process.invoke(destination, arguments);
        }

        private Set<String> putBrandsIntoSet(String[] method) {

            Set<String> brandSet = new HashSet<String>();
            Collections.addAll(brandSet, method);
            return brandSet;
        }
    }
}