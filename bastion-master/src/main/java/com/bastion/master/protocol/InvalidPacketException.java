package com.bastion.master.protocol;

public class InvalidPacketException extends RuntimeException{

   public InvalidPacketException() {
        super();
    }
 
    public InvalidPacketException(String message) {
        super(message);
    }
 
    public InvalidPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}
