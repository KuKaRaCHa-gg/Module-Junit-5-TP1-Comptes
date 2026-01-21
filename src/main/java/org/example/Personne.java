package org.example;

/**
 * Classe représentant une personne titulaire d'un compte bancaire.
 * Une personne est identifiée par son nom, son prénom et son adresse.
 */
public class Personne {
    private final String nom;
    private final String prenom;
    private final String adresse;

    /**
     * Constructeur de Personne.
     *
     * @param nom     le nom de la personne (non null)
     * @param prenom  le prénom de la personne (non null)
     * @param adresse l'adresse de la personne (non null)
     * @throws IllegalArgumentException si l'un des paramètres est null
     */
    public Personne(String nom, String prenom, String adresse) {
        if (nom == null || prenom == null || adresse == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }

    /**
     * Retourne le nom de la personne.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prénom de la personne.
     *
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Retourne l'adresse de la personne.
     *
     * @return l'adresse
     */
    public String getAdresse() {
        return adresse;
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + adresse + ")";
    }
}

