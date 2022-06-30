package com.teamvoy.shop.controller;

import com.teamvoy.shop.dto.PhoneDTO;
import com.teamvoy.shop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("phones")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping
    public List<PhoneDTO> getAllPhones() {
        return phoneService.getAll();
    }

    @GetMapping("{id}")
    public PhoneDTO getPhone(
            @PathVariable("id") Long phoneId
    ) {
        return phoneService.getById(phoneId);
    }

    @PostMapping
    public PhoneDTO create(
            @Valid @RequestBody PhoneDTO dto
    ) {
        return phoneService.create(dto);
    }

    @PutMapping
    public PhoneDTO update(
            @Valid @RequestBody PhoneDTO dto
    ) {
        return phoneService.update(dto);
    }

    @DeleteMapping("{id}")
    public boolean delete(
            @PathVariable("id") Long phoneId
    ) {
        return phoneService.delete(phoneId);
    }


    @GetMapping("count")
    public Map<String, Long> getPhonesCount() {
        return Map.of("count", phoneService.getCount());
    }
}
