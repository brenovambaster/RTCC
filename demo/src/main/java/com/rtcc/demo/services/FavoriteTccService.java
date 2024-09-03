package com.rtcc.demo.services;

import com.rtcc.demo.DTOs.FavoriteTccRequestDTO;
import com.rtcc.demo.DTOs.TccResponseDTO;
import com.rtcc.demo.exception.EntityAlreadyExistsException;
import com.rtcc.demo.exception.EntityNotFoundException;
import com.rtcc.demo.model.Academic;
import com.rtcc.demo.model.FavoriteTcc;
import com.rtcc.demo.model.Tcc;
import com.rtcc.demo.model.id.FavoriteTccId;
import com.rtcc.demo.repository.AcademicRepository;
import com.rtcc.demo.repository.FavoriteTccRepository;
import com.rtcc.demo.repository.TccRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FavoriteTccService {

    private final FavoriteTccRepository favoriteTccRepository;
    private final TccRepository tccRepository;
    private final AcademicRepository academicRepository;
    Logger logger = Logger.getLogger(FavoriteTccService.class.getName());

    @Autowired
    public FavoriteTccService(FavoriteTccRepository favoriteTccRepository, TccRepository tccRepository, AcademicRepository academicRepository) {
        this.favoriteTccRepository = favoriteTccRepository;
        this.tccRepository = tccRepository;
        this.academicRepository = academicRepository;
    }

    /**
     * Favorite a Tcc
     *
     * @param dto FavoriteTccRequestDTO
     * @return boolean
     */
    @Transactional
    public boolean favoriteTcc(FavoriteTccRequestDTO dto) {
        if (favoriteTccRepository.existsById(new FavoriteTccId(dto.tccId(), dto.academicId()))) {
            throw new EntityAlreadyExistsException("Tcc já foi favoritado por este usuário.");
        }

        FavoriteTcc favoriteTcc = buildFavoriteTcc(dto);

        // Incrementa a contagem de favoritos
        Tcc tcc = favoriteTcc.getTcc();
        tcc.addFavorite();

        // Salva as alterações
        tccRepository.save(tcc);
        favoriteTccRepository.save(favoriteTcc);

        return true;
    }

    /**
     * Unfavorite a Tcc
     *
     * @param dto FavoriteTccRequestDTO
     * @return boolean
     */
    @Transactional
    public boolean unfavoriteTcc(FavoriteTccRequestDTO dto) {
        if (!favoriteTccRepository.existsById(new FavoriteTccId(dto.tccId(), dto.academicId()))) {
            throw new EntityNotFoundException("Favorite not found");
        }

        FavoriteTcc favoriteTcc = buildFavoriteTcc(dto);
        Tcc tcc = favoriteTcc.getTcc();
        tcc.removeFavorite();
        tccRepository.save(tcc);

        favoriteTccRepository.delete(favoriteTcc);
        return true;
    }

    /**
     * Build a FavoriteTcc object
     *
     * @param dto
     * @return FavoriteTcc
     */
    private FavoriteTcc buildFavoriteTcc(FavoriteTccRequestDTO dto) {
        Academic academic = academicRepository.findById(dto.academicId())
                .orElseThrow(() -> new EntityNotFoundException("Academic not found"));
        Tcc tcc = tccRepository.findById(dto.tccId())
                .orElseThrow(() -> new EntityNotFoundException("Tcc not found"));

        return new FavoriteTcc(tcc, academic);
    }

    public List<Tcc> getTccFavoritesByAcademic(String academicId) {
        List<Tcc> tccs = new ArrayList<>();
        List<FavoriteTcc> favorites = favoriteTccRepository.findFavoritesByAcademicId(academicId);

        for (FavoriteTcc favorite : favorites) {
            tccs.add(favorite.getTcc());
        }
        return tccs;
    }

    public TccResponseDTO getFavoriteTcc(FavoriteTccRequestDTO dto) {
        FavoriteTcc favoriteTcc = favoriteTccRepository.findById(new FavoriteTccId(dto.tccId(), dto.academicId()))
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found"));

        return new TccService().convertToResponseDTO(favoriteTcc.getTcc());
    }
}
