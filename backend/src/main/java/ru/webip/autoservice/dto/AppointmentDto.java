package ru.webip.autoservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ru.webip.autoservice.model.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Запись клиента на ремонт или техническое обслуживание автомобиля")
public class AppointmentDto {

    @Schema(description = "Уникальный идентификатор", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Имя клиента обязательно")
    @Schema(description = "Имя и фамилия клиента", example = "Алексей Смирнов")
    private String clientName;

    @NotBlank(message = "Телефон клиента обязателен")
    @Pattern(regexp = "^[0-9+()\\- ]{7,20}$", message = "Некорректный формат телефона")
    @Schema(description = "Контактный телефон", example = "+7 900 123-45-67")
    private String phone;

    @NotBlank(message = "Марка автомобиля обязательна")
    @Schema(description = "Марка автомобиля", example = "Toyota")
    private String carBrand;

    @NotBlank(message = "Модель автомобиля обязательна")
    @Schema(description = "Модель автомобиля", example = "Camry")
    private String carModel;

    @NotBlank(message = "Госномер автомобиля обязателен")
    @Schema(description = "Государственный регистрационный номер", example = "А123ВС 716")
    private String licensePlate;

    @NotBlank(message = "Вид работ обязателен")
    @Schema(description = "Запрошенная услуга", example = "Замена масла")
    private String serviceType;

    @NotNull(message = "Дата записи обязательна")
    @Schema(description = "Дата визита", example = "2026-10-12")
    private LocalDate appointmentDate;

    @NotNull(message = "Время записи обязательно")
    @Schema(description = "Время визита", example = "10:30")
    private LocalTime appointmentTime;

    @NotBlank(message = "Имя мастера обязательно")
    @Schema(description = "Назначенный мастер", example = "Илья Кузнецов")
    private String masterName;

    @NotNull(message = "Статус записи обязателен")
    @Schema(description = "Текущий статус записи", example = "CONFIRMED")
    private AppointmentStatus status;

    @Schema(description = "Комментарий клиента", example = "Проверить тормозные колодки")
    private String comment;

    public AppointmentDto() {
    }

    public AppointmentDto(Long id, String clientName, String phone, String carBrand, String carModel,
                          String licensePlate, String serviceType, LocalDate appointmentDate,
                          LocalTime appointmentTime, String masterName, AppointmentStatus status,
                          String comment) {
        this.id = id;
        this.clientName = clientName;
        this.phone = phone;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.licensePlate = licensePlate;
        this.serviceType = serviceType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.masterName = masterName;
        this.status = status;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
