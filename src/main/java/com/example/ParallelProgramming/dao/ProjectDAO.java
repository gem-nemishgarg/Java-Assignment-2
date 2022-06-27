package com.example.ParallelProgramming.dao;

import com.example.ParallelProgramming.exception.CustomException;
import com.example.ParallelProgramming.model.Project;
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
public class ProjectDAO {

    private static final Logger log = LoggerFactory.getLogger(ProjectDAO.class);

    public static final String GET_PROJECT_BY_ID = "SELECT * FROM Project WHERE projectId=:projectId";
    public static final String GET_ALL_PROJECTS = "SELECT * FROM Project";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Async
    public CompletableFuture<List<Project>> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        try {
            projectList = jdbcTemplate.query(GET_ALL_PROJECTS,
                    new MapSqlParameterSource(), BeanPropertyRowMapper.newInstance(Project.class));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(projectList);
    }

    @Async
    public CompletableFuture<Project> getProjectById(Integer projectId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("projectId", projectId);
        Project project;
        try {
            project = jdbcTemplate.queryForObject(GET_PROJECT_BY_ID,
                    parameterSource, BeanPropertyRowMapper.newInstance(Project.class));
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("No record exist for projectId - " + projectId, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CompletableFuture.completedFuture(project);
    }
}
