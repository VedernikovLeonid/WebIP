package ru.webip.autoservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.webip.autoservice.dto.AppointmentDto;
import ru.webip.autoservice.exception.AppointmentNotFoundException;
import ru.webip.autoservice.model.AppointmentStatus;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Записи на обслуживание", description = "CRUD-операции системы записи клиентов автосервиса")
public class AppointmentController {

    private final Map<Long, AppointmentDto> appointments = new LinkedHashMap<>();
    private long nextId = 6L;

    public AppointmentController() {
        appointments.put(1L, seed(1L, "Алексей Смирнов", "+7 900 123-45-67", "Toyota", "Camry", "А123ВС 716",
                "Замена масла и фильтров", "2026-10-12", "10:30", "Илья Кузнецов", AppointmentStatus.CONFIRMED,
                "Проверить уровень технических жидкостей"));
        appointments.put(2L, seed(2L, "Мария Волкова", "+7 901 222-18-09", "Kia", "Rio", "М456ОР 716",
                "Компьютерная диагностика", "2026-10-12", "13:00", "Дмитрий Орлов", AppointmentStatus.NEW,
                "Появился индикатор двигателя"));
        appointments.put(3L, seed(3L, "Иван Петров", "+7 902 700-30-11", "Hyundai", "Solaris", "Е777КХ 116",
                "Шиномонтаж", "2026-10-13", "09:00", "Илья Кузнецов", AppointmentStatus.IN_PROGRESS,
                "Комплект летних шин в багажнике"));
        appointments.put(4L, seed(4L, "Ольга Никитина", "+7 903 444-20-20", "Lada", "Vesta", "Р222АА 716",
                "Замена тормозных колодок", "2026-10-14", "15:30", "Николай Беляев", AppointmentStatus.NEW,
                "Передние колодки"));
        appointments.put(5L, seed(5L, "Сергей Морозов", "+7 904 555-44-33", "Skoda", "Octavia", "Т555РТ 116",
                "Техническое обслуживание ТО-2", "2026-10-15", "11:00", "Дмитрий Орлов", AppointmentStatus.COMPLETED,
                "Забрать автомобиль после 18:00"));
    }

    @GetMapping
    @Operation(summary = "Получить список записей", description = "Возвращает все записи или записи с указанным статусом")
    public List<AppointmentDto> getAll(@RequestParam(required = false) AppointmentStatus status) {
        if (status == null) {
            return new ArrayList<>(appointments.values());
        }
        return appointments.values().stream()
                .filter(appointment -> appointment.getStatus() == status)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись по идентификатору")
    public AppointmentDto getById(@PathVariable Long id) {
        return findById(id);
    }

    @PostMapping
    @Operation(summary = "Создать запись")
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentDto appointment) {
        appointment.setId(nextId++);
        appointments.put(appointment.getId(), appointment);
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + appointment.getId()))
                .body(appointment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись")
    public AppointmentDto update(@PathVariable Long id, @Valid @RequestBody AppointmentDto appointment) {
        findById(id);
        appointment.setId(id);
        appointments.put(id, appointment);
        return appointment;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        findById(id);
        appointments.remove(id);
        return ResponseEntity.noContent().build();
    }

    private AppointmentDto findById(Long id) {
        AppointmentDto appointment = appointments.get(id);
        if (appointment == null) {
            throw new AppointmentNotFoundException(id);
        }
        return appointment;
    }

    private AppointmentDto seed(Long id, String clientName, String phone, String carBrand, String carModel,
                                String licensePlate, String serviceType, String date, String time,
                                String masterName, AppointmentStatus status, String comment) {
        return new AppointmentDto(id, clientName, phone, carBrand, carModel, licensePlate, serviceType,
                LocalDate.parse(date), LocalTime.parse(time), masterName, status, comment);
    }
}
