package com.example.ParallelProgramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandidateProjectDetails {
    private Integer empId;
    private String empName;
    private String email;
    private String phoneNo;
    private String destination;
    private String location;
    List<Project> projects = new ArrayList<>();
}
