package com.example.movie.service;

import com.example.movie.dto.LocationDto;
import com.example.movie.entity.Location;
import com.example.movie.repository.LocationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    EntityManager em;
    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDto> findAllLocationDto() {
        return locationRepository.findAll().stream()
                .map(x->LocationDto.fromLocationEntity(x))
                .toList();
    }
    public Location findOneLocation(Long locationNo){
        return locationRepository.findById(locationNo).orElse(null);
    }
}
