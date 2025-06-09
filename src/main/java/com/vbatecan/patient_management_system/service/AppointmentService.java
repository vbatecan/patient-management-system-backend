package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDTO save(AppointmentDTO appointmentDTO); // Renamed from createAppointment
    Optional<AppointmentDTO> getAppointmentById(Integer id);
    List<AppointmentDTO> getAllAppointments();
    List<AppointmentDTO> getAppointmentsByPatientId(Integer patientId);
    List<AppointmentDTO> getAppointmentsByDoctorId(Integer doctorId);
    AppointmentDTO update(Integer id, AppointmentDTO appointmentDTO); // Renamed from updateAppointment
    AppointmentDTO updateAppointmentStatus(Integer id, String status);
    void delete(Integer id); // Renamed from deleteAppointment
}
