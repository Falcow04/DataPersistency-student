-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S2: Data Manipulation Language
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
--
--
-- Opdracht: schrijf SQL-queries om onderstaande resultaten op te vragen,
-- aan te maken, verwijderen of aan te passen in de database van de
-- bedrijfscasus.
--
-- Codeer je uitwerking onder de regel 'DROP VIEW ...' (bij een SELECT)
-- of boven de regel 'ON CONFLICT DO NOTHING;' (bij een INSERT)
-- Je kunt deze eigen query selecteren en los uitvoeren, en wijzigen tot
-- je tevreden bent.
--
-- Vervolgens kun je je uitwerkingen testen door de testregels
-- (met [TEST] erachter) te activeren (haal hiervoor de commentaartekens
-- weg) en vervolgens het hele bestand uit te voeren. Hiervoor moet je de
-- testsuite in de database hebben geladen (bedrijf_postgresql_test.sql).
-- NB: niet alle opdrachten hebben testregels.
--
-- Lever je werk pas in op Canvas als alle tests slagen. Draai daarna
-- alle wijzigingen in de database terug met de queries helemaal onderaan.
-- ------------------------------------------------------------------------


-- S2.1. Vier-daagse cursussen
--
-- Geef code en omschrijving van alle cursussen die precies vier dagen duren.
-- DROP VIEW IF EXISTS s2_1; CREATE OR REPLACE VIEW s2_1 AS                                                   -- [TEST]

-- DROP VIEW IF EXISTS s2_1;
-- CREATE OR REPLACE VIEW s2_1 AS
-- SELECT code, omschrijving
-- FROM cursussen
-- WHERE lengte = 4;


-- S2.2. Medewerkersoverzicht
--
-- Geef alle informatie van alle medewerkers, gesorteerd op functie,
-- en per functie op leeftijd (van jong naar oud).
-- DROP VIEW IF EXISTS s2_2; CREATE OR REPLACE VIEW s2_2 AS                                                     -- [TEST]

-- DROP VIEW IF EXISTS s2_2;
-- CREATE OR REPLACE VIEW s2_2 AS
-- SELECT *
-- FROM medewerkers
-- ORDER BY functie, gbdatum ASC;


-- S2.3. Door het land
--
-- Welke cursussen zijn in Utrecht en/of in Maastricht uitgevoerd? Geef
-- code en begindatum.
-- DROP VIEW IF EXISTS s2_3; CREATE OR REPLACE VIEW s2_3 AS                                                     -- [TEST]

-- DROP VIEW IF EXISTS s2_3;
-- CREATE OR REPLACE VIEW s2_3 AS
-- SELECT cursus, begindatum
-- FROM uitvoeringen
-- WHERE locatie IN ('UTRECHT', 'MAASTRICHT');

-- S2.4. Namen
--
-- Geef de naam en voorletters van alle medewerkers, behalve van R. Jansen.
-- DROP VIEW IF EXISTS s2_4; CREATE OR REPLACE VIEW s2_4 AS                                                     -- [TEST]

-- DROP VIEW IF EXISTS s2_4;
-- CREATE OR REPLACE VIEW s2_4 AS
-- SELECT naam, voorl
-- FROM medewerkers
-- WHERE NOT (naam = 'JANSEN' AND voorl = 'R');

-- S2.5. Nieuwe SQL-cursus
--
-- Er wordt een nieuwe uitvoering gepland voor cursus S02, en wel op de
-- komende 2 maart. De cursus wordt gegeven in Leerdam door Nick Smit.
-- Voeg deze gegevens toe.                                                                                       -- [TEST]

-- INSERT INTO uitvoeringen (cursus, begindatum, locatie, docent)
-- VALUES ('S02', '2025-03-02', 'LEERDAM', 7369)
-- ON CONFLICT DO NOTHING;

-- S2.6. Stagiairs
--
-- Neem één van je collega-studenten aan als stagiair ('STAGIAIR') en
-- voer zijn of haar gegevens in. Kies een personeelnummer boven de 8000.
                                                                                        -- [TEST]

-- INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
-- VALUES (8001, 'BOER', 'T', 'STAGIAIR', NULL, '2002-06-15', 1200, NULL, 10)
-- ON CONFLICT DO NOTHING;

-- S2.7. Nieuwe schaal
--
-- We breiden het salarissysteem uit naar zes schalen. Voer een extra schaal in voor mensen die
-- tussen de 3001 en 4000 euro verdienen. Zij krijgen een toelage van 500 euro.
                                                                                       -- [TEST]

