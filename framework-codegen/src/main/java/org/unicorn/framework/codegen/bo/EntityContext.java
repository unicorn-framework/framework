package org.unicorn.framework.codegen.bo;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
/**
 * @author  xiebin
 */
public class EntityContext extends ClassBaseContext {
  private Set<String> importSet;

  
}
