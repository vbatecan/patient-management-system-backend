package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.PatientDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.model.UserAccount;
import com.vbatecan.patient_management_system.repository.PatientRepository;
import com.vbatecan.patient_management_system.repository.UserAccountRepository;
import com.vbatecan.patient_management_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public PatientDTO save(PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }

    @Override
    public Optional<PatientDTO> findById(Integer id) { // Renamed from getPatientById
        return patientRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<PatientDTO> findAll() { // Renamed from getAllPatients
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientDTO update(Integer id, PatientDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        existingPatient.setFirstName(patientDTO.getFirstName());
        existingPatient.setLastName(patientDTO.getLastName());
        existingPatient.setDateOfBirth(patientDTO.getDateOfBirth());
        existingPatient.setGender(patientDTO.getGender());
        existingPatient.setContactNumber(patientDTO.getContactNumber());
        existingPatient.setEmail(patientDTO.getEmail());
        existingPatient.setAddress(patientDTO.getAddress());
        existingPatient.setEmergencyContact(patientDTO.getEmergencyContact());
        existingPatient.setUpdatedAt(LocalDateTime.now());

        if (patientDTO.getUserAccountId() != null) {
            UserAccount userAccount = userAccountRepository.findById(patientDTO.getUserAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + patientDTO.getUserAccountId()));
            existingPatient.setUserAccount(userAccount);
        } else {
            existingPatient.setUserAccount(null);
        }

        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToDTO(updatedPatient);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        if (patient.getUserAccount() != null) {
            dto.setUserAccountId(patient.getUserAccount().getId());
        }
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setContactNumber(patient.getContactNumber());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setEmergencyContact(patient.getEmergencyContact());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        return dto;
    }

    private Patient convertToEntity(PatientDTO patientDTO) {
        Patient patient = new Patient();
        if (patientDTO.getUserAccountId() != null) {
            UserAccount userAccount = userAccountRepository.findById(patientDTO.getUserAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserAccount not found with id: " + patientDTO.getUserAccountId()));
            patient.setUserAccount(userAccount);
        }
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setGender(patientDTO.getGender());
        patient.setContactNumber(patientDTO.getContactNumber());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setEmergencyContact(patientDTO.getEmergencyContact());
        return patient;
    }
}
