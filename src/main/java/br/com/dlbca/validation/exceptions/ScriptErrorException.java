package br.com.dlbca.validation.exceptions;


/**
 * Created by guilherme on 08/08/14.
 */
public class ScriptErrorException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3336340892502076137L;

	public ScriptErrorException(String message, Exception e) {
        super(message, e);
    }
}
