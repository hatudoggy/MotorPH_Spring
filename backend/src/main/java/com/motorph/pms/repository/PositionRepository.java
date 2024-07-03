package com.motorph.pms.repository;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/repository/PositionRepository.java
import com.motorph.ems.model.Position;
import com.motorph.ems.model.User;
=======
import com.motorph.pms.model.Position;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/repository/PositionRepository.java
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findAllByDepartmentCode(String departmentCode);
}
