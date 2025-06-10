package com.vbatecan.patient_management_system.service.impl;

import com.vbatecan.patient_management_system.dto.AppointmentDTO;
import com.vbatecan.patient_management_system.exception.ResourceNotFoundException;
import com.vbatecan.patient_management_system.model.Appointment;
import com.vbatecan.patient_management_system.model.Doctor;
import com.vbatecan.patient_management_system.model.Patient;
import com.vbatecan.patient_management_system.repository.AppointmentRepository;
import com.vbatecan.patient_management_system.repository.DoctorRepository;
import com