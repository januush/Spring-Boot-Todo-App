package com.example.januush.todolistapp.reports;

import com.example.januush.todolistapp.model.event.TaskDone;
import com.example.januush.todolistapp.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    @EventListener
    public void on(TaskDone event) {
        logger.info("Got " + event);
    }

    @EventListener
    public void on(TaskUndone event) {
        logger.info("Got " + event);
    }
}
