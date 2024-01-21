package kr.co.kfs.assetedu.servlet.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 체결일자 결제일자 validation
 * 체결일자는 결제일자보다 과거이거나 같은 날이어야한다.
 * 
 * @author Kim Do Young
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContSettleDateValidation.class)
public @interface ContSettleDate {
	String message() default "결제일자는 체결일자보다 같은 날이거나 미래여야합니다";
	Class<?>[] groups() default{};
	public abstract Class<? extends Payload>[] payload() default{};
}
