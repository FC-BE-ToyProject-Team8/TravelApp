package kr.co.fastcampus.travel.domain.itinerary.entity.util;

import jakarta.persistence.AttributeConverter;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;

public class TransportationConverter implements AttributeConverter<Transportation, String> {

    @Override
    public String convertToDatabaseColumn(Transportation attribute) {
        return attribute.name();
    }

    @Override
    public Transportation convertToEntityAttribute(String value) {
        return Transportation.valueOf(value);
    }
}
