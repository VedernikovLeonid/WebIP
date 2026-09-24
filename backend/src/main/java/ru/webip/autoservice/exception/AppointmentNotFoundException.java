package ru.webip.autoservice.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(Long id) {
        super("Запись с идентификатором " + id + " не найдена");
    }
}
