package pl.hermes.server.model.domain;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Access(AccessType.FIELD)
@MappedSuperclass
abstract class PersistentObject implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @Version
    private Integer version;
}
