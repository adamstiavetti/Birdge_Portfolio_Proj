package com.bridge.example.bridge_portfolio_proj.Service;

import com.bridge.example.bridge_portfolio_proj.Entities.Project;
import com.bridge.example.bridge_portfolio_proj.Repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "Website1", "Sample Description", 4.5, Instant.parse("2024-04-08T14:30:00Z"), 14);
    }

    @Test
    void canCreateProject() {

        when(projectRepository.save(project)).thenReturn(project);
        Project saved = projectService.createProject(project);
        assertThat(saved.getTitle()).isEqualTo("Website1");
        verify(projectRepository).save(project);
    }

    @Test
    void canGetAllProjects() {

        when(projectRepository.findAll()).thenReturn(List.of(project));
        List<Project> result = projectService.getAllProjects();
        assertThat(result).contains(project);
        verify(projectRepository).findAll();
    }

    @Test
    void canGetProjectById() {

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Optional<Project> result = projectService.getProjectById(1L);
        assertThat(result).isPresent().contains(project);
    }

    @Test
    void canUpdateProject() {
        Project updated = new Project(1L, "Website1.1", "Update Desc", 5.0, Instant.parse("2024-04-08T14:30:00Z"), 14 );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updated);
        Project result = projectService.updateProject(1L, updated);
        assertThat(result.getTitle()).isEqualTo("Website1.1");
        assertThat(result.getDescription()).isEqualTo("Update Desc");
        assertThat(result.getRating()).isEqualTo(5.0);
        assertThat(result.getCreatedAt()).isEqualTo("2024-04-08T14:30:00Z");
        assertThat(result.getDurationDays()).isEqualTo(14);
    }

    @Test
    void canDeleteProject() {
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }
}
