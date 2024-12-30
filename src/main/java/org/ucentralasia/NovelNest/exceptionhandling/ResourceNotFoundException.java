package org.ucentralasia.NovelNest.exceptionhandling;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

