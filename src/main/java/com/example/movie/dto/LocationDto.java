package com.example.movie.dto;

import com.example.movie.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
    public Long locationNo;
    public String locationName;

    public static LocationDto fromLocationEntity(Location location) {
        return new LocationDto(
                location.getLocationNo(), location.getLocationName()
        );
    }

    public static Location fromLocationDto(LocationDto locationDto){
        Location location = new Location();
        location.builder()
                .locationNo(locationDto.getLocationNo())
                .locationName(locationDto.getLocationName())
                .build();
        return location;
    }

}