package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.TaskConfigurationProperties;
import com.example.januush.todolistapp.model.*;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
		var toTest = new ProjectService(mockGroupRepository, null, mockConfig, null);
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
		var mockGroupRepository = groupRepositoryReturning(true);
		// and
		TaskConfigurationProperties mockConfig = configurationReturning(true);
		var toTest = new ProjectService(null, mockProjectRepository, mockConfig, null);
		// when
		var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
		// then
		assertThat(exception)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("id not found");
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for a given id")
	void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
		// given
		var mockProjectRepository = mock(ProjectRepository.class);
		when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
		// and
		var mockGroupRepository = groupRepositoryReturning(false);
		// and
		TaskConfigurationProperties mockConfig = configurationReturning(true);
		var toTest = new ProjectService(mockGroupRepository, mockProjectRepository, mockConfig, null);
		// when
		var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
		// then
		assertThat(exception)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("id not found");
	}

	@Test
	@DisplayName("should create a new group from project")
	void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
		// given
		var today = LocalDate.now().atStartOfDay();
		// and
		var project = projectWith("bar", Set.of(-1,-2));
		// and
		var mockRepository = mock(ProjectRepository.class);
		when(mockRepository.findById(anyInt())).thenReturn(Optional.of(project));
		// and
		InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
		var serviceWithInMemoryRepo = new TaskGroupService(inMemoryGroupRepo, null);
		int countBeforeCall = inMemoryGroupRepo.count();
		// and
		TaskConfigurationProperties mockConfig = configurationReturning(true);
		// system under test
		var toTest = new ProjectService(inMemoryGroupRepo, mockRepository, mockConfig, serviceWithInMemoryRepo);
		// when
		GroupReadModel result = toTest.createGroup(today, 1);
		// then
		assertThat(result.getDescription()).isEqualTo("bar");
		assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
		assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
		assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
	}

	private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
		Set<ProjectStep> steps = daysToDeadline.stream()
				.map(days -> {
					var step = mock(ProjectStep.class);
					when(step.getDescription()).thenReturn("foo");
					when(step.getDaysToDeadline()).thenReturn(days);
					return step;
				})
				.collect(Collectors.toSet());

		var result = mock(Project.class);
		when(result.getDescription()).thenReturn(projectDescription);
		when(result.getSteps()).thenReturn(
			steps
		);
		return result;
	}

	private TaskConfigurationProperties configurationReturning(final boolean result) {
		var mockTemplate = mock(TaskConfigurationProperties.Template.class);
		when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
		// and
		var mockConfig = mock(TaskConfigurationProperties.class);
		when(mockConfig.getTemplate()).thenReturn(mockTemplate);
		return mockConfig;
	}

	private TaskGroupRepository groupRepositoryReturning(final boolean result) {
		var mockGroupRepository = mock(TaskGroupRepository.class);
		when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
		return mockGroupRepository;
	}

	// Simulate TaskGroupRepository
	private InMemoryGroupRepository inMemoryGroupRepository() {
		return new InMemoryGroupRepository();
	}

	private static class InMemoryGroupRepository implements TaskGroupRepository {
		private int index = 0;
		private Map<Integer, TaskGroup> map = new HashMap<>();

		public int count() {
			return map.values().size();
		}

		@Override
		public List<TaskGroup> findAll() {
			return new ArrayList<>(map.values());
		}

		@Override
		public Optional<TaskGroup> findById(final Integer id) {
			return Optional.ofNullable(map.get(id));
		}

		@Override
		public TaskGroup save(final TaskGroup entity) {
			if (entity.getId() == 0) {
				try {
					var field =  TaskGroup.class.getDeclaredField("id");
					field.setAccessible(true);
							field.set(entity, ++index); // set id with reflection
				} catch (NoSuchFieldException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			map.put(entity.getId(), entity);
			return entity;
		}

		@Override
		public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
			return map.values().stream()
					.filter(group -> !group.isDone())
					.anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
		}

		@Override
		public boolean existsByDescription(final String description) {
			return  map.values().stream()
					.anyMatch(group -> group.getDescription().equals(description));
		}
	};
}



