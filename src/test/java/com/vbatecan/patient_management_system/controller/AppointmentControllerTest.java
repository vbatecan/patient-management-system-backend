package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDTO appointmentDTO;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1);
        appointmentDTO.setPatientId(1);
        appointmentDTO.setDoctorId(1);
        appointmentDTO.setAppointmentDate(LocalDateTime.now());
        appointmentDTO.setStatus("Scheduled");
        appointmentDTO.setReason("Check-up");
    }

    @Test
    void createAppointment_shouldReturnCreatedAppointment() throws Exception {
        when(appointmentService.save(any(AppointmentDTO.class))).thenReturn(appointmentDTO);

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(appointmentDTO.getId()))
                .andExpect(jsonPath("$.status").value("Scheduled"));

        verify(appointmentService, times(1)).save(any(AppointmentDTO.class));
    }

    @Test
    void getAppointmentById_whenExists_shouldReturnAppointment() throws Exception {
        when(appointmentService.findById(1)).thenReturn(Optional.of(appointmentDTO));

        mockMvc.perform(get("/api/v1/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentDTO.getId()));

        verify(appointmentService, times(1)).findById(1);
    }

    @Test
    void getAppointmentById_whenNotExists_shouldReturnNotFound() throws Exception {
        when(appointmentService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/appointments/1"))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(1);
    }

    @Test
    void getAllAppointments_shouldReturnPageOfAppointments() throws Exception {
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(Collections.singletonList(appointmentDTO), PageRequest.of(0, 10), 1);
        when(appointmentService.findAll(any(Pageable.class))).thenReturn(appointmentPage);

        mockMvc.perform(get("/api/v1/appointments?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(appointmentDTO.getId()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(appointmentService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAppointmentsByPatientId_shouldReturnPageOfAppointments() throws Exception {
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(Collections.singletonList(appointmentDTO), PageRequest.of(0, 10), 1);
        when(appointmentService.findByPatientId(eq(1), any(Pageable.class))).thenReturn(appointmentPage);

        mockMvc.perform(get("/api/v1/appointments/patient/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].patientId").value(1));

        verify(appointmentService, times(1)).findByPatientId(eq(1), any(Pageable.class));
    }

    @Test
    void getAppointmentsByDoctorId_shouldReturnPageOfAppointments() throws Exception {
        Page<AppointmentDTO> appointmentPage = new PageImpl<>(Collections.singletonList(appointmentDTO), PageRequest.of(0, 10), 1);
        when(appointmentService.findByDoctorId(eq(1), any(Pageable.class))).thenReturn(appointmentPage);

        mockMvc.perform(get("/api/v1/appointments/doctor/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].doctorId").value(1));

        verify(appointmentService, times(1)).findByDoctorId(eq(1), any(Pageable.class));
    }

    @Test
    void updateAppointment_whenExists_shouldReturnUpdatedAppointment() throws Exception {
        AppointmentDTO updatedDetails = new AppointmentDTO();
        updatedDetails.setStatus("Completed");
        updatedDetails.setReason("Follow-up");

        AppointmentDTO updatedAppointment = new AppointmentDTO();
        updatedAppointment.setId(1);
        updatedAppointment.setPatientId(1);
        updatedAppointment.setDoctorId(1);
        updatedAppointment.getAppointmentDate(appointmentDTO.getAppointmentDate());
        updatedAppointment.setStatus("Completed");
        updatedAppointment.setReason("Follow-up");


        when(appointmentService.update(eq(1), any(AppointmentDTO.class))).thenReturn(updatedAppointment);

        mockMvc.perform(put("/api/v1/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.reason").value("Follow-up"));

        verify(appointmentService, times(1)).update(eq(1), any(AppointmentDTO.class));
    }

    @Test
    void updateAppointment_whenNotExists_shouldReturnNotFound() throws Exception {
        AppointmentDTO updatedDetails = new AppointmentDTO();
        updatedDetails.setStatus("Completed");

        when(appointmentService.update(eq(1), any(AppointmentDTO.class))).thenThrow(new ResourceNotFoundException("Appointment not found"));

        mockMvc.perform(put("/api/v1/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).update(eq(1), any(AppointmentDTO.class));
    }

    @Test
    void updateAppointmentStatus_whenExists_shouldReturnUpdatedAppointment() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "Cancelled");

        AppointmentDTO updatedAppointment = new AppointmentDTO();
        updatedAppointment.setId(1);
        updatedAppointment.setStatus("Cancelled");


        when(appointmentService.updateAppointmentStatus(eq(1), eq("Cancelled"))).thenReturn(updatedAppointment);

        mockMvc.perform(patch("/api/v1/appointments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Cancelled"));

        verify(appointmentService, times(1)).updateAppointmentStatus(eq(1), eq("Cancelled"));
    }

    @Test
    void updateAppointmentStatus_whenNotExists_shouldReturnNotFound() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "Cancelled");

        when(appointmentService.updateAppointmentStatus(eq(1), eq("Cancelled"))).thenThrow(new ResourceNotFoundException("Appointment not found"));

        mockMvc.perform(patch("/api/v1/appointments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).updateAppointmentStatus(eq(1), eq("Cancelled"));
    }

    @Test
    void updateAppointmentStatus_whenStatusIsEmpty_shouldReturnBadRequest() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", " "); // Empty or blank status

        mockMvc.perform(patch("/api/v1/appointments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isBadRequest());

        verify(appointmentService, never()).updateAppointmentStatus(anyInt(), anyString());
    }
    
    @Test
    void updateAppointmentStatus_whenStatusIsNull_shouldReturnBadRequest() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", null);

        mockMvc.perform(patch("/api/v1/appointments/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isBadRequest());

        verify(appointmentService, never()).updateAppointmentStatus(anyInt(), anyString());
    }


    @Test
    void deleteAppointment_whenExists_shouldReturnNoContent() throws Exception {
        doNothing().when(appointmentService).delete(1);

        mockMvc.perform(delete("/api/v1/appointments/1"))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).delete(1);
    }

    @Test
    void deleteAppointment_whenNotExists_shouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Appointment not found")).when(appointmentService).delete(1);

        mockMvc.perform(delete("/api/v1/appointments/1"))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).delete(1);
    }
}
