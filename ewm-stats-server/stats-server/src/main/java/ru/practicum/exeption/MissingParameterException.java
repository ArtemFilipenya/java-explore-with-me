package ru.practicum.exeption;

public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String paramName) {
        super("Отсутствует обязательный параметр: " + paramName);
    }
}