-- INSERT INTO schalen (snr, ondergrens, bovengrens, toelage)
-- VALUES (6, 3001, 4000, 500)
-- ON CONFLICT DO NOTHING;

-- S2.8. Nieuwe cursus
--
-- Er wordt een nieuwe 6-daagse cursus 'Data & Persistency' in het programma opgenomen.
-- Voeg deze cursus met code 'D&P' toe, maak twee uitvoeringen in Leerdam en schrijf drie
-- mensen in.                                                                                      -- [TEST]


-- S2.9. Salarisverhoging
--
-- De medewerkers van de afdeling VERKOOP krijgen een salarisverhoging
-- van 5.5%, behalve de manager van de afdeling, deze krijgt namelijk meer: 7%.
-- Voer deze verhogingen door.

-- UPDATE medewerkers
-- SET maandsal = maandsal * 1.055
-- WHERE afd = (SELECT anr FROM afdelingen WHERE naam = 'VERKOOP')
-- AND functie != 'MANAGER';

-- UPDATE medewerkers
-- SET maandsal = maandsal * 1.07
-- WHERE afd = (SELECT anr FROM afdelingen WHERE naam = 'VERKOOP')
-- AND functie = 'MANAGER';

-- S2.10. Concurrent
--
-- Martens heeft als verkoper succes en wordt door de concurrent
-- weggekocht. Verwijder zijn gegevens.

-- Zijn collega Alders heeft ook plannen om te vertrekken. Verwijder ook zijn gegevens.
-- Waarom lukt dit (niet)?

-- DELETE FROM inschrijvingen
-- WHERE cursist IN (SELECT mnr FROM medewerkers WHERE naam IN ('MARTENS', 'ALDERS'));

-- DELETE FROM uitvoeringen
-- WHERE docent IN (SELECT mnr::numeric FROM medewerkers WHERE naam IN ('MARTENS', 'ALDERS'));

-- DELETE FROM medewerkers
-- WHERE naam IN ('MARTENS', 'ALDERS');

-- S2.11. Nieuwe afdeling
--
-- Je wordt hoofd van de nieuwe afdeling 'FINANCIEN' te Leerdam,
-- onder de hoede van De Koning. Kies een personeelnummer boven de 8000.
-- Zorg voor de juiste invoer van deze gegevens.
                                                                                      -- [TEST]

-- INSERT INTO afdelingen (anr, naam, locatie, hoofd)
-- VALUES (40, 'FINANCIEN', 'LEERDAM', (SELECT mnr FROM medewerkers WHERE naam = 'DE KONING'))
-- ON CONFLICT DO NOTHING;

-- INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
-- VALUES (8002, 'JAN', 'M', 'FINANCIEN', NULL, '1985-09-10', 5000, NULL, 40)
-- ON CONFLICT DO NOTHING;



-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_select('S2.1') AS resultaat
UNION
SELECT 'S2.2 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT * FROM test_select('S2.3') AS resultaat
UNION
SELECT * FROM test_select('S2.4') AS resultaat
UNION
SELECT * FROM test_exists('S2.5', 1) AS resultaat
UNION
SELECT * FROM test_exists('S2.6', 1) AS resultaat
UNION
SELECT * FROM test_exists('S2.7', 6) AS resultaat
ORDER BY resultaat;


-- Draai alle wijzigingen terug om conflicten in komende opdrachten te voorkomen.
UPDATE medewerkers SET afd = NULL WHERE mnr < 7369 OR mnr > 7934;
UPDATE afdelingen SET hoofd = NULL WHERE anr > 40;
DELETE FROM afdelingen WHERE anr > 40;
DELETE FROM medewerkers WHERE mnr < 7369 OR mnr > 7934;
DELETE FROM inschrijvingen WHERE cursus = 'D&P';
DELETE FROM uitvoeringen WHERE cursus = 'D&P';
DELETE FROM cursussen WHERE code = 'D&P';
DELETE FROM uitvoeringen WHERE locatie = 'LEERDAM';
INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (7654, 'MARTENS', 'P', 'VERKOPER', 7698, '28-09-1976', 1250, 1400, 30) ON CONFLICT DO NOTHING;
UPDATE medewerkers SET maandsal = 1600 WHERE mnr = 7499;
UPDATE medewerkers SET maandsal = 1250 WHERE mnr = 7521;
UPDATE medewerkers SET maandsal = 2850 WHERE mnr = 7698;
UPDATE medewerkers SET maandsal = 1500 WHERE mnr = 7844;
UPDATE medewerkers SET maandsal = 800 WHERE mnr = 7900;


