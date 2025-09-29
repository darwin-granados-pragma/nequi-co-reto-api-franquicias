package co.com.franchise.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "branch")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchEntity implements Persistable<String> {

  @Id
  @Column("id_branch")
  private String id;

  @Column("name")
  private String name;

  @Column("id_franchise")
  private String idFranchise;

  @Transient
  private boolean isNew;

  @Override
  public boolean isNew() {
    return this.isNew || this.id == null;
  }
}
