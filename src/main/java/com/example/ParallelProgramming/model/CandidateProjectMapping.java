package com.example.ParallelProgramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandidateProjectMapping {
    private Integer id;
    private Integer empId;
    private Integer projectId;
    private Boolean isActive;
    private Integer lastModifiedBy;
    private Timestamp lastModifiedOn;
}
