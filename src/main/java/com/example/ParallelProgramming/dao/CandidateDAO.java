package com.example.ParallelProgramming.dao;

import com.example.ParallelProgramming.exception.CustomException;
import com.example.ParallelProgramming.model.Candidate;
import com.example.ParallelProgramming.model.CandidateProjectMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class CandidateDAO {

    private static final Logger log = LoggerFactory.getLogger(CandidateDAO.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public static final String GET_EMPLOYEE_DETAIL_BY_ID = "SELECT * FROM Employee WHERE empId=:empId";
    public static final String GET_ALL_EMPLOYEE = "SELECT * FROM Employee";
    public static final String GET_ALL_PROJECTS_BY_EMPID = "SELECT * FROM EmpProjectMapping Where empId=:empId";

    @Async
    public CompletableFuture<Candidate> getCandidateById(Integer cndId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("empId", cndId);
        Candidate candidate;
        try {
            candidate = jdbcTemplate.queryForObject(GET_EMPLOYEE_DETAIL_BY_ID,
                    parameterSource, BeanPropertyRowMapper.newInstance(Candidate.class));
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("No record exist for candidate id - " + cndId, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(candidate);
    }

    @Async
    public CompletableFuture<List<Candidate>> getAllCandidate() {
        List<Candidate> candidateList = new ArrayList<>();
        try {
            candidateList = jdbcTemplate.query(GET_ALL_EMPLOYEE,
                    new MapSqlParameterSource(), BeanPropertyRowMapper.newInstance(Candidate.class));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(candidateList);
    }

    public List<CandidateProjectMapping> getAllProjectOfEmp(Integer cndId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("empId", cndId);
        List<CandidateProjectMapping> cndProjects = new ArrayList<>();
        try {
            cndProjects = jdbcTemplate.query(GET_ALL_PROJECTS_BY_EMPID,
                    parameterSource, BeanPropertyRowMapper.newInstance(CandidateProjectMapping.class));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return cndProjects;
    }
}