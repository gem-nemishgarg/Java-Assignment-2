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
@RequestMapping("/parallel")
public class MainController {

    @Autowired
    private MainService mainService;

    /*
    Get API to fetch all Candidates
    */
    @GetMapping("/all")
    public Response<List<Candidate>> getAllCandidates() {
        Response<List<Candidate>> response = new Response<>();
        List<Candidate> candidateList = mainService.getAllCandidate();
        if (candidateList != null && candidateList.size() > 0) {
            response.setData(candidateList);
            response.setSuccessMessage("Got all active records successfully.");
        } else {
            response.setErrorMessage("Not found any active records.");
        }
        return response;
    }

    /*
    Get API to find a particular Candidate based on ID
    */
    @GetMapping("/id")
    public Response<Candidate> getCandidateById(@PathVariable int id) {
        Response<Candidate> response = new Response<>();
        Candidate candidate = mainService.getCandidateById(id);
        if (candidate != null) {
            response.setData(candidate);
            response.setSuccessMessage("Fetched candidate successfully.");
        } else {
            response.setErrorMessage("Not found any data for given candidate Id.");
        }
        return response;
    }

    /*
    Get API to find list of Projects mapped to a Candidate
    */
    @GetMapping("/cnd/projects/cndId")
    public Response<CandidateProjectDetails> getAllProjectsOfEmp(@PathVariable int cndId) {
        Response<CandidateProjectDetails> response = new Response<>();
        CandidateProjectDetails candidateProjectDetails = mainService.getAllCandidateProjectsById(cndId);
        if (candidateProjectDetails != null) {
            response.setData(candidateProjectDetails);
            response.setSuccessMessage("Fetched all projects of " + candidateProjectDetails.getEmpName() + ".");
        } else {
            response.setErrorMessage("Not found any projects for candidate id - " + cndId + ".");
        }
        return response;
    }
}