package br.com.dlbca.validation.exceptions;

import javax.script.ScriptException;

/**
 * Created by guilherme on 08/08/14.
 */
public class ScriptErrorException extends RuntimeException {
    public ScriptErrorException(String message, Exception e) {
        super(message, e);
    }
}
