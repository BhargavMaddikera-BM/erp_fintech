package com.blackstrawai;

public class ApplicationException extends Exception{
	  
    private static final long serialVersionUID = 1L;
    
    public ApplicationException(String s,Throwable e) 
    { 
        super(s,e); 
    } 
    
    public ApplicationException(String s) 
    { 
        super(s); 
    } 
    
    public ApplicationException(Throwable e) 
    { 
        super(e); 
    } 
    

}
