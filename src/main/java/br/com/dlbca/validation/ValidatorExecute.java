package br.com.dlbca.validation;

import br.com.dlbca.validation.engine.ValidatorResult;

import java.util.List;

/**
 * Created by guilherme on 11/08/14.
 */
public interface ValidatorExecute {
    public ValidatorResult using(List<String> constrains);
}
