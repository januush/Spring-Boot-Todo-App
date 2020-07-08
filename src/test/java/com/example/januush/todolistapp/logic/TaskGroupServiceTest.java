package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.model.TaskGroupRepository;
import com.example.januush.todolistapp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

	@Test
    @DisplayName("should throw IllegalStateException when undone tasks exists")
	void toggleGroup_undoneTasksExists_throwsIllegalStateException() {
		// given
		var mockTaskGroupRepository = mock(TaskGroupRepository.class);
		var mockTaskRepository = mock(TaskRepository.class);
		// when
		when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(true);
		// system under test
		var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
		// when
		var exception = catchThrowable(() -> toTest.toggleGroup(1)); // that line has to go after when
		// then
		assertThat(exception)
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("Group has undone tasks");
	}

	@Test
    @DisplayName("should throw IllegalArgumentException when all the task done but no task with given id found")
    void toggleGroup_noUndoneTasksExists_And_TaskWithGivenIdNotExists_throwsIllegalArgumentException() {
        // given
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        var mockTaskRepository = mock(TaskRepository.class);
        // when
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt())).thenReturn(false);
        // and
        when(mockTaskGroupRepository.findById(1)).thenReturn(Optional.empty());
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1)); // that line has to go after when
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TaskGroup with given id not found");
    }
}