package com.vbatecan.patient_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.patient_management_system.dto.BillingDTO;
import com.vbatecan.patient_management_system.exception.GlobalExceptionHandler;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.service.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(BillingController.class)
@Import(GlobalExceptionHandler.class) // To ensure ResourceNotFoundException is handled
class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BillingDTO billingDTO;

    @BeforeEach
    void setUp() {
        billingDTO = new BillingDTO();
        billingDTO.setId(1);
        billingDTO.setPatientId(1);
        billingDTO.setAppointmentId(1);
        billingDTO.setAmount(new BigDecimal("100.00"));
        billingDTO.setBillingDate(LocalDateTime.now());
        billingDTO.setStatus("Pending");
        billingDTO.setDetails("Consultation Fee");
    }

    @Test
    void createBilling_shouldReturnCreatedBilling() throws Exception {
        when(billingService.save(any(BillingDTO.class))).thenReturn(billingDTO);

        mockMvc.perform(post("/api/v1/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(billingDTO.getId()))
                .andExpect(jsonPath("$.status").value("Pending"));

        verify(billingService, times(1)).save(any(BillingDTO.class));
    }

    @Test
    void createBilling_whenServiceThrowsIllegalArgument_shouldReturnBadRequest() throws Exception {
        String errorMessage = "Patient ID or Appointment ID cannot be null";
        when(billingService.save(any(BillingDTO.class))).thenThrow(new IllegalArgumentException(errorMessage));

        mockMvc.perform(post("/api/v1/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BillingDTO()))) // Empty DTO
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(billingService, times(1)).save(any(BillingDTO.class));
    }
    
    @Test
    void createBilling_whenServiceThrowsResourceNotFound_shouldReturnNotFound() throws Exception {
        when(billingService.save(any(BillingDTO.class))).thenThrow(new ResourceNotFoundException("Patient not found"));

        mockMvc.perform(post("/api/v1/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billingDTO)))
                .andExpect(status().isNotFound()); // Handled by GlobalExceptionHandler

        verify(billingService, times(1)).save(any(BillingDTO.class));
    }

    @Test
    void getBillingById_whenExists_shouldReturnBilling() throws Exception {
        when(billingService.findById(1)).thenReturn(Optional.of(billingDTO));

        mockMvc.perform(get("/api/v1/billings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(billingDTO.getId()));

        verify(billingService, times(1)).findById(1);
    }

    @Test
    void getBillingById_whenNotExists_shouldReturnNotFound() throws Exception {
        when(billingService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/billings/1"))
                .andExpect(status().isNotFound());

        verify(billingService, times(1)).findById(1);
    }

    @Test
    void getAllBillings_shouldReturnPageOfBillings() throws Exception {
        Page<BillingDTO> billingPage = new PageImpl<>(Collections.singletonList(billingDTO), PageRequest.of(0, 10), 1);
        when(billingService.findAll(any(Pageable.class))).thenReturn(billingPage);

        mockMvc.perform(get("/api/v1/billings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(billingDTO.getId()))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(billingService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getBillingsByPatientId_shouldReturnPageOfBillings() throws Exception {
        Page<BillingDTO> billingPage = new PageImpl<>(Collections.singletonList(billingDTO), PageRequest.of(0, 10), 1);
        when(billingService.findByPatientId(eq(1), any(Pageable.class))).thenReturn(billingPage);

        mockMvc.perform(get("/api/v1/billings/patient/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].patientId").value(1));

        verify(billingService, times(1)).findByPatientId(eq(1), any(Pageable.class));
    }
    
    @Test
    void getBillingsByPatientId_whenPatientNotFound_shouldReturnNotFound() throws Exception {
        when(billingService.findByPatientId(eq(1), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Patient not found"));

        mockMvc.perform(get("/api/v1/billings/patient/1?page=0&size=10"))
                .andExpect(status().isNotFound());

        verify(billingService, times(1)).findByPatientId(eq(1), any(Pageable.class));
    }

    @Test
    void getBillingsByAppointmentId_shouldReturnPageOfBillings() throws Exception {
        Page<BillingDTO> billingPage = new PageImpl<>(Collections.singletonList(billingDTO), PageRequest.of(0, 10), 1);
        when(billingService.findByAppointmentId(eq(1), any(Pageable.class))).thenReturn(billingPage);

        mockMvc.perform(get("/api/v1/billings/appointment/1?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].appointmentId").value(1));

        verify(billingService, times(1)).findByAppointmentId(eq(1), any(Pageable.class));
    }

    @Test
    void getBillingsByAppointmentId_whenAppointmentNotFound_shouldReturnNotFound() throws Exception {
        when(billingService.findByAppointmentId(eq(1), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Appointment not found"));

        mockMvc.perform(get("/api/v1/billings/appointment/1?page=0&size=10"))
                .andExpect(status().isNotFound());

        verify(billingService, times(1)).findByAppointmentId(eq(1), any(Pageable.class));
    }


    @Test
    void updateBilling_whenExists_shouldReturnUpdatedBilling() throws Exception {
        BillingDTO updatedDetails = new BillingDTO();
        updatedDetails.setStatus("Paid");
        updatedDetails.setAmount(new BigDecimal("150.00"));

        BillingDTO updatedBilling = new BillingDTO();
        updatedBilling.setId(1);
        updatedBilling.setStatus("Paid");
        updatedBilling.setAmount(new BigDecimal("150.00"));


        when(billingService.update(eq(1), any(BillingDTO.class))).thenReturn(updatedBilling);

        mockMvc.perform(put("/api/v1/billings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Paid"))
                .andExpect(jsonPath("$.amount").value(150.00));

        verify(billingService, times(1)).update(eq(1), any(BillingDTO.class));
    }

    @Test
    void updateBilling_whenNotExists_shouldReturnNotFound() throws Exception {
        BillingDTO updatedDetails = new BillingDTO();
        updatedDetails.setStatus("Paid");

        when(billingService.update(eq(1), any(BillingDTO.class))).thenThrow(new ResourceNotFoundException("Billing not found"));

        mockMvc.perform(put("/api/v1/billings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound()); // Handled by GlobalExceptionHandler

        verify(billingService, times(1)).update(eq(1), any(BillingDTO.class));
    }

    @Test
    void updateBillingStatus_whenExists_shouldReturnUpdatedBilling() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "Paid");

        BillingDTO updatedBilling = new BillingDTO();
        updatedBilling.setId(1);
        updatedBilling.setStatus("Paid");

        when(billingService.updateBillingStatus(eq(1), eq("Paid"))).thenReturn(updatedBilling);

        mockMvc.perform(patch("/api/v1/billings/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Paid"));

        verify(billingService, times(1)).updateBillingStatus(eq(1), eq("Paid"));
    }

    @Test
    void updateBillingStatus_whenNotExists_shouldReturnNotFound() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "Paid");

        when(billingService.updateBillingStatus(eq(1), eq("Paid"))).thenThrow(new ResourceNotFoundException("Billing not found"));

        mockMvc.perform(patch("/api/v1/billings/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isNotFound()); // Handled by GlobalExceptionHandler

        verify(billingService, times(1)).updateBillingStatus(eq(1), eq("Paid"));
    }

    @Test
    void updateBillingStatus_whenStatusIsEmpty_shouldReturnBadRequest() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", " ");

        mockMvc.perform(patch("/api/v1/billings/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist()); // Expecting empty body from controller

        verify(billingService, never()).updateBillingStatus(anyInt(), anyString());
    }
    
    @Test
    void updateBillingStatus_whenStatusIsNull_shouldReturnBadRequest() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", null);

        mockMvc.perform(patch("/api/v1/billings/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist()); // Expecting empty body from controller

        verify(billingService, never()).updateBillingStatus(anyInt(), anyString());
    }

    @Test
    void deleteBilling_whenExists_shouldReturnNoContent() throws Exception {
        doNothing().when(billingService).delete(1);

        mockMvc.perform(delete("/api/v1/billings/1"))
                .andExpect(status().isNoContent());

        verify(billingService, times(1)).delete(1);
    }

    @Test
    void deleteBilling_whenNotExists_shouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Billing not found")).when(billingService).delete(1);

        mockMvc.perform(delete("/api/v1/billings/1"))
                .andExpect(status().isNotFound()); // Handled by GlobalExceptionHandler

        verify(billingService, times(1)).delete(1);
    }
}
