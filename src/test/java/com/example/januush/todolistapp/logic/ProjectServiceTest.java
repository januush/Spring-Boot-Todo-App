package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.TaskConfigurationProperties;
import com.example.januush.todolistapp.model.ProjectRepository;
import com.example.januush.todolistapp.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {
	@Test
	@DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
	void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
		// given
		var mockGroupRepository = mock(TaskGroupRepository.class);
		when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
		// and
		TaskConfigurationProperties mockConfig = configurationReturning(false);
		// system under test
		var toTest = new ProjectService(mockGroupRepository, null, mockConfig);

		// when
		var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
		// then
		assertThat(exception)
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("one undone group");
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
	void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
		// given
		var mockProjectRepository = mock(ProjectRepository.class);
		when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
		// and
		TaskConfigurationProperties mockConfig = configurationReturning(true);
		var toTest = new ProjectService(null, mockProjectRepository, mockConfig);

		// when
		var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
		// then
		assertThat(exception)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("id not found");
	}

	private TaskConfigurationProperties configurationReturning(final boolean result) {
		var mockTemplate = mock(TaskConfigurationProperties.Template.class);
		when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
		// and
		var mockConfig = mock(TaskConfigurationProperties.class);
		when(mockConfig.getTemplate()).thenReturn(mockTemplate);
		return mockConfig;
	}
}