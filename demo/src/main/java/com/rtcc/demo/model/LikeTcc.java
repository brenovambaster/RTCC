package com.rtcc.demo.model;

import com.rtcc.demo.model.id.LikeTccId;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Entity(name = "academic_likes_tcc")
@Table(name = "academic_likes_tcc")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeTcc implements Serializable {

    @EmbeddedId
    private LikeTccId id;

    @ManyToOne
    @MapsId("tccId")
    @JoinColumn(name = "tcc_id")
    private Tcc tcc;

    @ManyToOne
    @MapsId("academicId")
    @JoinColumn(name = "academic_id")
    private Academic academic;


    public LikeTcc(Tcc tcc, Academic academic) {
        this.tcc = tcc;
        this.academic = academic;
        this.id = new LikeTccId(tcc.getId(), academic.getId());
    }
}
