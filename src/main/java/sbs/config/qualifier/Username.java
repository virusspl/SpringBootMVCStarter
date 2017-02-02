package sbs.config.qualifier;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Username.UsernameValidator.class)
@Documented
public @interface Username {
	String message() default "{javax.validation.constraints.Pattern.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class UsernameValidator implements ConstraintValidator<Username, String> {
		public void initialize(Username username) {
		}
		
		
		public boolean isValid(String name, ConstraintValidatorContext context) {
			/*
				^                # Start of the line
  				[a-z0-9_-]	     # Match characters and symbols in the list, a-z, A-Z, 0-9, underscore, hyphen
             	{3,15}  		 # Length at least 3 characters and maximum length of 15
				$                # End of the line
			 */
			
			  String name_pattern =  "^[a-zA-Z0-9_-]{2,25}$";
			  Pattern pattern = Pattern.compile(name_pattern);
			  Matcher matcher = pattern.matcher(name);
			  return matcher.matches();	
		}
	}
}