package com.awn.unittestscanner.dtos;

import java.util.HashMap;

public final class StaticDataProjectDTO {
    static HashMap<String, ProjectDTO> projectDTOHashMap = new HashMap<String, ProjectDTO>();

    public StaticDataProjectDTO() {
    }

    public static HashMap<String, ProjectDTO> getProjectDTOHashMap() {
        return projectDTOHashMap;
    }

    public static void setProjectDTOHashMap(HashMap<String, ProjectDTO> projectDTOHashMap) {
        StaticDataProjectDTO.projectDTOHashMap = projectDTOHashMap;
    }
}
