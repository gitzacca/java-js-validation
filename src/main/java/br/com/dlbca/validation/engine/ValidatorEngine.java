package br.com.dlbca.validation.engine;

import java.util.List;

/**
 * Created by guilherme on 08/08/14.
 */
public interface ValidatorEngine {
    public ValidatorResult validate();
    public void setPathScript(String pathScript);
    public void setData(Object data);
    public void setConstrains(List<String> constrains);
}
