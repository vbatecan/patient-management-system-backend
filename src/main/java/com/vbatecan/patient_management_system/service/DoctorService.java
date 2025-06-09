package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO save(DoctorDTO doctorDTO);
    Optional<DoctorDTO> findById(Integer id);
    Optional<DoctorDTO> findByUserAccountId(Integer userAccountId);
    Optional<DoctorDTO> findByEmail(String email);
    List<DoctorDTO> findAll();
    DoctorDTO update(Integer id, DoctorDTO doctorDTO);
    void delete(Integer id);
}
