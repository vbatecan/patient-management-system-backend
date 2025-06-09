package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO save(DoctorDTO doctorDTO);
    Optional<DoctorDTO> findById(Integer id); // Renamed from getDoctorById
    Optional<DoctorDTO> findByUserAccountId(Integer userAccountId); // Renamed from getDoctorByUserAccountId
    Optional<DoctorDTO> findByEmail(String email); // Renamed from getDoctorByEmail
    List<DoctorDTO> findAll(); // Renamed from getAllDoctors
    DoctorDTO update(Integer id, DoctorDTO doctorDTO);
    void delete(Integer id);
}
