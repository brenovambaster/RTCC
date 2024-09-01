package com.rtcc.demo.model;

import com.rtcc.demo.model.id.FavoriteTccId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "academic_favorites")  // Nome da tabela definido corretamente
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteTcc implements Serializable {

    @EmbeddedId
    private FavoriteTccId id;

    @ManyToOne
    @MapsId("tccId")
    @JoinColumn(name = "tcc_id", referencedColumnName = "id")
    private Tcc tcc;

    @ManyToOne
    @MapsId("academicId")
    @JoinColumn(name = "academic_id")
    private Academic academic;

    public FavoriteTcc(Tcc tcc, Academic academic) {
        this.tcc = tcc;
        this.academic = academic;
        this.id = new FavoriteTccId(tcc.getId(), academic.getId());
    }
}
