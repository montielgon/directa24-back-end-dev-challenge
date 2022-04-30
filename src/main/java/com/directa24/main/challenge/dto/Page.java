package com.directa24.main.challenge.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Page<T> {

	@JsonProperty("page")
	private Integer page;

	@JsonProperty("per_page")
	private Integer perPage;

	@JsonProperty("total")
	private Integer total;

	@JsonProperty("total_pages")
	private Integer totalPages;

	@JsonProperty("data")
	private List<T> data;
	
	
}
