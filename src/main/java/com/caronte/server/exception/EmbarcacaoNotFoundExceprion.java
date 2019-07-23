package com.caronte.server.exception;

public class EmbarcacaoNotFoundExceprion extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmbarcacaoNotFoundExceprion(Long id) {
		 super("Could not find proprietario " + id);
	}
}
