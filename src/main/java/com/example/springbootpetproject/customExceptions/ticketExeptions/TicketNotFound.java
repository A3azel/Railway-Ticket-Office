package com.example.springbootpetproject.customExceptions.ticketExeptions;

public class TicketNotFound extends RuntimeException{
    public TicketNotFound() {
        super();
    }

    public TicketNotFound(String message) {
        super(message);
    }
}
