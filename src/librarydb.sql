-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 31, 2020 alle 23:27
-- Versione del server: 10.4.11-MariaDB
-- Versione PHP: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librarydb`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `autore`
--

CREATE TABLE `autore` (
  `id` int(10) UNSIGNED NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `autore`
--

INSERT INTO `autore` (`id`, `nome`, `cognome`) VALUES
(5, 'dario', 'nitti');

-- --------------------------------------------------------

--
-- Struttura della tabella `autore_pubblicazione`
--

CREATE TABLE `autore_pubblicazione` (
  `id_autore` int(10) UNSIGNED NOT NULL,
  `isbn` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `autore_pubblicazione`
--

INSERT INTO `autore_pubblicazione` (`id_autore`, `isbn`) VALUES
(5, '14897177');

-- --------------------------------------------------------

--
-- Struttura della tabella `like`
--

CREATE TABLE `like` (
  `id_utente` int(10) UNSIGNED NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `operazione`
--

CREATE TABLE `operazione` (
  `id` int(10) UNSIGNED NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `tipo` enum('inserimento','modifica','rimozione','moderazione') DEFAULT 'inserimento',
  `descrizione` text DEFAULT NULL,
  `id_utente` int(10) UNSIGNED DEFAULT NULL,
  `isbn` varchar(13) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `pubblicazione`
--

CREATE TABLE `pubblicazione` (
  `isbn` varchar(13) NOT NULL,
  `titolo` varchar(255) DEFAULT NULL,
  `editore` varchar(255) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `lingua` varchar(255) DEFAULT NULL,
  `pagine` int(10) UNSIGNED DEFAULT NULL,
  `descrizione` text DEFAULT NULL,
  `indice` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `pubblicazione`
--

INSERT INTO `pubblicazione` (`isbn`, `titolo`, `editore`, `data`, `lingua`, `pagine`, `descrizione`, `indice`) VALUES
('14897177', 'io, robot', 'pinguino', '2010-10-10', 'ita', 93, 'des', 'no');

-- --------------------------------------------------------

--
-- Struttura della tabella `recensione`
--

CREATE TABLE `recensione` (
  `id_utente` int(10) UNSIGNED NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `testo` text DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `approvata` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `ristampa`
--

CREATE TABLE `ristampa` (
  `isbn` varchar(13) NOT NULL,
  `data` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `sorgente`
--

CREATE TABLE `sorgente` (
  `id` int(10) UNSIGNED NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `formato` varchar(255) DEFAULT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  `isbn` varchar(13) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `tag`
--

CREATE TABLE `tag` (
  `id` int(10) UNSIGNED NOT NULL,
  `nome` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `tag_pubblicazione`
--

CREATE TABLE `tag_pubblicazione` (
  `id_tag` int(10) UNSIGNED NOT NULL,
  `isbn` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--

CREATE TABLE `utente` (
  `id` int(10) UNSIGNED NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `hashPassword` char(64) DEFAULT NULL,
  `ruolo` enum('passivo','attivo') DEFAULT 'passivo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `utente`
--

INSERT INTO `utente` (`id`, `nome`, `cognome`, `email`, `hashPassword`, `ruolo`) VALUES
(3, 'passivo', 'passivo', 'passivo', '3e5679e64551480bb3b48fb1051e62e9d86f8b89810bb9a461a289944b613eae', 'passivo'),
(4, 'dario', 'nitti', 'dario.nitti@student.univaq.it', '7b18601f5caaa6dbbc7ad058ac54a25d30e7a508ce814c41f44ea5cabf9b3181', 'attivo'),
(5, 'pimiano', 'medugno', 'primiano.medugno@student.univaq.it', '7b18601f5caaa6dbbc7ad058ac54a25d30e7a508ce814c41f44ea5cabf9b3181', 'attivo'),
(6, 'alessandro', 'de amicis', 'alessandro.deamicis@student.univaq.it', '7b18601f5caaa6dbbc7ad058ac54a25d30e7a508ce814c41f44ea5cabf9b3181', 'attivo');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `autore`
--
ALTER TABLE `autore`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `autore_pubblicazione`
--
ALTER TABLE `autore_pubblicazione`
  ADD PRIMARY KEY (`id_autore`,`isbn`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `like`
--
ALTER TABLE `like`
  ADD PRIMARY KEY (`id_utente`,`isbn`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `operazione`
--
ALTER TABLE `operazione`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_utente` (`id_utente`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `pubblicazione`
--
ALTER TABLE `pubblicazione`
  ADD PRIMARY KEY (`isbn`);

--
-- Indici per le tabelle `recensione`
--
ALTER TABLE `recensione`
  ADD PRIMARY KEY (`id_utente`,`isbn`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `ristampa`
--
ALTER TABLE `ristampa`
  ADD PRIMARY KEY (`isbn`,`data`);

--
-- Indici per le tabelle `sorgente`
--
ALTER TABLE `sorgente`
  ADD PRIMARY KEY (`id`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `tag_pubblicazione`
--
ALTER TABLE `tag_pubblicazione`
  ADD PRIMARY KEY (`id_tag`,`isbn`),
  ADD KEY `isbn` (`isbn`);

--
-- Indici per le tabelle `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `autore`
--
ALTER TABLE `autore`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT per la tabella `operazione`
--
ALTER TABLE `operazione`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `sorgente`
--
ALTER TABLE `sorgente`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `tag`
--
ALTER TABLE `tag`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `utente`
--
ALTER TABLE `utente`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `autore_pubblicazione`
--
ALTER TABLE `autore_pubblicazione`
  ADD CONSTRAINT `autore_pubblicazione_ibfk_1` FOREIGN KEY (`id_autore`) REFERENCES `autore` (`id`),
  ADD CONSTRAINT `autore_pubblicazione_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `like`
--
ALTER TABLE `like`
  ADD CONSTRAINT `like_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`),
  ADD CONSTRAINT `like_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `operazione`
--
ALTER TABLE `operazione`
  ADD CONSTRAINT `operazione_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`),
  ADD CONSTRAINT `operazione_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `recensione`
--
ALTER TABLE `recensione`
  ADD CONSTRAINT `recensione_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`),
  ADD CONSTRAINT `recensione_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `ristampa`
--
ALTER TABLE `ristampa`
  ADD CONSTRAINT `ristampa_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `sorgente`
--
ALTER TABLE `sorgente`
  ADD CONSTRAINT `sorgente_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);

--
-- Limiti per la tabella `tag_pubblicazione`
--
ALTER TABLE `tag_pubblicazione`
  ADD CONSTRAINT `tag_pubblicazione_ibfk_1` FOREIGN KEY (`id_tag`) REFERENCES `tag` (`id`),
  ADD CONSTRAINT `tag_pubblicazione_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `pubblicazione` (`isbn`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
