package br.com.dlbca.validation.engine;

import java.util.List;
import java.util.Map;

/**
 * Created by guilherme on 08/08/14.
 */
public interface ValidatorResult {
    boolean hasErrors();
    Map<String, Object> getAllFailures();
    List<String> getError(String field);
}
