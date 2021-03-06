package de.blanke.ba.model;

import java.awt.Color;

import jade.util.leap.Serializable;
/**
 * Diese Klasse repr�sentiert die Steine auf der Logik Ebene.
 * @author Paul Blanke
 *
 */
public class Stein implements Serializable{
// Attribute
	private static final long serialVersionUID = -4084719863898733007L;
	private Color farbe;
	private int ring;
	private int xCord;
	private int yCord;
	private boolean isTeilVonMuehle = false;
	private Spielstein spielstein;
	private int index;
// Getter und Setter
	public Color getFarbe() {
		return farbe;
	}
	public void setFarbe(Color farbe) {
		this.farbe = farbe;
	}
	public int getRing() {
		return ring;
	}
	public void setRing(int ring) {
		this.ring = ring;
	}
	public int getxCord() {
		return xCord;
	}
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	public int getyCord() {
		return yCord;
	}
	public void setyCord(int yCord) {
		this.yCord = yCord;
	}
	public Spielstein getSpielstein() {
		return spielstein;
	}
	public void setSpielstein(Spielstein spielstein) {
		this.spielstein = spielstein;
	}
	public boolean getIsTeilVonMuehle() {
		return isTeilVonMuehle;
	}
	public void setIsTeilVonMuehle(boolean isTeilVonMuehle) {
		this.isTeilVonMuehle = isTeilVonMuehle;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
// Kontruktoren	
	public Stein(int ring, int xCord, int yCord, Color farbe) {
		this.ring = ring;
		this.xCord = xCord;
		this.yCord = yCord;
		this.farbe = farbe;
	}
	public Stein(int ring, int xCord, int yCord, Color farbe, Spielstein spielstein) {
		this.ring = ring;
		this.xCord = xCord;
		this.yCord = yCord;
		this.farbe = farbe;
		this.setSpielstein(spielstein);
	}
	// Dieser Konstruktor wird f�r die Agentenoperationen verwendet.
	public Stein(int ring, int xCord, int yCord, Color farbe, Spielstein spielstein, int index) {
		this.ring = ring;
		this.xCord = xCord;
		this.yCord = yCord;
		this.farbe = farbe;
		this.setSpielstein(spielstein);
		this.setIndex(index);
		
		
	}
// Methoden	
	public Feld convertToFeld() {
		return new Feld(this.ring, this.xCord, this.yCord);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @Override diese Methode �berschreibt die Equals Methode und
	 * pr�ft anhand der Positionen ob zwei Steine gleich sind.
	 */
	@Override
	public boolean equals(Object obj) {
		Stein stein = (Stein) obj;
		 if((this.getRing() == stein.getRing()) && (this.getxCord() == stein.getxCord()) 
				 && this.getyCord() == stein.getyCord()) {
			 return true;
		 } else {
			 return false;
		 }
	}
	public boolean equalsReihe(Stein stein) {
		if((this.getRing() == stein.getRing()) && (this.getxCord() == stein.getxCord()) 
				 || this.getyCord() == stein.getyCord()) {
			 return true;
		 } else {
			 return false;
		 }
	}
	@Override
	public String toString() {
		return "Koordinaten:" + this.ring + " , " + this.xCord + " , " + this.yCord + " (Ring, X, Y)." ;
	}

}
