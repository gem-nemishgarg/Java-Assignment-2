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
public class Project {
    private Integer projectId;
    private String name;
    private String description;
    private String clientName;
    private Boolean isActive;
    private Integer lastModifiedBy;
    private Timestamp lastModifiedOn;
}
