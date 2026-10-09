package ru.webip.autoservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.webip.autoservice.dto.AppointmentRq;
import ru.webip.autoservice.dto.AppointmentRs;
import ru.webip.autoservice.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Записи", description = "CRUD-операции с записями на ремонт и ТО")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @Operation(summary = "Получить список записей")
    public List<AppointmentRs> findAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись по идентификатору")
    public AppointmentRs findById(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать запись")
    public AppointmentRs create(@Valid @RequestBody AppointmentRq request) {
        return appointmentService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись")
    public AppointmentRs update(@PathVariable Long id, @Valid @RequestBody AppointmentRq request) {
        return appointmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить запись")
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}
