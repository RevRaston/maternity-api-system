package uk.ac.plymouth.maternityapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.plymouth.maternityapi.model.Admission;
import uk.ac.plymouth.maternityapi.model.RoomAllocation;
import uk.ac.plymouth.maternityapi.model.Patient;
import uk.ac.plymouth.maternityapi.model.Allocation;
import uk.ac.plymouth.maternityapi.model.Employee;

import java.util.List;

@Component
public class MaternityApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public MaternityApiClient(RestTemplate restTemplate,
                              @Value("${external.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public String getAllocationsRaw() {
        String url = baseUrl + "/Allocations";
        return restTemplate.getForObject(url, String.class);
    }

    public String getEmployeesRaw() {
        String url = baseUrl + "/Employees";
        return restTemplate.getForObject(url, String.class);
    }


    public List<Admission> getAdmissions() {
        String url = baseUrl + "/Admissions";

        ResponseEntity<List<Admission>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Admission>>() {}
        );

        return response.getBody();
    }

    public List<RoomAllocation> getRoomAllocations() {
        String url = baseUrl + "/RoomAllocations";

        ResponseEntity<List<RoomAllocation>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoomAllocation>>() {}
        );

        return response.getBody();
    }

    public List<Patient> getPatients() {
        String url = baseUrl + "/Patients";

        ResponseEntity<List<Patient>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Patient>>() {}
        );

        return response.getBody();
    }

    public List<Allocation> getAllocations() {
        String url = baseUrl + "/Allocations";

        ResponseEntity<List<Allocation>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Allocation>>() {}
        );

        return response.getBody();
    }

    public List<Employee> getEmployees() {
        String url = baseUrl + "/Employees";

        ResponseEntity<List<Employee>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {}
        );

        return response.getBody();
    }

}