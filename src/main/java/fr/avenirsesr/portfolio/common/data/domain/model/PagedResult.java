package fr.avenirsesr.portfolio.common.data.domain.model;

import java.util.List;

public record PagedResult<T>(List<T> content, PageInfo pageInfo) {}
