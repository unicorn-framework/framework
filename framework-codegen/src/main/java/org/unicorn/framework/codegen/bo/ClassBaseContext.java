package org.unicorn.framework.codegen.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
/**
 * @author  xiebin
 */
public class ClassBaseContext {
	private String name;
	private String pkg;
	private String classImportPath;
	private String beanName;

}
