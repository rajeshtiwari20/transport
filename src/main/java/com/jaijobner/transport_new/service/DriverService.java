package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.driver.DriverCompactResp;
import com.jaijobner.transport_new.dto.driver.DriverCreateReq;
import com.jaijobner.transport_new.dto.driver.DriverReq;
import com.jaijobner.transport_new.dto.driver.DriverUpdateReq;
import com.jaijobner.transport_new.entity.DriverEntity;
import com.jaijobner.transport_new.mapper.DriverMapper;
import com.jaijobner.transport_new.repository.DriverRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    public Page<DriverEntity> getAllDrivers (DriverReq req) {
        Sort sort = req.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);

        Specification<DriverEntity> spec = (root, query, cb) -> {
            if (req.getSearchTerm() == null || req.getSearchTerm().trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + req.getSearchTerm().trim().toLowerCase() + "%";

            Predicate namePredicate = cb.like(cb.lower(root.get("driverName")), pattern);
            Predicate mobilePredicate = cb.like(cb.lower(root.get("driverMobile")), pattern);

            Expression<String> addressString = root.get("driverAddress").as(String.class);

            Predicate addressPredicate = cb.like(cb.lower(addressString), pattern);


            return cb.or(namePredicate, mobilePredicate, addressPredicate);
        };

        return driverRepository.findAll(spec, pageable);
    }

    public void createDriver(DriverCreateReq req) {
        DriverEntity driverEntity = driverMapper.driverCreateReqToDriverEntity(req);
        driverRepository.save(driverEntity);
    }

    public Optional<DriverEntity> getDriverById (Long id) {
        return driverRepository.findById(id);
    }

    public void updateDriver (Long id, DriverUpdateReq req) {
        Optional<DriverEntity> optionalDriverEntity = driverRepository.findById(id);
        if (optionalDriverEntity.isPresent()) {
            DriverEntity DriverEntity = optionalDriverEntity.get();
            driverMapper.updateDriverEntityFromUpdateReq(DriverEntity, req);
            driverRepository.save(DriverEntity);
        } else {
            throw new RuntimeException("Driver not found with id: " + id);
        }
    }

    public void deleteDriver(Long id) {
        if (driverRepository.existsById(id)) {
            driverRepository.deleteById(id);
        } else {
            throw new RuntimeException("Driver not found with id: " + id);
        }
    }

    public List<DriverCompactResp> getCompactList(){
        return driverMapper.toCompactResp(driverRepository.findAllProjectedByOrderByDriverNameAsc());
    }
}
