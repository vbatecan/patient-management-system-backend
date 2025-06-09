package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    Optional<DoctorDTO> getDoctorById(Integer id);
    Optional<DoctorDTO> getDoctorByUserAccountId(Integer userAccountId);
    Optional<DoctorDTO> getDoctorByEmail(String email);
    List<DoctorDTO> getAllDoctors();
    DoctorDTO updateDoctor(Integer id, DoctorDTO doctorDTO);
    void deleteDoctor(Integer id);
}
