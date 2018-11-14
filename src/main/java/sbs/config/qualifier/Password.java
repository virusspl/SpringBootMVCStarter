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
@Constraint(validatedBy = Password.PasswordValidator.class)
@Documented
public @interface Password {
	String message() default "{javax.validation.constraints.Pattern.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class PasswordValidator implements ConstraintValidator<Password, String> {
		public void initialize(Password password) {
		}
		
		
		public boolean isValid(String password, ConstraintValidatorContext context) {
			/*
			(				# Start of group
  			(?=.*\d)		#   must contains one digit from 0-9
  			(?=.*[a-z])		#   must contains one lowercase characters
  			(?=.*[A-Z])		#   must contains one uppercase characters
			.				#   match anything with previous condition checking
			{6,20}			#	length at least 6 characters and maximum of 20
			)				# End of group
			 */
			  String password_pattern = "("
			  		+ "(?=.*\\d)"		// 1 digit
//			  		+ "(?=.*[a-z])"		// 1 lowercase 
			  		+ "(?=.*[A-Z])"		// 1 uppercase
			  		+ ".{6,20}"			// 6-20 length
			  		+ ")";
			  Pattern pattern = Pattern.compile(password_pattern);
			  Matcher matcher = pattern.matcher(password);
			  return matcher.matches();	
		}
	}
}