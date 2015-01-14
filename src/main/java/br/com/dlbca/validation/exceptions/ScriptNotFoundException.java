package br.com.dlbca.validation.exceptions;


/**
 * Created by guilherme on 08/08/14.
 */
public class ScriptNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4445877820949992634L;

	public ScriptNotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
