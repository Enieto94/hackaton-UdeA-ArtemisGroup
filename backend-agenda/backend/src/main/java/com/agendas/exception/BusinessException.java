package com.agendas.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
        throw new BusinessException("No puedes consultar fechas pasadas");
    }
}