package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.*;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.LikeTcc;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.repository.AcademicRepository;
import com.rtcc.demo.repository.LikeTccRepository;
import com.rtcc.demo.repository.TccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;
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
    public boolean likeTcc(LikeTccRequestDTO dto) {
        LikeTcc likeTcc = buildLikeTcc(dto);
        likeTccRepository.save(likeTcc);
        return true;
    }

    /**
     * Unlike a Tcc
     *
     * @param dto LikeTccRequestDTO
     */
    public boolean unlikeTcc(LikeTccRequestDTO dto) {
        LikeTcc likeTcc = buildLikeTcc(dto);
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
        Academic academic = academicRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Academic not found"));
        Tcc tcc = tccRepository.findById(dto.tccId())
                .orElseThrow(() -> new EntityNotFoundException("Tcc not found"));

        return new LikeTcc(tcc, academic);
    }


}
