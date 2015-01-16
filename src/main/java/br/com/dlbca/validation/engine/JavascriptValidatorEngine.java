package br.com.dlbca.validation.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.core.io.ClassPathResource;

import br.com.dlbca.validation.exceptions.ScriptErrorException;
import br.com.dlbca.validation.exceptions.ScriptNotFoundException;

/**
 * Created by guilherme on 07/08/14.
 */
public class JavascriptValidatorEngine implements ValidatorEngine {
    private String pathScript;
    private Object data;
    private List<String> constrains;
    
    private Context context;
    private JsRuntimeSupport browserSupport = new JsRuntimeSupport();
    private ScriptableObject sharedScope;

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
    	
    	try {
    		context = Context.enter();
    		instanceJsScript(data, constrains);
    		validatePath(pathScript);
    		runJsCommand("var vResult; require(['" + pathScript.replace(".js", "") + "'], function(Validator){vResult = new Validator(data, constrains).validate();})");
    		
    		return new ValidatorResult() {
    			@Override
    			public boolean hasErrors() {
    				return (boolean) runJsCommand("var hasErrors = vResult.hasErrors(); hasErrors;");
    			}
    			
    			@Override
    			@SuppressWarnings("unchecked")
    			public Map<String, Object> getAllFailures() {
    				return (Map<String, Object>) runJsCommand("var allErrors = vResult.getAllFailures(); allErrors;");
    			}
    			
    			@Override
    			@SuppressWarnings({ "unchecked"})
    			public List<String> getError(String field) {
    				sharedScope.put("field", sharedScope, Context.javaToJS(field, sharedScope));
    				return (List<String>) runJsCommand("var error = vResult.getError(field); error;");
    			}
    		};
    		
    	} catch (ScriptErrorException | ScriptNotFoundException e) {
    		throw e;
    	} finally {
    		Context.exit();
    	}
    	
    }

    private Object runJsCommand(String command) {
    	try {
    		return context.evaluateString(sharedScope, command, "execution", 0, null);
    	 } catch (Exception e) {
             throw new ScriptErrorException("Script Error: " +
                     "Verify script validation " +
                     "contains " +
                     "Validator with method validate(); " +
                     "ValidatorResult with methods " +
                     "hasErrors()", e);
         }
    }

    private void instanceJsScript(Object data, List<String> constrains) {
    	
    	sharedScope = context.initStandardObjects(browserSupport, true);
    	
    	String[] names = { "print", "load" };
        sharedScope.defineFunctionProperties(names, sharedScope.getClass(), ScriptableObject.DONTENUM);

        Scriptable argsObj = context.newArray(sharedScope, new Object[] {});
        sharedScope.defineProperty("arguments", argsObj, ScriptableObject.DONTENUM);

		try {
			context.evaluateReader(sharedScope, new InputStreamReader(getInputStream("./r.js")), "require", 1, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Object[] arrayConstraints = new Object[constrains.size()];
        int count = 0;
        
        for (String constraint : constrains) {
			arrayConstraints[count] =  Context.javaToJS(constraint, sharedScope);
			count ++;
		}
        
        sharedScope.put("data", sharedScope, Context.javaToJS(data, sharedScope));
        sharedScope.put("constrains", sharedScope, arrayConstraints);
        
    }
    
    private InputStream getInputStream(String file) {
		try {
			return new ClassPathResource(file).getInputStream();
		} catch (IOException e) {
			throw new ScriptNotFoundException("Script not found: " + file, e);
		}
    }
    
    private void validatePath(String path) {
    	getInputStream(path);
    }
}