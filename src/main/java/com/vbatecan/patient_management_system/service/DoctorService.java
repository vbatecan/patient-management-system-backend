package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO save(DoctorDTO doctorDTO); // Renamed from createDoctor
    Optional<DoctorDTO> getDoctorById(Integer id);
    Optional<DoctorDTO> getDoctorByUserAccountId(Integer userAccountId);
    Optional<DoctorDTO> getDoctorByEmail(String email);
    List<DoctorDTO> getAllDoctors();
    DoctorDTO update(Integer id, DoctorDTO doctorDTO); // Renamed from updateDoctor
    void delete(Integer id); // Renamed from deleteDoctor
}
