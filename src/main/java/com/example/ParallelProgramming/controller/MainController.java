package com.example.ParallelProgramming.controller;

import com.example.ParallelProgramming.model.Candidate;
import com.example.ParallelProgramming.model.CandidateProjectDetails;
import com.example.ParallelProgramming.model.Response;
import com.example.ParallelProgramming.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/async")
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/id")
    public Response<Candidate> getCandidateById(@PathVariable int id) {
        Response<Candidate> response = new Response<>();
        Candidate candidate = mainService.getEmployeeById(id);
        if (candidate != null) {
            response.setData(candidate);
            response.setSuccessMessage("Fetched candidate successfully");
        } else {
            response.setErrorMessage("Not found any data for given candidate Id");
        }
        return response;
    }

    @GetMapping("/all")
    public Response<List<Candidate>> getAllCandidates() {
        Response<List<Candidate>> response = new Response<>();
        List<Candidate> employeeList = mainService.getAllEmployee();
        if (employeeList != null && employeeList.size() > 0) {
            response.setData(employeeList);
            response.setSuccessMessage("Got all active record successfully");
        } else {
            response.setErrorMessage("Not found any active records");
        }
        return response;
    }

    @GetMapping("/emp/projects/empId")
    public Response<CandidateProjectDetails> getAllProjectsOfEmp(@PathVariable int empId) {
        Response<CandidateProjectDetails> response = new Response<>();
        CandidateProjectDetails employeeProjectDetails = mainService.getAllEmployeeProjectsById(empId);
        if (employeeProjectDetails != null) {
            response.setData(employeeProjectDetails);
            response.setSuccessMessage("Got all projects of " + employeeProjectDetails.getEmpName());
        } else {
            response.setErrorMessage("Not found any projects for employee id - " + empId);
        }
        return response;
    }
}
