package br.com.dlbca.validation;

/**
 * Created by guilherme on 08/08/14.
 */
public interface Validator {
    public ValidatorExecute validate(Object data);
}