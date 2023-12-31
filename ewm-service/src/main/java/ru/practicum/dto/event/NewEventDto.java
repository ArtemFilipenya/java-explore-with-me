package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.Constants.YYYY_MM_DD_HH_MM_SS;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 3, max = 120, message = "size must be between 3 and 120")
    private String title;

    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 20, max = 2000, message = "size must be between 20 and 2000")
    private String annotation;

    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 20, max = 7000, message = "size must be between 20 and 7000")
    private String description;

    @NotNull(message = "Category cannot be empty or null")
    @Positive(message = "CategoryId must be positive")
    private Long category;

    @NotNull(message = "EventDate cannot be empty or null")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;

    @NotNull(message = "Location cannot be empty or null")
    private LocationDto location;

    @NotNull(message = "Paid cannot be empty or null")
    private boolean paid = false;

    @NotNull(message = "Participant limit cannot be empty or null")
    @PositiveOrZero(message = "Participant limit must be positive or zero")
    private Integer participantLimit = 0;

    @NotNull(message = "Request moderation cannot be empty or null")
    private boolean requestModeration = true;
}
