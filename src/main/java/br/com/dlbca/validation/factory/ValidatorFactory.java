package br.com.dlbca.validation.factory;

import br.com.dlbca.validation.Validator;
import br.com.dlbca.validation.ValidatorExecution;
import br.com.dlbca.validation.engine.JavascriptValidatorEngine;
import br.com.dlbca.validation.engine.ValidatorEngine;

/**
 * Created by guilherme on 11/08/14.
 */
public class ValidatorFactory {

    private ValidatorFactory() {
    }

    public static Validator create() {

        JavascriptValidatorEngine engine = new JavascriptValidatorEngine();
        engine.setPathScript("/validator.js");

        return new ValidatorExecution(engine);
    }

    public static Creator using(final ValidatorEngine engine) {
        return new Creator() {
            public Validator create() {
                return new ValidatorExecution(engine);
            }
        };
    }

    public interface Creator {
        Validator create();
    }
}
