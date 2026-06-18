package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.truck.TruckCreateReq;
import com.jaijobner.transport_new.dto.truck.TruckReq;
import com.jaijobner.transport_new.dto.truck.TruckUpdateReq;
import com.jaijobner.transport_new.entity.TruckEntity;
import com.jaijobner.transport_new.mapper.TruckMapper;
import com.jaijobner.transport_new.repository.TruckRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TruckService {
    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private TruckMapper truckMapper;

    public Page<TruckEntity> getAllTrucks (TruckReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);

        Specification<TruckEntity> spec = (root, query, cb) -> {
            if (req.getSearchTerm() == null || req.getSearchTerm().trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";

            Predicate namePredicate = cb.like(cb.lower(root.get("truckNum")), pattern);
            Predicate chasisNumPredicate = cb.like(cb.lower(root.get("chasisNum")), pattern);
            Predicate engineNumPredicate = cb.like(cb.lower(root.get("engineNum")), pattern);
            Predicate ownerNamePredicate = cb.like(cb.lower(root.get("ownerName")), pattern);
            Expression<String> mobileAsString = root.get("ownerMobile").as(String.class);
            Predicate ownerMobilePredicate = cb.like(cb.lower(mobileAsString), pattern);


            return cb.or(namePredicate, chasisNumPredicate, engineNumPredicate, ownerNamePredicate, ownerMobilePredicate);
        };

        return truckRepository.findAll(spec, pageable);
    }

    public void createTruck(TruckCreateReq req) {
        TruckEntity truckEntity = truckMapper.truckCreateReqToTruckEntity(req);
        truckRepository.save(truckEntity);
    }

    public Optional<TruckEntity> getTruckById (Long id) {
        return truckRepository.findById(id);
    }

    public void updateTruck (Long id, TruckUpdateReq req) {
        Optional<TruckEntity> optionalTruckEntity = truckRepository.findById(id);
        if (optionalTruckEntity.isPresent()) {
            TruckEntity truckEntity = optionalTruckEntity.get();
            truckMapper.updateTruckEntityFromUpdateReq(truckEntity, req);
            truckRepository.save(truckEntity);
        } else {
            throw new RuntimeException("Truck not found with id: " + id);
        }
    }

    public void deleteTruck(Long id) {
        if (truckRepository.existsById(id)) {
            truckRepository.deleteById(id);
        } else {
            throw new RuntimeException("Truck not found with id: " + id);
        }
    }
}
