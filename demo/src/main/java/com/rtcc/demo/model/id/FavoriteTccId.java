package com.rtcc.demo.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavoriteTccId implements Serializable {

    @Column(name = "tcc_id")
    private String tccId;

    @Column(name = "user_id")
    private String academicId;

}
