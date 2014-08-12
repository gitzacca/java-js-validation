package br.com.dlbca.validation.exceptions;

import java.io.FileNotFoundException;

/**
 * Created by guilherme on 08/08/14.
 */
public class ScriptNotFoundException extends RuntimeException {
    public ScriptNotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
