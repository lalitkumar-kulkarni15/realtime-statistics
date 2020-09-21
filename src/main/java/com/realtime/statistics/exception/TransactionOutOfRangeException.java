package com.realtime.statistics.exception;

public class TransactionOutOfRangeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionOutOfRangeException(String message){
        super(message);
    }

}
