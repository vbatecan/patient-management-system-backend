package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    Optional<AppointmentDTO> getAppointmentById(Integer id);
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAppointmentsByPatientId(Integer patientId);
    List<AppointmentDTO> getAppointmentsByDoctorId(Integer doctorId);
    AppointmentDTO updateAppointment(Integer id, AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointmentStatus(Integer id, String status);
    void deleteAppointment(Integer id);
}
