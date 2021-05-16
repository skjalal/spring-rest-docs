package com.example.web.mappers;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class DateMapper {

  public OffsetDateTime asOffsetDateTime(Timestamp timestamp) {
    return Optional.ofNullable(timestamp).map(this::convert).orElse(null);
  }

  public Timestamp asTimestamp(OffsetDateTime offsetDateTime) {
    return Optional.ofNullable(offsetDateTime).map(this::convert).orElse(null);
  }

  private Timestamp convert(OffsetDateTime offsetDateTime) {
    return Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
  }

  private OffsetDateTime convert(Timestamp timestamp) {
    return OffsetDateTime.of(
        timestamp.toLocalDateTime().getYear(),
        timestamp.toLocalDateTime().getMonthValue(),
        timestamp.toLocalDateTime().getDayOfMonth(),
        timestamp.toLocalDateTime().getHour(),
        timestamp.toLocalDateTime().getMinute(),
        timestamp.toLocalDateTime().getSecond(),
        timestamp.toLocalDateTime().getNano(),
        ZoneOffset.UTC);
  }
}
