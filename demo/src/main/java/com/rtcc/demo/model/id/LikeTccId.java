package com.rtcc.demo.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeTccId implements Serializable {
    @Column(name = "tcc_id")
    private String tccId;

    @Column(name = "academic_id")
    private String academicId;

}
