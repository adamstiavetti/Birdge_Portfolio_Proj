package com.bridge.example.bridge_portfolio_proj.Controller;

import com.bridge.example.bridge_portfolio_proj.Entities.Project;
import com.bridge.example.bridge_portfolio_proj.Service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project(1L, "Website1", "Sample", 4.5, Instant.parse("2024-04-08T14:30:00Z"), 14);
    }

    @Test
    void canCreateProject() throws Exception {
        when(projectService.createProject(any(Project.class))).thenReturn(project);
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Website1"));
    }

    @Test
    void canGetAllProjects() throws Exception {
        when(projectService.getAllProjects()).thenReturn(List.of(project));
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void canGetProjectById() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(Optional.of(project));
        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Website1"));
    }

    @Test
    void canUpdateProject() throws Exception {
        Project updated = new Project(1L, "Updated", "Updated", 4.5, Instant.parse("2024-04-08T14:30:00Z"), 14);
        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(updated);
        mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void canDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
        verify(projectService).deleteProject(1L);
    }
}
