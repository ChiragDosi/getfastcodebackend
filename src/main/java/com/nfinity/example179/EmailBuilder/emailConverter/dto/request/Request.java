package com.nfinity.example179.EmailBuilder.emailConverter.dto.request;

import java.util.ArrayList;
import java.util.List;

public class Request {

	private General general = new General();

	private List<Structures> structures = new ArrayList<>();

	public General getGeneral() {
		return general;
	}

	public void setGeneral(General general) {
		this.general = general;
	}

	public List<Structures> getStructures() {
		return structures;
	}

	public void setStructures(List<Structures> structures) {
		this.structures = structures;
	}

}
