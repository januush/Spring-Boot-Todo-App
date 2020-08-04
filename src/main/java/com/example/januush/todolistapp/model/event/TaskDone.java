package com.example.januush.todolistapp.model.event;

import com.example.januush.todolistapp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
