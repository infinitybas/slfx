package com.infinitybas.slfx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Controller;

/**
 * Annotation to denote FXML linked controller classes. This allows us to
 * retrieve the associated FXML for a controller without having to delve into
 * loading resources from string literals, which is an inherently flaky process.
 * 
 * @author Will Faithfull
 *
 */
@Controller
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SLFXControllerFor {
	public String value() default "";
}
