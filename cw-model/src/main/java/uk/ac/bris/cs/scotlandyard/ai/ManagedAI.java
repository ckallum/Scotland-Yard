package uk.ac.bris.cs.scotlandyard.ai;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers the annotated AI class with runtime <br>
 * Annotated class must implement {@link PlayerFactory}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ManagedAI {

	enum VisualiserType {
		/**
		 * {@link Visualiser#surface()} is directly on top of the map
		 */
		MAP_OVERLAP,

		/**
		 * {@link Visualiser#surface()} will be displayed in a separate tool
		 * window
		 */
		WINDOWED,

		/**
		 * Does not render the node supplied by {@link Visualiser#surface()} at
		 * all
		 */
		NONE
	}

	/**
	 * The display name and unique identifier of the AI class
	 * 
	 * @return some unique name
	 */
	String value();

	/**
	 * The visualiser type to use for the AI
	 * 
	 * @return the visualiser type, defaults to {@link VisualiserType#NONE}
	 */
	VisualiserType visualiserType() default VisualiserType.NONE;

}
