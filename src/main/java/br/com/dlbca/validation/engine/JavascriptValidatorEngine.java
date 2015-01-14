package br.com.dlbca.validation.engine;

import br.com.dlbca.validation.exceptions.ScriptErrorException;
import br.com.dlbca.validation.exceptions.ScriptNotFoundException;

import javax.script.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * Created by guilherme on 07/08/14.
 */
public class JavascriptValidatorEngine implements ValidatorEngine {
    private String pathScript;
    private Object data;
    private List<String> constrains;
    private final ScriptEngine engine;
    private Bindings variables;

    public JavascriptValidatorEngine() {
        this.engine = new ScriptEngineManager().getEngineByName("js");
        variables = new SimpleBindings();
        this.engine.setBindings(variables, ScriptContext.ENGINE_SCOPE);
    }

    public void setPathScript(String pathScript) {
        this.pathScript = pathScript;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setConstrains(List<String> constrains) {
        this.constrains = constrains;
    }

    @Override
    public ValidatorResult validate() {
        instanceJsScript(data, constrains);

        runJsCommand("var validator = new Validator(data, constrains); var vResult = validator.validate();");

        return new ValidatorResult() {
            @Override
            public boolean hasErrors() {
                runJsCommand("var hasErrors = vResult.hasErrors();");

                return (boolean) engine.get("hasErrors");
            }

			@Override
			@SuppressWarnings("unchecked")
            public Map<String, Object> getAllFailures() {
                runJsCommand("var allErrors = vResult.getAllFailures();");
                return (Map<String, Object>) engine.get("allErrors");
            }

			@Override
			@SuppressWarnings("unchecked")
            public List<String> getError(String field) {
                variables.put("field", field);
                runJsCommand("var error = vResult.getError(field);");


                return (List<String>) engine.get("error");
            }
        };
    }

    private void runJsCommand(String command) {
        try {
            engine.eval(command);
        } catch (ScriptException e) {
            throw new ScriptErrorException("Script Error: " +
                    "Verify script validation " +
                    "contains " +
                    "Validator with method validate(); " +
                    "ValidatorResult with methods " +
                    "hasErrors()", e);
        }
    }

    private void instanceJsScript(Object data, List<String> constrains) {
        variables.put("data", data);
        variables.put("constrains", constrains.toArray());

        try {
            this.engine.eval(new FileReader(JavascriptValidatorEngine.class.getResource(pathScript).getFile()));
        } catch (FileNotFoundException | NullPointerException e) {
            throw new ScriptNotFoundException("Not Found Script", e);
        } catch (ScriptException e) {
            throw new ScriptErrorException("Script Error", e);
        }
    }
}