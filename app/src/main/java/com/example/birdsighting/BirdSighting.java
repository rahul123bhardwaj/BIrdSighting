package com.example.birdsighting;

public class BirdSighting {
	public String birdName;
	public String zipCode;
	public String reporterEmail;
	public Integer importance;

	public BirdSighting() {
	}

	public BirdSighting(String birdName, String zipCode, String reporterEmail, Integer importance) {
		this.birdName = birdName;
		this.zipCode = zipCode;
		this.reporterEmail = reporterEmail;
		this.importance = importance;
	}

}
