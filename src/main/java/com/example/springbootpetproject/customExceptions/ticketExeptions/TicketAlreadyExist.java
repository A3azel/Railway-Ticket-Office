package com.example.springbootpetproject.customExceptions.ticketExeptions;

public class TicketAlreadyExist extends Throwable{
    public TicketAlreadyExist() {
        super();
    }

    public TicketAlreadyExist(String message) {
        super(message);
    }
}
