/**
 * Suite de tests JUnit 5 pour les classes Personne et Compte.
 *
 * Ce fichier contient tous les tests unitaires pour valider le fonctionnement
 * des classes de modélisation des comptes bancaires.
 *
 * @author TP1 - Comptes Bancaires
 * @version 1.0
 */
package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de la classe Personne.
 *
 * Valide la création de personnes, la validation des paramètres et la représentation
 * textuelle des objets Personne.
 */
@DisplayName("Tests Personne")
class PersonneTest {

    private Personne personne;

    @BeforeEach
    void setUp() {
        personne = new Personne("Dupont", "Jean", "123 rue de la Paix");
    }

    /**
     * Vérifie qu'une personne est créée correctement.
     */
    @Test
    @DisplayName("Création valide")
    void testConstructor() {
        assertEquals("Dupont", personne.getNom());
        assertEquals("Jean", personne.getPrenom());
        assertEquals("123 rue de la Paix", personne.getAdresse());
    }

    /**
     * Vérifie que le constructeur rejette les paramètres null.
     */
    @Test
    @DisplayName("Rejet paramètres null")
    void testParamNull() {
        assertThrows(IllegalArgumentException.class, () -> new Personne(null, "Jean", "rue"));
        assertThrows(IllegalArgumentException.class, () -> new Personne("Dupont", null, "rue"));
        assertThrows(IllegalArgumentException.class, () -> new Personne("Dupont", "Jean", null));
    }

    /**
     * Vérifie que toString retourne le bon format.
     */
    @Test
    @DisplayName("toString")
    void testToString() {
        assertEquals("Jean Dupont (123 rue de la Paix)", personne.toString());
    }
}

/**
 * Tests unitaires de la classe Compte.
 *
 * Organise les tests en plusieurs catégories pour valider :
 * - La création des comptes avec différentes configurations
 * - Les opérations bancaires (crédit, débit, virement)
 * - La gestion du découvert
 * - Les limites et restrictions
 * - Les scénarios complets
 */
@DisplayName("Tests Compte")
class CompteTest {

    private Personne personne1;
    private Personne personne2;
    private Compte compte1;

    /**
     * Initialisation avant chaque test.
     * Crée deux personnes et un compte associé à la première personne.
     */
    @BeforeEach
    void setUp() {
        personne1 = new Personne("Dupont", "Jean", "123 rue de la Paix");
        personne2 = new Personne("Martin", "Marie", "456 avenue des Champs");
        compte1 = new Compte(1001, personne1);
    }

    /**
     * Tests des constructeurs de la classe Compte.
     *
     * Valide les différentes façons de créer un compte et les valeurs par défaut.
     * Vérifie également la validation des paramètres d'entrée.
     */
    @Nested
    @DisplayName("Constructeurs")
    class Constructeurs {

        /**
         * Vérifie que le constructeur crée un compte avec les valeurs par défaut.
         */
        @Test
        @DisplayName("Valeurs par défaut")
        void testConstructor1() {
            assertEquals(1001, compte1.getNumeroCompte());
            assertEquals(personne1, compte1.getTitulaire());
            assertEquals(0, compte1.getSolde());
            assertEquals(800, compte1.getDecouvertMaxAutorise());
            assertEquals(1000, compte1.getDebitMaxAutorise());
        }

        /**
         * Vérifie que le constructeur crée un compte avec un dépôt initial.
         */
        @Test
        @DisplayName("Avec dépôt initial")
        void testConstructor2() {
            Compte compte = new Compte(1002, personne1, 500);
            assertEquals(500, compte.getSolde());
        }

        /**
         * Vérifie que le constructeur crée un compte avec tous les paramètres.
         */
        @Test
        @DisplayName("Configuration complète")
        void testConstructor3() {
            Compte compte = new Compte(1003, personne1, 1000, 1000, 2000);
            assertEquals(1000, compte.getSolde());
            assertEquals(1000, compte.getDecouvertMaxAutorise());
            assertEquals(2000, compte.getDebitMaxAutorise());
        }

        /**
         * Vérifie que le constructeur rejette les paramètres invalides.
         */
        @Test
        @DisplayName("Validation paramètres")
        void testValidation() {
            assertThrows(IllegalArgumentException.class, () -> new Compte(-1, personne1));
            assertThrows(IllegalArgumentException.class, () -> new Compte(1001, null));
            assertThrows(IllegalArgumentException.class, () -> new Compte(1001, personne1, -1000, 800, 1000));
        }
    }

    /**
     * Tests des opérations bancaires.
     *
     * Valide les opérations de base sur les comptes :
     * - Crédit (ajout de fonds)
     * - Débit (retrait de fonds)
     * - Virement (transfert entre comptes)
     * - Validation des montants
     */
    @Nested
    @DisplayName("Opérations")
    class Operations {

        /**
         * Vérifie que le crédit augmente le solde.
         */
        @Test
        @DisplayName("Crédit")
        void testCredit() {
            compte1.crediter(500);
            assertEquals(500, compte1.getSolde());

            compte1.crediter(300);
            assertEquals(800, compte1.getSolde());
        }

        /**
         * Vérifie que le débit diminue le solde.
         */
        @Test
        @DisplayName("Débit")
        void testDebit() {
            compte1.crediter(1000);
            compte1.debiter(300);
            assertEquals(700, compte1.getSolde());

            compte1.debiter(600);
            assertEquals(100, compte1.getSolde());
        }

        /**
         * Vérifie que le virement transfère l'argent entre deux comptes.
         */
        @Test
        @DisplayName("Virement")
        void testVirement() {
            compte1.crediter(500);
            Compte compte2 = new Compte(1002, personne2);

            compte1.virement(200, compte2);

            assertEquals(300, compte1.getSolde());
            assertEquals(200, compte2.getSolde());
        }

