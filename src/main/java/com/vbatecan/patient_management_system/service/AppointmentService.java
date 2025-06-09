package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentDTO save(AppointmentDTO appointmentDTO);
    Optional<AppointmentDTO> findById(Integer id); // Renamed from getAppointmentById
    List<AppointmentDTO> findAll(); // Renamed from getAllAppointments
    List<AppointmentDTO> findByPatientId(Integer patientId); // Renamed from getAppointmentsByPatientId
    List<AppointmentDTO> findByDoctorId(Integer doctorId); // Renamed from getAppointmentsByDoctorId
    AppointmentDTO update(Integer id, AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointmentStatus(Integer id, String status);
    void delete(Integer id);
}
