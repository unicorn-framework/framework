package org.unicorn.framework.codegen.bo;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EntityContext {
  private String name;
  private String pkg;
  private Set<String> importSet;
  private String classImportPath;

  
}
