package com.devoir_6_java.constant;


public class Compte {
    private String numero_de_compte;
    private double solde;
    private String nom;

    public Compte(String numero_de_compte, double solde, String nom) {
        this.setNumero_de_compte(numero_de_compte);
        this.setSolde(solde);
        this.setNom(nom);
    }

    public void deposer(double montant) {
        solde += montant;
    }

    public void retirer(double montant) {
        if (solde >= montant) {
            solde -= montant;
        } else {
            System.out.println("Insufficient funds!");
        }
    }

	/**
	 * @return the numero_de_compte
	 */
	public String getNumero_de_compte() {
		return numero_de_compte;
	}

	/**
	 * @param numero_de_compte the numero_de_compte to set
	 */
	public void setNumero_de_compte(String numero_de_compte) {
		this.numero_de_compte = numero_de_compte;
	}

	/**
	 * @return the solde
	 */
	public double getSolde() {
		return solde;
	}

	/**
	 * @param solde the solde to set
	 */
	public void setSolde(double solde) {
		this.solde = solde;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getIdUtilisateur() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 @Override
	    public String toString() {
	        return 
	                "numero De Compte='" + numero_de_compte + '\'' +
	                ", solde=" + solde +
	                ", nom='" + nom;
	                
	    }
}

