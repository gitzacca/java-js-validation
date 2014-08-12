package br.com.dlbca.validation.engine;

import br.com.dlbca.validation.exceptions.ScriptErrorException;
import br.com.dlbca.validation.exceptions.ScriptNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guilherme on 07/08/14.
 */

public class JavasciptValidatorEngineTest {

    private JavascriptValidatorEngine engine;

    @Before
    public void setUp() {
        engine = new JavascriptValidatorEngine();
        engine.setData(givenPersonWithContacts());
        engine.setConstrains(givenConstrainsForPerson());
        engine.setPathScript("/validator.js");
    }

    @Test(expected = ScriptNotFoundException.class)
    public void throwExceptionIfScriptNotFound() {
        engine.setPathScript("/notFound.js");
        engine.validate();
    }

    @Test(expected = ScriptErrorException.class)
    public void throwExceptionIfScriptNotContainsValidator() {
        engine.setPathScript("/validatorWithoutPrincipalClass.js");
        engine.validate();
    }

    @Test(expected = ScriptErrorException.class)
    public void throwExceptionIfScriptNotContainsHasErrorsMethod() {
        engine.setPathScript("/validatorWithoutValidateResult.js");

        ValidatorResult vResult = engine.validate();
        vResult.hasErrors();
    }

    @Test(expected = ScriptErrorException.class)
    public void throwExceptionIfScriptNotContainsGetAllFailures() {
        engine.setPathScript("/validatorWithoutValidateResult.js");

        ValidatorResult vResult = engine.validate();
        vResult.getAllFailures();
    }

    @Test(expected = ScriptErrorException.class)
    public void throwExceptionIfScriptNotContainsGetError() {
        engine.setPathScript("/validatorWithoutValidateResult.js");

        ValidatorResult vResult = engine.validate();
        vResult.getError("name");
    }

    @Test
    public void shouldReturnFalse() {

        ValidatorResult vResult = engine.validate();

        Assert.assertFalse(vResult.hasErrors());
    }

    @Test
    public void shouldReturnNameNotEmpty() {

        ValidatorResult vResult = engine.validate();
        Map<String, Object> allFailures = vResult.getAllFailures();

        List<String> nameFailures = (List) allFailures.get("name");

        Assert.assertEquals(nameFailures.get(0), "notEmpty");
    }

    @Test
    public void shouldReturnListWithFailConstrains() {

        ValidatorResult vResult = engine.validate();
        List<String> nameFailures = (List) vResult.getError("name");

        Assert.assertEquals(nameFailures.get(0), "notEmpty");
    }

    private Person givenPersonWithContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        contacts.add(new Contact("rumpelstiltskin@gmail.com"));
        contacts.add(new Contact("+55 (11) 9666-1666"));

        return new Person("Rumpelstiltskin", contacts);
    }

    private List<String> givenConstrainsForPerson() {
        List<String> constrains = new ArrayList<String>();
        constrains.add("notEmpty($name)");
        return constrains;
    }

    public class Person {
        private String name;
        private ArrayList<Contact> contacts;

        public Person(String name, ArrayList<Contact> contacts) {
            this.contacts = contacts;
            this.name = name;
        }
    }

    public class Contact {
        private String value;

        public Contact(String value) {
            this.value = value;
        }
    }


}
