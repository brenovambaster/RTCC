package com.rtcc.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "name")
@Entity(name = "keywords")
@Table(name = "keywords")

public class Keywords {
    @Id
    private String name;

}
