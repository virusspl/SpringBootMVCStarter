package sbs.controller.prodcomp;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PendingLockAspect {

	public PendingLockAspect() {
		
	}
	
	@Before("execution(* sbs.controller.prodcomp.ProductionComponentsController.*(..))")
    public void beforeMethod(JoinPoint jp) throws Exception {
		
    }
	
	@After("execution(* sbs.controller.prodcomp.ProductionComponentsController.*(..))")
	public void afterMethod(JoinPoint jp) throws Exception {

	}
}






    
