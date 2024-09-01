package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.*;
import com.rtcc.demo.exception.EntityAlreadyExistsException;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.LikeTcc;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.model.id.LikeTccId;
import com.rtcc.demo.repository.AcademicRepository;
import com.rtcc.demo.repository.LikeTccRepository;
import com.rtcc.demo.repository.TccRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class LikeTccService {

    private final LikeTccRepository likeTccRepository;
    private final TccRepository tccRepository;
    private final AcademicRepository academicRepository;
    private final AcademicService academicService;
    Logger logger = Logger.getLogger(LikeTccService.class.getName());

    @Autowired
    public LikeTccService(LikeTccRepository likeTccRepository, TccRepository tccRepository, AcademicRepository academicRepository, AcademicService academicService) {
        this.likeTccRepository = likeTccRepository;
        this.tccRepository = tccRepository;
        this.academicRepository = academicRepository;
        this.academicService = academicService;
    }

    /**
     * Like a Tcc
     *
     * @param dto LikeTccRequestDTO
     * @return
     */
    @Transactional
    public boolean likeTcc(LikeTccRequestDTO dto) {
        if (likeTccRepository.existsById(new LikeTccId(dto.tccId(), dto.academicId()))) {
            throw new EntityAlreadyExistsException("Tcc já foi curtido por este usuário. ");
        }

        LikeTcc likeTcc = buildLikeTcc(dto);

        // Incrementa a contagem de likes
        Tcc tcc = likeTcc.getTcc();
        tcc.addLike();

        // Salva as alterações
        tccRepository.save(tcc);
        likeTccRepository.save(likeTcc);

        return true;
    }

    /**
     * Unlike a Tcc
     *
     * @param dto LikeTccRequestDTO
     */
    @Transactional
    public boolean unlikeTcc(LikeTccRequestDTO dto) {

        if (!likeTccRepository.existsById(new LikeTccId(dto.academicId(), dto.tccId()))) {
            throw new EntityNotFoundException("Like not found");
        }

        LikeTcc likeTcc = buildLikeTcc(dto);
        Tcc tcc = likeTcc.getTcc();
        tcc.unlike();
        tccRepository.save(tcc);

        likeTccRepository.delete(likeTcc);
        return true;
    }

    /**
     * Build a LikeTcc object
     *
     * @param dto
     * @return
     */
    private LikeTcc buildLikeTcc(LikeTccRequestDTO dto) {
        Academic academic = academicRepository.findById(dto.academicId())
                .orElseThrow(() -> new EntityNotFoundException("Academic not found"));
        Tcc tcc = tccRepository.findById(dto.tccId())
                .orElseThrow(() -> new EntityNotFoundException("Tcc not found"));

        return new LikeTcc(tcc, academic);
    }

    public List<Tcc> getTccLikesByAcademic(String academicId) {
        List<Tcc> tccs = new ArrayList<>();
        List<LikeTcc> likes = likeTccRepository.findLikedTccsByAcademicId(academicId);

        for (LikeTcc like : likes) {
            tccs.add(like.getTcc());
        }
        return tccs;
    }


}
