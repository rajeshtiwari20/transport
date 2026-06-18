package com.jaijobner.transport_new.service;

import com.jaijobner.transport_new.dto.party.PartyCreateReq;
import com.jaijobner.transport_new.dto.party.PartyUpdateReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.jaijobner.transport_new.dto.party.PartyReq;
import com.jaijobner.transport_new.entity.PartyEntity;
import com.jaijobner.transport_new.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class PartyService {
    @Autowired
    PartyRepository partyRepository;

    public Page<PartyEntity> getAllParties(PartyReq req) {
        Sort sort = req.getSortBy().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(req.getSortBy()).descending()
                : Sort.by(req.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(req.getPageNo()-1, req.getPageSize(), sort);
        return partyRepository.findAll(pageable);
    }

    public void createParty(PartyCreateReq req) {
        PartyEntity partyEntity = PartyEntity.builder()
                .partyName(req.getPartyName())
                .partyCode(req.getPartyCode())
                .partyType(req.getPartyType())
                .email(req.getEmail())
                .mobile(req.getMobile())
                .telephone(req.getTelephone())
                .address1(req.getAddress1())
                .address2(req.getAddress2())
                .district(req.getDistrict())
                .state(req.getState())
                .pinNo(req.getPinNo())
                .gstNo(req.getGstNo())
                .panNo(req.getPanNo())
                .accountNo(req.getAccountNo())
                .bankName(req.getBankName())
                .ifscCode(req.getIfscCode())
                .branchName(req.getBranchName())
                .build();

        partyRepository.save(partyEntity);
    }

    public Optional<PartyEntity> getPartyById(Long id) {
        return partyRepository.findById(id);
    }

    public void updateParty(Long id, PartyUpdateReq req) {
        Optional<PartyEntity> optionalParty = partyRepository.findById(id);
        if (optionalParty.isPresent()) {
            PartyEntity partyEntity = optionalParty.get();
            partyEntity.setPartyName(req.getPartyName());
            partyEntity.setPartyCode(req.getPartyCode());
            partyEntity.setPartyType(req.getPartyType());
            partyEntity.setEmail(req.getEmail());
            partyEntity.setMobile(req.getMobile());
            partyEntity.setTelephone(req.getTelephone());
            partyEntity.setAddress1(req.getAddress1());
            partyEntity.setAddress2(req.getAddress2());
            partyEntity.setDistrict(req.getDistrict());
            partyEntity.setState(req.getState());
            partyEntity.setPinNo(req.getPinNo());
            partyEntity.setGstNo(req.getGstNo());
            partyEntity.setPanNo(req.getPanNo());
            partyEntity.setAccountNo(req.getAccountNo());
            partyEntity.setBankName(req.getBankName());
            partyEntity.setIfscCode(req.getIfscCode());
            partyEntity.setBranchName(req.getBranchName());

            partyRepository.save(partyEntity);
        } else {
            throw new RuntimeException("Party not found with id: " + id);
        }
    }

    public void deleteParty(Long id) {
        if (partyRepository.existsById(id)) {
            partyRepository.deleteById(id);
        } else {
            throw new RuntimeException("Party not found with id: " + id);
        }
    }
}
