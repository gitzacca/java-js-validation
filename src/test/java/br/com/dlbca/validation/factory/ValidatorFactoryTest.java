package br.com.dlbca.validation.factory;


import br.com.dlbca.validation.Validator;
import br.com.dlbca.validation.ValidatorExecution;
import br.com.dlbca.validation.engine.JavascriptValidatorEngine;
import br.com.dlbca.validation.engine.ValidatorResult;
import br.com.dlbca.validation.engine.ValidatorEngine;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by guilherme on 11/08/14.
 */
public class ValidatorFactoryTest {

    @Test
    public void shouldCreateValidatorDefault() throws NoSuchFieldException, IllegalAccessException {
        Validator validator = ValidatorFactory.create();

        Assert.assertNotNull(validator);
        Assert.assertEquals(ValidatorExecution.class, validator.getClass());
        Field engineField = validator.getClass().getDeclaredField("engine");

        engineField.setAccessible(true);

        ValidatorEngine engine = (ValidatorEngine) engineField.get(validator);

        Assert.assertEquals(JavascriptValidatorEngine.class, engine.getClass());
    }

    @Test
    public void shouldCreatedValidatorDefinedEngine() throws NoSuchFieldException, IllegalAccessException {
        ValidatorEngine fakeEngine = getValidatorEngine();

        Validator validator = ValidatorFactory.using(fakeEngine).create();

        Assert.assertNotNull(validator);
        Assert.assertEquals(ValidatorExecution.class, validator.getClass());
        Field engineField = validator.getClass().getDeclaredField("engine");

        engineField.setAccessible(true);

        ValidatorEngine engine = (ValidatorEngine) engineField.get(validator);

        Assert.assertEquals(fakeEngine.getClass(), engine.getClass());
    }

    private ValidatorEngine getValidatorEngine() {
        return new ValidatorEngine() {
                @Override
                public ValidatorResult validate() {
                    return null;
                }

                @Override
                public void setPathScript(String pathScript) {

                }

                @Override
                public void setData(Object data) {

                }

                @Override
                public void setConstrains(List<String> constrains) {

                }
            };
    }

}
