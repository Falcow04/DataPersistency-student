-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S6: Views
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------


-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie
-- DROP VIEW IF EXISTS deelnemers;
-- CREATE OR REPLACE VIEW deelnemers AS
-- SELECT 
--   i.cursist, 
--   i.cursus, 
--   i.begindatum, 
--   u.docent, 
--   u.locatie
-- FROM inschrijvingen i
-- JOIN uitvoeringen u ON i.cursus = u.cursus AND i.begindatum = u.begindatum;

-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view (behandeld in de les):
--     CREATE OR REPLACE VIEW personeel AS
-- 	     SELECT mnr, voorl, naam as medewerker, afd, functie
--       FROM medewerkers;

-- DROP VIEW IF EXISTS personeel;
-- CREATE OR REPLACE VIEW personeel AS
-- SELECT 
--   mnr, 
--   voorl, 
--   naam AS medewerker, 
--   afd, 
--   functie
-- FROM medewerkers;

-- SELECT 
--   d.cursus, 
--   d.begindatum, 
--   p.voorl, 
--   p.medewerker AS cursist_naam
-- FROM deelnemers d
-- JOIN personeel p ON d.cursist = p.mnr;

-- 3. Is de view "deelnemers" updatable ? Waarom ?
-- Nee want het is een JOIN tussen 2 tabellen
-- Postgresql weet niet automatisch hoe een crud moet worden afgehandleld in beide tabellen

-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen: 
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt. 

-- DROP VIEW IF EXISTS dagcursussen;
-- CREATE OR REPLACE VIEW dagcursussen AS
-- SELECT code, omschrijving, type
-- FROM cursussen
-- WHERE lengte = 1;

-- 2. Maak een tweede view met de naam "daguitvoeringen". 
--    Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt

-- DROP VIEW IF EXISTS daguitvoeringen;
-- CREATE OR REPLACE VIEW daguitvoeringen AS
-- SELECT u.*
-- FROM uitvoeringen u
-- JOIN dagcursussen d ON u.cursus = d.code;

-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT
-- DROP VIEW dagcursussen CASCADE verwijdert alle views van dagcurssen maar ook daguitvoeringen
-- DROP VIEW dagcursussen RESTRICT zult werken als er geen andere views afhankelijk zijn van dagcurssen dus als daguitvoering nog bestaat zult het ook een error geven