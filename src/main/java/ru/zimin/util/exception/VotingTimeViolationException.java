package ru.zimin.util.exception;

public class VotingTimeViolationException extends RuntimeException {
    public VotingTimeViolationException(String message) {
        super(message);
    }
}
