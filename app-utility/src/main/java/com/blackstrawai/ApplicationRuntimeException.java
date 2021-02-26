package com.blackstrawai;

public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    
    public ApplicationRuntimeException(String s,Throwable e) 
    { 
        super(s,e); 
    } 
    
    public ApplicationRuntimeException(String s) 
    { 
        super(s); 
    } 
    
    public ApplicationRuntimeException(Throwable e) 
    { 
        super(e); 
    } 
}
