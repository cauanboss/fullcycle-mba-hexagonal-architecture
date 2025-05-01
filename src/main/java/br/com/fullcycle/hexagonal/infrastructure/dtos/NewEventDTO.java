package br.com.fullcycle.hexagonal.infrastructure.dtos;

public record NewEventDTO(
                Long id,
                String name,
                String date,
                int totalSpots,
                Long partnerId) {
}
