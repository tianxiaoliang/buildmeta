package org.flysnow.cloud.buildmeta.ui.resteasy.exception;

import java.io.Serializable;

public class BuildMetadataServiceException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 7786141544419367058L;
	
	public BuildMetadataServiceException(){
		super();
	}
	
	public BuildMetadataServiceException(String message, Throwable cause){
		super(message, cause);
	}
	public BuildMetadataServiceException(Throwable cause){
		super(cause);
	}
	public BuildMetadataServiceException(String msg){
		super(msg);
	}

}