        /**
         * Vérifie que les opérations rejettent les montants invalides.
         */
        @Test
        @DisplayName("Validation montants")
        void testValidationMontants() {
            assertThrows(IllegalArgumentException.class, () -> compte1.crediter(-100));
            assertThrows(IllegalArgumentException.class, () -> compte1.crediter(0));
            assertThrows(IllegalArgumentException.class, () -> compte1.debiter(0));
            assertThrows(IllegalArgumentException.class, () -> compte1.debiter(-100));
        }
    }

    /**
     * Tests de la gestion du découvert.
     *
     * Valide le comportement des comptes en situation de découvert :
     * - Calcul du montant découvert
     * - Détection d'un compte à découvert
     * - Respect des limites de découvert autorisé
     * - Calcul du débit autorisé
     */
    @Nested
    @DisplayName("Découvert")
    class Decouverts {

        /**
         * Vérifie le calcul du découvert et l'état de découvert.
         */
        @Test
        @DisplayName("Gestion découvert")
        void testDecouvert() {
            // Sans découvert
            compte1.crediter(100);
            assertEquals(0, compte1.getDecouvert());
            assertFalse(compte1.estADecouvert());

            // Avec découvert
            compte1.debiter(500);
            assertEquals(400, compte1.getDecouvert());
            assertTrue(compte1.estADecouvert());
        }

        /**
         * Vérifie que le débit ne peut pas dépasser le découvert maximal.
         */
        @Test
        @DisplayName("Limite découvert")
        void testLimiteDecouvert() {
            assertThrows(IllegalArgumentException.class, () -> compte1.debiter(900));
            assertThrows(IllegalArgumentException.class, () -> compte1.debiter(801));
        }

        /**
         * Vérifie que getDebitAutorise calcule correctement.
         */
        @Test
        @DisplayName("Débit autorisé")
        void testDebitAutorise() {
            // Solde nul : 800 (découvert max)
            assertEquals(800, compte1.getDebitAutorise());

            // Solde positif : 1000 (débit max)
            compte1.crediter(500);
            assertEquals(1000, compte1.getDebitAutorise());

            // Solde négatif : -500, débit autorisé = min(1000, -500+800) = 300
            compte1.debiter(1000);
            assertEquals(300, compte1.getDebitAutorise());
        }
    }

    /**
     * Tests des limites et modifications des paramètres.
     *
     * Valide la modification des limites du compte :
     * - Modification du découvert maximal autorisé
     * - Modification du débit maximal autorisé
     * - Contraintes lors de la réduction du découvert
     */
    @Nested
    @DisplayName("Limites")
    class Limites {

        /**
         * Vérifie la modification du découvert maximal.
         */
        @Test
        @DisplayName("Modification découvert max")
        void testSetDecouvertMax() {
            compte1.setDecouvertMaxAutorise(1500);
            assertEquals(1500, compte1.getDecouvertMaxAutorise());

            assertThrows(IllegalArgumentException.class, () -> compte1.setDecouvertMaxAutorise(-100));
        }

        /**
         * Vérifie la modification du débit maximal.
         */
        @Test
        @DisplayName("Modification débit max")
        void testSetDebitMax() {
            compte1.setDebitMaxAutorise(2000);
            assertEquals(2000, compte1.getDebitMaxAutorise());

            assertThrows(IllegalArgumentException.class, () -> compte1.setDebitMaxAutorise(-100));
        }

        /**
         * Vérifie qu'on ne peut pas réduire le découvert en dessous du découvert actuel.
         */
        @Test
        @DisplayName("Découvert actuel limite réduction")
        void testDecouvertActuelLimite() {
            compte1.debiter(500);
            assertThrows(IllegalArgumentException.class, () -> compte1.setDecouvertMaxAutorise(300));

            compte1.setDecouvertMaxAutorise(500);
            assertEquals(500, compte1.getDecouvertMaxAutorise());
        }
    }

    /**
     * Tests des scénarios complets et cas d'usage réalistes.
     *
     * Valide les interactions complexes entre les différentes fonctionnalités :
     * - Séquences d'opérations multiples
     * - Gestion de plusieurs comptes pour un même titulaire
     * - Comportement global du système
     */
    @Nested
    @DisplayName("Scénarios complets")
    class Scenarios {

        /**
         * Teste un scénario complet avec plusieurs opérations.
         */
        @Test
        @DisplayName("Opérations multiples")
        void testScenarioComplet() {
            Compte compte2 = new Compte(1002, personne2, 1000);

            // Crédit
            compte1.crediter(2000);
            assertEquals(2000, compte1.getSolde());

            // Débit
            compte1.debiter(500);
            assertEquals(1500, compte1.getSolde());

            // Virement
            compte1.virement(300, compte2);
            assertEquals(1200, compte1.getSolde());
            assertEquals(1300, compte2.getSolde());

            // Modification limites
            compte1.setDecouvertMaxAutorise(1000);
            compte1.setDebitMaxAutorise(1500);

            // Débit avec nouvelle limite
            compte1.debiter(1500);
            assertEquals(-300, compte1.getSolde());
        }

        /**
         * Vérifie que plusieurs comptes peuvent avoir le même titulaire.
         */
        @Test
        @DisplayName("Plusieurs comptes même titulaire")
        void testPlusieurComptes() {
            Compte compte2 = new Compte(1002, personne1, 500);
            Compte compte3 = new Compte(1003, personne1, 1000);

            assertEquals(personne1, compte1.getTitulaire());
            assertEquals(personne1, compte2.getTitulaire());
            assertEquals(personne1, compte3.getTitulaire());
        }
    }
}

