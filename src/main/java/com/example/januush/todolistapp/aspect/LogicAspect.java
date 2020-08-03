package com.example.januush.todolistapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
class LogicAspect {
    // here we define diffrent aspects of objects, methods
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        return null;
    }
}
