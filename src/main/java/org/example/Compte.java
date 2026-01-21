package org.example;

/**
 * Classe représentant un compte bancaire.
 * Un compte est identifié par un numéro unique et est associé à une personne titulaire.
 */
public class Compte {
    private final int numeroCompte;
    private final Personne titulaire;
    private double solde;
    private double decouvertMaxAutorise;
    private double debitMaxAutorise;

    /**
     * Constructeur principal de Compte.
     * Crée un compte avec un numéro et un titulaire spécifiés.
     * Par défaut, le solde est à 0, le découvert maximal autorisé est 800 € et le débit maximal autorisé est 1000 €.
     *
     * @param numeroCompte le numéro unique du compte (positif)
     * @param titulaire    la personne titulaire du compte (non null)
     * @throws IllegalArgumentException si le numéro de compte est négatif ou si le titulaire est null
     */
    public Compte(int numeroCompte, Personne titulaire) {
        this(numeroCompte, titulaire, 0, 800, 1000);
    }

    /**
     * Constructeur de Compte avec dépôt initial.
     *
     * @param numeroCompte      le numéro unique du compte (positif)
     * @param titulaire         la personne titulaire du compte (non null)
     * @param depotInitial      le montant du dépôt initial
     * @throws IllegalArgumentException si le numéro de compte est négatif, si le titulaire est null ou si le dépôt initial est négatif
     */
    public Compte(int numeroCompte, Personne titulaire, double depotInitial) {
        this(numeroCompte, titulaire, depotInitial, 800, 1000);
    }

    /**
     * Constructeur de Compte avec paramètres complètement définis.
     *
     * @param numeroCompte              le numéro unique du compte (positif)
     * @param titulaire                 la personne titulaire du compte (non null)
     * @param soldeInitial              le montant initial du solde
     * @param decouvertMaxAutorise      le découvert maximal autorisé (positif)
     * @param debitMaxAutorise          le débit maximal autorisé (positif)
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Compte(int numeroCompte, Personne titulaire, double soldeInitial,
                  double decouvertMaxAutorise, double debitMaxAutorise) {
        if (numeroCompte < 0) {
            throw new IllegalArgumentException("Le numéro de compte ne peut pas être négatif");
        }
        if (titulaire == null) {
            throw new IllegalArgumentException("Le titulaire ne peut pas être null");
        }
        if (soldeInitial < -decouvertMaxAutorise) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être inférieur au découvert maximal autorisé");
        }
        if (decouvertMaxAutorise < 0) {
            throw new IllegalArgumentException("Le découvert maximal autorisé ne peut pas être négatif");
        }
        if (debitMaxAutorise < 0) {
            throw new IllegalArgumentException("Le débit maximal autorisé ne peut pas être négatif");
        }

        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.solde = soldeInitial;
        this.decouvertMaxAutorise = decouvertMaxAutorise;
        this.debitMaxAutorise = debitMaxAutorise;
    }

    /**
     * Retourne le numéro du compte.
     *
     * @return le numéro du compte
     */
    public int getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * Retourne le titulaire du compte.
     *
     * @return la personne titulaire du compte
     */
    public Personne getTitulaire() {
        return titulaire;
    }

    /**
     * Retourne le solde actuel du compte.
     *
     * @return le solde
     */
    public double getSolde() {
        return solde;
    }

    /**
     * Retourne le découvert maximal autorisé pour ce compte.
     *
     * @return le découvert maximal autorisé
     */
    public double getDecouvertMaxAutorise() {
        return decouvertMaxAutorise;
    }

    /**
     * Retourne le débit maximal autorisé pour ce compte.
     *
     * @return le débit maximal autorisé
     */
    public double getDebitMaxAutorise() {
        return debitMaxAutorise;
    }

