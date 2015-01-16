package br.com.dlbca.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.core.io.ClassPathResource;

import br.com.dlbca.validation.engine.JsRuntimeSupport;

public class RJsDemo {

	@Test
    public void simpleRhinoTest() throws FileNotFoundException, IOException {
    Context cx = Context.enter();

    final JsRuntimeSupport browserSupport = new JsRuntimeSupport();

    final ScriptableObject sharedScope = cx.initStandardObjects(browserSupport, true);
    sharedScope.put("eita", sharedScope, "esse é o valor da parada");

    String[] names = { "print", "load" };
    sharedScope.defineFunctionProperties(names, sharedScope.getClass(), ScriptableObject.DONTENUM);

    Scriptable argsObj = cx.newArray(sharedScope, new Object[] {});
    sharedScope.defineProperty("arguments", argsObj, ScriptableObject.DONTENUM);

    cx.evaluateReader(sharedScope, new InputStreamReader(getInputStream("./r.js")), "require", 1, null);
    
    cx.evaluateString(sharedScope, "require(['./teste/Validator'], function(Validator){new Validator().validate();})", "test", 0, null);
    
    Context.exit();

  }
	
  private InputStream getInputStream(String file) throws IOException {
	  return new ClassPathResource(file).getInputStream();
  }

	
}
