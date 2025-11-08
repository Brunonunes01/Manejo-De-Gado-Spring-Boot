package com.fazenda.manejo.business;

// 1. É uma RuntimeException, o que significa que o Spring
//    saberá fazer "rollback" da transação se ela ocorrer.
public class BusinessException extends RuntimeException {

    // 2. Um construtor que nos permite passar uma mensagem
    public BusinessException(String message) {
        super(message);
    }
}