    /**
     * Modifie le découvert maximal autorisé pour ce compte.
     *
     * @param nouveauDecovertMax la nouvelle valeur du découvert maximal autorisé (positif)
     * @throws IllegalArgumentException si la nouvelle valeur est négative ou si elle est inférieure au découvert actuel
     */
    public void setDecouvertMaxAutorise(double nouveauDecovertMax) {
        if (nouveauDecovertMax < 0) {
            throw new IllegalArgumentException("Le découvert maximal autorisé ne peut pas être négatif");
        }
        if (nouveauDecovertMax < Math.abs(Math.min(solde, 0))) {
            throw new IllegalArgumentException("Le découvert maximal autorisé ne peut pas être inférieur au découvert actuel");
        }
        this.decouvertMaxAutorise = nouveauDecovertMax;
    }

    /**
     * Modifie le débit maximal autorisé pour ce compte.
     *
     * @param nouveauDebitMax la nouvelle valeur du débit maximal autorisé (positif)
     * @throws IllegalArgumentException si la nouvelle valeur est négative
     */
    public void setDebitMaxAutorise(double nouveauDebitMax) {
        if (nouveauDebitMax < 0) {
            throw new IllegalArgumentException("Le débit maximal autorisé ne peut pas être négatif");
        }
        this.debitMaxAutorise = nouveauDebitMax;
    }

    /**
     * Retourne le montant du découvert actuel du compte.
     * Le découvert est nul si le solde est positif ou nul, sinon il est égal à la valeur absolue du solde.
     *
     * @return le montant du découvert
     */
    public double getDecouvert() {
        return Math.max(0, -solde);
    }

    /**
     * Indique si le compte est à découvert.
     *
     * @return true si le solde est négatif, false sinon
     */
    public boolean estADecouvert() {
        return solde < 0;
    }

    /**
     * Retourne le montant débit autorisé en fonction du solde courant et du débit maximal autorisé.
     *
     * @return le montant du débit autorisé
     */
    public double getDebitAutorise() {
        return Math.min(debitMaxAutorise, solde + decouvertMaxAutorise);
    }

    /**
     * Crédite le compte avec le montant spécifié.
     * Ajoute un montant positif au solde du compte.
     *
     * @param montant le montant à créditer (doit être positif)
     * @throws IllegalArgumentException si le montant est négatif ou nul
     */
    public void crediter(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à créditer doit être positif");
        }
        solde += montant;
    }

    /**
     * Débite le compte avec le montant spécifié.
     * Retire un montant positif au solde du compte.
     * Le solde résultant ne doit pas être inférieur au découvert maximal autorisé.
     *
     * @param montant le montant à débiter (doit être positif)
     * @throws IllegalArgumentException si le montant est négatif, nul, ou si le débit demandé dépasse le débit autorisé
     */
    public void debiter(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à débiter doit être positif");
        }
        if (montant > debitMaxAutorise) {
            throw new IllegalArgumentException("Le montant dépasse le débit maximal autorisé");
        }
        if (solde - montant < -decouvertMaxAutorise) {
            throw new IllegalArgumentException("Le solde ne peut pas descendre en dessous du découvert maximal autorisé");
        }
        solde -= montant;
    }

    /**
     * Effectue un virement depuis ce compte vers un compte destinataire.
     * Débite ce compte et crédite le compte destinataire du même montant.
     *
     * @param montant            le montant du virement (doit être positif)
     * @param compteDestinataire le compte destinataire du virement (non null)
     * @throws IllegalArgumentException si le montant est invalide, le compte destinataire est null, ou le virement ne peut pas être effectué
     */
    public void virement(double montant, Compte compteDestinataire) {
        if (compteDestinataire == null) {
            throw new IllegalArgumentException("Le compte destinataire ne peut pas être null");
        }
        // Le virement débite ce compte
        debiter(montant);
        // Et crédite le compte destinataire
        compteDestinataire.crediter(montant);
    }

    @Override
    public String toString() {
        return "Compte{" +
                "numéro=" + numeroCompte +
                ", titulaire=" + titulaire +
                ", solde=" + solde +
                ", découvert=" + getDecouvert() +
                "}";
    }
}

