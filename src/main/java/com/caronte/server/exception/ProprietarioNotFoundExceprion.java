package com.caronte.server.exception;

public class ProprietarioNotFoundExceprion extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProprietarioNotFoundExceprion(Long id) {
		 super("Could not find proprietario " + id);
	}
}
