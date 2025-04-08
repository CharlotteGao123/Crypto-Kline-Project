package org.cryptoklineproject.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RoleAspect {
    // @Before
    @Around("@annotation(org.cryptoklineproject.config.RequiredRole) || within(@org.cryptoklineproject.config.RequiredRole *)")
    public Object checkRequireRole(ProceedingJoinPoint pjp) throws Throwable{
        String currentUserRole = getCurrentUserRole();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        RequiredRole requiredRoleAnnotation = targetMethod.getAnnotation(RequiredRole.class);
        if (requiredRoleAnnotation == null){
            //check annotation
            requiredRoleAnnotation = targetMethod.getDeclaringClass().getAnnotation(RequiredRole.class);
        }
        if(requiredRoleAnnotation != null){
            String requiredRole = requiredRoleAnnotation.value();
            log.info("[RoleAspect] Checking role -> required={}, current={}\", requiredRole, currentUserRole");

            //compare role
            if (!requiredRole.equalsIgnoreCase(currentUserRole)){
                throw new SecurityException("Access denied. Current role:" + currentUserRole + ", required:" + requiredRole);
            }
        }
        return pjp.proceed();
    }

    private String getCurrentUserRole(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String roleHeader = request.getHeader("X-Role");
            if (roleHeader != null && !roleHeader.isEmpty()) {
                return roleHeader;
            }
        }
        return"client";
    }
}
