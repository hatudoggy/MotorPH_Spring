package com.motorph.pms.model;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/model/Department.java
=======
import com.motorph.pms.dto.DepartmentDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/model/Department.java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "department")
public class Department {

    @Id
    private String departmentCode;
    private String departmentName;
}
