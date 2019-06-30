package com.msg.msg.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.msg.msg.entities.Token;
import com.msg.msg.repositories.TokenRepository;
import com.msg.msg.validation.Validations;

@Aspect
@Configuration
public class TokenValidationAspect {

	@Autowired
	public TokenRepository tokenRepository;

	@Pointcut("execution(* com.msg.msg.controllers.*.*(..))")
	private void pointCutForAllControllers() {
	}
	
	@Pointcut("execution(* com.msg.msg.controllers.TrainerController.*(..))")
	private void pointCutForTrainerController() {
		
	}

	@Before("pointCutForAllControllers())")
	private void adviceForValidationOfToken(JoinPoint theJoinPoint) {
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		System.out.println("Method called " + methodSig);
		String[] parameterNames = methodSig.getParameterNames();
		Object[] arguments = theJoinPoint.getArgs();
		for (int i = 0; i < parameterNames.length; i++) {
			if (parameterNames[i].equals("alphanumeric")) {
				String alphanumeric = (String) arguments[i];
				Token token = tokenRepository.findByAlphanumeric(alphanumeric);
				Validations.validateToken(token);
			}
		}
	}
	
	@Before("pointCutForTrainerController())")
	private void adviceForValidationOfTokenForAdmin(JoinPoint theJoinPoint) {
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		System.out.println("Method called " + methodSig);
		String[] parameterNames = methodSig.getParameterNames();
		Object[] arguments = theJoinPoint.getArgs();
		for (int i = 0; i < parameterNames.length; i++) {
			if (parameterNames[i].equals("alphanumeric")) {
				String alphanumeric = (String) arguments[i];
				Token token = tokenRepository.findByAlphanumeric(alphanumeric);
				Validations.validateTokenForTrainer(token);
			}
		}
	}
}
