package com.vbatecan.patient_management_system.service;

import com.vbatecan.patient_management_system.dto.DoctorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO save(DoctorDTO doctorDTO);
    Optional<DoctorDTO> findById(Integer id);
    Optional<DoctorDTO> findByUserAccountId(Integer userAccountId);
    Optional<DoctorDTO> findByEmail(String email);
    Page<DoctorDTO> findAll(Pageable pageable);
    DoctorDTO update(Integer id, DoctorDTO doctorDTO);
    void delete(Integer id);
}
