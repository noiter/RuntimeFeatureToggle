package com.noiter.org;

import org.junit.Before;
import org.junit.Test;

import static com.noiter.org.BrandActionFactory.brand;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BrandActionFactoryTest {

    private FooService service;

    @Before
    public void setUp() throws Exception {
        service = mock(FooService.class);
        when(service.getFinalGoal()).thenReturn("Hello world");
        service = new FooService();
    }

    @Test
    public void shouldGenerateActionWithoutParameter() {

        FooAction action = brand("Brand_One").generateAction(FooAction.class);

        assertThat(action, notNullValue());
    }

    @Test
    public void shouldGenerateActionWithParameters() {

        FooAction action = brand("Brand_One").generateAction(FooAction.class, new Object[] {service});

        assertThat(action, notNullValue());
    }

    @Test
    public void shouldExecuteMethodWhenAnnotationDoesNotContainBrand() {

        FooAction action = brand("Brand_One").generateAction(FooAction.class, new Object[] {service});

        assertThat(action.getBrandName(), is("Feature toggle implementation"));
    }

    @Test
    public void shouldNotExecuteMethodWhenAnnotationContainsBrand() {

        FooAction action = brand("Brand_Two").generateAction(FooAction.class);

        assertThat(action.getBrandName(), nullValue());
    }
}
