package br.com.dlbca.validation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.dlbca.validation.exceptions.ScriptNotFoundException;

/**
 * Created by guilherme on 11/08/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/jsvalidator-module.xml", "classpath:/jsvalidator-change-path-script-test.xml"})
public class ValidatorChangePathScriptTest {

    @Autowired
    Validator validator;

    @Test(expected = ScriptNotFoundException.class)
    public void shouldReturnInstanceWithDI() {
        validator.validate(givenPersonWithContacts()).using(givenConstrainsForPerson());
    }

    private Person givenPersonWithContacts() {
        return new Person("Rumpelstiltskin");
    }

    private List<String> givenConstrainsForPerson() {
        List<String> constrains = new ArrayList<>();
        constrains.add("notEmpty($name)");
        return constrains;
    }

    public class Person {
        
    	private String name;

        public Person(String name) {
            this.name = name;
        }

		public String getName() {
			return name;
		}
        
    }

}