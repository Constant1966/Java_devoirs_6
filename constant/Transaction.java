package com.devoir_6_java.constant;

import java.time.LocalDateTime;

class Transaction {
    private double montant;
    private String typeDeTransaction;
    private LocalDateTime date;
    private int id;

    public Transaction(double montant, String typeDeTransaction, LocalDateTime date, int id) {
        this.montant = montant;
        this.typeDeTransaction = typeDeTransaction;
        this.date = date;
        this.id = id;
    }

    // Getters and setters

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public String getTypeDeTransaction() {
		return typeDeTransaction;
	}

	public void setTypeDeTransaction(String typeDeTransaction) {
		this.typeDeTransaction = typeDeTransaction;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}

