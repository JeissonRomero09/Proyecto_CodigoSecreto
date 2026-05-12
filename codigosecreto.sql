-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-05-2026 a las 18:44:57
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `codigosecreto`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugador`
--

CREATE TABLE `jugador` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `puntaje` int(11) DEFAULT 0,
  `es_invitado` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jugador`
--

INSERT INTO `jugador` (`id`, `nombre`, `puntaje`, `es_invitado`) VALUES
(1, 'jirem', 0, 0),
(2, '5', 0, 0),
(3, 'jireem', 0, 0),
(4, 'Milo', 0, 0),
(5, 'Profe', 0, 0),
(6, 'david', 0, 0),
(7, 'Ferney', 0, 0),
(8, 'JiMi', 0, 0),
(9, 'Profe_Winson', 0, 0),
(10, '123', 0, 0),
(11, 'drom', 0, 0),
(12, 'prueba1', 0, 0),
(13, '2425', 0, 0),
(14, 'prueba3', 0, 0),
(15, 'prueba4', 0, 0),
(16, 'agragar ', 0, 0),
(17, 'agragar name ', 0, 0),
(18, 'prueba6', 0, 0),
(19, 'prueba7', 0, 0),
(20, 'JUnitPlayer', 0, 0),
(21, 'JUnitPlayer', 0, 0),
(22, 'JUnitPlayer', 0, 0),
(23, 'JUnitPlayer', 0, 0),
(24, '1', 0, 0),
(25, '213123', 0, 0),
(26, 'dsfsdf', 0, 0),
(27, 'JUnitPlayer', 0, 0),
(28, 'ws', 0, 0),
(29, 'JUnitPlayer', 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida`
--

CREATE TABLE `partida` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partida`
--

INSERT INTO `partida` (`id`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10),
(11),
(12),
(13),
(14),
(15),
(16),
(17),
(18),
(19),
(20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partida_jugador`
--

CREATE TABLE `partida_jugador` (
  `id` int(11) NOT NULL,
  `partida_id` int(11) NOT NULL,
  `jugador_id` int(11) NOT NULL,
  `resultado` enum('VICTORIA','DERROTA') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partida_jugador`
--

INSERT INTO `partida_jugador` (`id`, `partida_id`, `jugador_id`, `resultado`) VALUES
(3, 1, 1, 'VICTORIA'),
(4, 1, 1, 'VICTORIA'),
(5, 3, 1, 'DERROTA'),
(6, 5, 1, 'DERROTA'),
(7, 6, 1, 'DERROTA'),
(8, 6, 2, 'DERROTA'),
(9, 7, 1, 'DERROTA'),
(10, 7, 2, 'DERROTA'),
(12, 8, 5, 'DERROTA'),
(14, 11, 1, 'DERROTA'),
(16, 13, 28, 'DERROTA'),
(17, 13, 1, 'DERROTA'),
(18, 15, 1, 'DERROTA'),
(19, 16, 1, 'DERROTA'),
(20, 16, 2, 'DERROTA'),
(21, 17, 4, 'VICTORIA'),
(22, 17, 1, 'DERROTA'),
(24, 19, 1, 'DERROTA'),
(25, 19, 2, 'DERROTA'),
(26, 20, 5, 'VICTORIA'),
(27, 20, 1, 'DERROTA');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `jugador`
--
ALTER TABLE `jugador`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `partida`
--
ALTER TABLE `partida`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `partida_jugador`
--
ALTER TABLE `partida_jugador`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_partida` (`partida_id`),
  ADD KEY `fk_jugador` (`jugador_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `jugador`
--
ALTER TABLE `jugador`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT de la tabla `partida`
--
ALTER TABLE `partida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `partida_jugador`
--
ALTER TABLE `partida_jugador`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `partida_jugador`
--
ALTER TABLE `partida_jugador`
  ADD CONSTRAINT `fk_jugador` FOREIGN KEY (`jugador_id`) REFERENCES `jugador` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_partida` FOREIGN KEY (`partida_id`) REFERENCES `partida` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
