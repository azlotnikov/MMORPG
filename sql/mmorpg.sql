-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Мар 06 2014 г., 22:44
-- Версия сервера: 5.6.14
-- Версия PHP: 5.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `mmorpg`
--

-- --------------------------------------------------------

--
-- Структура таблицы `dictionary`
--

DROP TABLE `dictionary`;

CREATE TABLE IF NOT EXISTS `dictionary` (
  `char_value` char(1) NOT NULL,
  `description` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `dictionary`
--

INSERT INTO `dictionary` (`char_value`, `description`) VALUES
  ('.', 'grass'),
  ('#', 'wall');

-- --------------------------------------------------------

--
-- Структура таблицы `game_data`
--

DROP TABLE `game_data`;

CREATE TABLE IF NOT EXISTS `game_data` (
  `map` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `game_data`
--

INSERT INTO `game_data` (`map`) VALUES
  (0xaced0005757200035b5b4398327eb72369a9ca020000787000000007757200025b43b02666b0e25d84ac0200007870000000080023002e002e002e0023002e002e00237571007e0002000000080023002e002e002e00230023002e00237571007e0002000000080023002e002e002e002e0023002300237571007e000200000008002300230023002e002e0023002300237571007e0002000000080023002e00230023002e002e002e00237571007e0002000000080023002e002e002e002e0023002e00237571007e00020000000800230023002300230023002300230023);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

DROP TABLE `users`;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `sid` varchar(100) NOT NULL,
  `game_id` bigint(20) NOT NULL,
  `pos_x` double NOT NULL,
  `pos_y` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `login`, `password`, `sid`, `game_id`, `pos_x`, `pos_y`) VALUES
  (1, 'gamer', 'e10adc3949ba59abbe56e057f20f883e', '-1', 1, 4.8469999999999995, 4.693999999999999);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
