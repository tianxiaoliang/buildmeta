package org.flysnow.cloud.buildmeta.wsclient;

public class BuildWSClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6306707892919703812L;
    
    public BuildWSClientException(String message, Throwable t) {
        super(message, t);
    }

    public BuildWSClientException(String message) {
        super(message);
    }

}
