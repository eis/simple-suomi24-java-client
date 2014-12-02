/**
 * Creation Date: 30.11.2014
 * Creation Time: 20:51
 *
 * @author eis
 */
package fi.eis.applications.chatapp.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}