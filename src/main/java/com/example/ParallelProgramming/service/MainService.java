package com.example.ParallelProgramming.service;

import com.example.ParallelProgramming.dao.CandidateDAO;
import com.example.ParallelProgramming.dao.ProjectDAO;
import com.example.ParallelProgramming.model.Candidate;
import com.example.ParallelProgramming.model.CandidateProjectDetails;
import com.example.ParallelProgramming.model.CandidateProjectMapping;
import com.example.ParallelProgramming.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MainService {

    private static final Logger log = LoggerFactory.getLogger(MainService.class);

    @Autowired
    private CandidateDAO employeeRepo;

    @Autowired
    private ProjectDAO projectRepo;

    public Candidate getEmployeeById(Integer empId) {
        CompletableFuture<Candidate> completableFuture = employeeRepo.getEmployeeById(empId);
        Candidate employee = null;
        try {
            employee = completableFuture.get();
        } catch (Exception exception) {
            log.error("Exception occurred while fetching employee details for empId - " + empId);
        }
        return employee;
    }

    public List<Candidate> getAllEmployee() {
        CompletableFuture<List<Candidate>> empListFuture = employeeRepo.getAllEmployee();
        List<Candidate> employeeList = new ArrayList<>();
        try {
            employeeList = empListFuture.get();
        } catch (Exception ex) {
            log.error("Exception occurred while fetching employee list from future object");
        }
        return employeeList;
    }

    public CandidateProjectDetails getAllEmployeeProjectsById(Integer empId) {
        List<CandidateProjectMapping> empProjectMappings = employeeRepo.getAllProjectOfEmp(empId);
        List<CompletableFuture<Project>> projectList = new ArrayList<>();
        empProjectMappings.forEach(empProjectMapping -> {
            CompletableFuture<Project> project = projectRepo.getProjectById(empProjectMapping.getProjectId());
            projectList.add(project);
        });
        log.info("Waiting for all projects to be fetched from DB for empId -" + empId);
        CompletableFuture.allOf(projectList.toArray(new CompletableFuture[empProjectMappings.size()])).join();

        Candidate employee = getEmployeeById(empId);
        List<Project> projects = new ArrayList<>();
        for (CompletableFuture<Project> projectCompletableFuture : projectList) {
            try {
                Project project = projectCompletableFuture.get();
                if (project != null)
                    projects.add(project);
            } catch (Exception e) {
                log.error("Exception occurred while getting projectList from future object");
            }
        }
        return mapEmpProjectDetails(employee, projects);
    }

    public CandidateProjectDetails mapEmpProjectDetails(Candidate candidate, List<Project> projectList) {
        CandidateProjectDetails employeeProjectDetails = new CandidateProjectDetails();
        if (candidate != null) {
            employeeProjectDetails.setEmpId(candidate.getEmpId());
            employeeProjectDetails.setEmpName(candidate.getFirstName() + " " + candidate.getLastName());
            employeeProjectDetails.setEmail(candidate.getEmail());
            employeeProjectDetails.setPhoneNo(candidate.getPhoneNo());
            employeeProjectDetails.setDestination(candidate.getDestination());
            employeeProjectDetails.setLocation(candidate.getLocation());
        }
        if (projectList != null) {
            employeeProjectDetails.setProjects(projectList);
        }
        return employeeProjectDetails;
    }
}
