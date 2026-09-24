package ru.webip.autoservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.webip.autoservice.config.CorsConfig;
import ru.webip.autoservice.exception.GlobalExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@Import({CorsConfig.class, GlobalExceptionHandler.class})
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllReturnsSeededAppointments() throws Exception {
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(5))))
                .andExpect(jsonPath("$[0].clientName").value("Алексей Смирнов"));
    }

    @Test
    void createAppointmentReturnsCreatedResource() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("clientName", "Тестовый клиент");
        request.put("phone", "+7 999 000-00-00");
        request.put("carBrand", "Ford");
        request.put("carModel", "Focus");
        request.put("licensePlate", "Т000ЕЕ 716");
        request.put("serviceType", "Замена ламп");
        request.put("appointmentDate", "2026-10-20");
        request.put("appointmentTime", "12:00");
        request.put("masterName", "Илья Кузнецов");
        request.put("status", "NEW");
        request.put("comment", "Без комментария");

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.carBrand").value("Ford"));
    }

    @Test
    void deleteUnknownAppointmentReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/appointments/999"))
                .andExpect(status().isNotFound());
    }
}
