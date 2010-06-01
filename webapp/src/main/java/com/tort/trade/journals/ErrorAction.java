package com.tort.trade.journals;

public class ErrorAction implements Action{
    private final String _message;

    public ErrorAction(String message) {
        _message = message;
    }

    @Override
    public View act() {
        return new ErrorView(_message);
    }
}
