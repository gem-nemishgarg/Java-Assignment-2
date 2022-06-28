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
    private CandidateDAO candidateDAO;

    @Autowired
    private ProjectDAO projectDAO;

    public Candidate getCandidateById(Integer cndId) {
        CompletableFuture<Candidate> completableFuture = candidateDAO.getCandidateById(cndId);
        Candidate candidate = null;
        try {
            candidate = completableFuture.get();
        } catch (Exception exception) {
            log.error("Exception occurred while fetching candidate details for cndId - " + cndId + ".");
        }
        return candidate;
    }

    public List<Candidate> getAllCandidate() {
        CompletableFuture<List<Candidate>> empListFuture = candidateDAO.getAllCandidate();
        List<Candidate> candidateList = new ArrayList<>();
        try {
            candidateList = empListFuture.get();
        } catch (Exception ex) {
            log.error("Exception occurred while fetching candidate list from future object.");
        }
        return candidateList;
    }

    public CandidateProjectDetails getAllCandidateProjectsById(Integer cndId) {
        List<CandidateProjectMapping> candidateProjectMappings = candidateDAO.getAllProjectOfEmp(cndId);
        List<CompletableFuture<Project>> projectList = new ArrayList<>();
        candidateProjectMappings.forEach(cndProjectMapping -> {
            CompletableFuture<Project> project = projectDAO.getProjectById(cndProjectMapping.getProjectId());
            projectList.add(project);
        });
        log.info("Waiting for all projects to be fetched from Database for cndId - " + cndId + ".");
        CompletableFuture.allOf(projectList.toArray(new CompletableFuture[candidateProjectMappings.size()])).join();

        Candidate candidate = getCandidateById(cndId);
        List<Project> projects = new ArrayList<>();
        for (CompletableFuture<Project> projectCompletableFuture : projectList) {
            try {
                Project project = projectCompletableFuture.get();
                if (project != null)
                    projects.add(project);
            } catch (Exception e) {
                log.error("Exception occurred while getting projectList from future object.");
            }
        }
        return mapEmpProjectDetails(candidate, projects);
    }

    public CandidateProjectDetails mapEmpProjectDetails(Candidate candidate, List<Project> projectList) {
        CandidateProjectDetails candidateProjectDetails = new CandidateProjectDetails();
        if (candidate != null) {
            candidateProjectDetails.setEmpId(candidate.getEmpId());
            candidateProjectDetails.setEmpName(candidate.getFirstName() + " " + candidate.getLastName());
            candidateProjectDetails.setPhoneNo(candidate.getPhoneNo());
            candidateProjectDetails.setEmail(candidate.getEmail());
            candidateProjectDetails.setDestination(candidate.getDestination());
            candidateProjectDetails.setLocation(candidate.getLocation());
        }
        if (projectList != null) {
            candidateProjectDetails.setProjects(projectList);
        }
        return candidateProjectDetails;
    }
}
