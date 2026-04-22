package uk.ac.plymouth.maternityapi.service;

import org.springframework.stereotype.Service;
import uk.ac.plymouth.maternityapi.client.MaternityApiClient;
import uk.ac.plymouth.maternityapi.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class MaternityService {

    private final MaternityApiClient apiClient;

    public MaternityService(MaternityApiClient apiClient) {
        this.apiClient = apiClient;
    }

    // F1
    public List<String> getRoomsUsedByPatient(int patientId) {
        List<Admission> admissions = apiClient.getAdmissions();
        List<RoomAllocation> roomAllocations = apiClient.getRoomAllocations();

        Set<Integer> admissionIds = admissions.stream()
                .filter(admission -> admission.getPatientId() == patientId)
                .map(Admission::getId)
                .collect(Collectors.toSet());

        return roomAllocations.stream()
                .filter(roomAllocation -> admissionIds.contains(roomAllocation.getAdmissionId()))
                .map(RoomAllocation::getRoomNumber)
                .filter(roomNumber -> roomNumber != null && !roomNumber.isBlank())
                .distinct()
                .toList();
    }

    // F2
    public List<String> getPatientsInRoomLast7Days(String roomNumber) {
        List<Admission> admissions = apiClient.getAdmissions();
        List<RoomAllocation> roomAllocations = apiClient.getRoomAllocations();
        List<Patient> patients = apiClient.getPatients();

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        Map<Integer, String> patientNamesById = patients.stream()
                .collect(Collectors.toMap(
                        Patient::getId,
                        patient -> {
                            if (patient.getSurname() != null && !patient.getSurname().isBlank()) {
                                return patient.getSurname();
                            }
                            return "Patient " + patient.getId();
                        }
                ));

        Set<Integer> matchingAdmissionIds = roomAllocations.stream()
                .filter(roomAllocation -> roomAllocation.getRoomNumber() != null)
                .filter(roomAllocation -> roomAllocation.getRoomNumber().equalsIgnoreCase(roomNumber))
                .map(RoomAllocation::getAdmissionId)
                .collect(Collectors.toSet());

        return admissions.stream()
                .filter(admission -> matchingAdmissionIds.contains(admission.getId()))
                .filter(admission -> {
                    LocalDateTime admissionDate = LocalDateTime.parse(admission.getAdmissionDate());
                    return admissionDate.isAfter(sevenDaysAgo) || admissionDate.isEqual(sevenDaysAgo);
                })
                .map(Admission::getPatientId)
                .distinct()
                .map(patientId -> patientNamesById.getOrDefault(patientId, "Patient " + patientId))
                .toList();
    }

    // F3
    public List<String> getLeastUsedRooms() {
        List<RoomAllocation> roomAllocations = apiClient.getRoomAllocations();

        Map<String, Long> roomUsage = roomAllocations.stream()
                .map(RoomAllocation::getRoomNumber)
                .filter(room -> room != null && !room.isBlank())
                .collect(Collectors.groupingBy(room -> room, Collectors.counting()));

        if (roomUsage.isEmpty()) {
            return List.of();
        }

        long minUsage = roomUsage.values().stream()
                .min(Long::compareTo)
                .orElse(0L);

        return roomUsage.entrySet().stream()
                .filter(entry -> entry.getValue() == minUsage)
                .map(Map.Entry::getKey)
                .toList();
    }

    // F4
    public List<String> getStaffWithThreeOrMoreConcurrentPatients() {
        List<Allocation> allocations = apiClient.getAllocations();
        List<Employee> employees = apiClient.getEmployees();

        Map<Integer, List<Allocation>> allocationsByEmployee = allocations.stream()
                .collect(Collectors.groupingBy(Allocation::getEmployeeId));

        Map<Integer, String> employeeNamesById = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getId,
                        employee -> employee.getForename() + " " + employee.getSurname()
                ));

        List<String> overloadedStaff = new ArrayList<>();

        for (Map.Entry<Integer, List<Allocation>> entry : allocationsByEmployee.entrySet()) {
            int employeeId = entry.getKey();
            List<Allocation> employeeAllocations = entry.getValue();

            if (hasThreeOrMoreConcurrentAllocations(employeeAllocations)) {
                overloadedStaff.add(employeeNamesById.getOrDefault(employeeId, "Employee " + employeeId));
            }
        }

        return overloadedStaff;
    }

    // 🔥 THIS WAS MISSING (VERY IMPORTANT)
    private boolean hasThreeOrMoreConcurrentAllocations(List<Allocation> allocations) {
        for (Allocation allocation : allocations) {
            LocalDateTime currentStart = LocalDateTime.parse(allocation.getStartTime());

            long overlappingCount = allocations.stream()
                    .filter(other -> {
                        LocalDateTime otherStart = LocalDateTime.parse(other.getStartTime());
                        LocalDateTime otherEnd = LocalDateTime.parse(other.getEndTime());

                        return !currentStart.isBefore(otherStart) && currentStart.isBefore(otherEnd);
                    })
                    .count();

            if (overlappingCount >= 3) {
                return true;
            }
        }

        return false;
    }
}