package br.com.dlbca.validation;

import br.com.dlbca.validation.engine.ValidatorResult;
import br.com.dlbca.validation.engine.ValidatorEngine;

import java.util.List;

/**
 * Created by guilherme on 11/08/14.
 */
public class ValidatorExecution implements Validator, ValidatorExecute {

    private ValidatorEngine engine;
    private Object data;
    private List<String> constrains;

    public ValidatorExecution(ValidatorEngine engine) {
        this.engine = engine;
    }

    @Override
    public ValidatorExecute validate(Object data) {
        this.data = data;

        return this;
    }

    @Override
    public ValidatorResult using(List<String> constrains) {
        this.constrains = constrains;

        engine.setData(this.data);
        engine.setConstrains(this.constrains);
        return engine.validate();
    }
}