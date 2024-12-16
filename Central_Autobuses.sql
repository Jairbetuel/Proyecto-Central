-- MySQL Script generated by MySQL Workbench
-- Sun Dec  1 20:53:13 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Autobus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`autobus` (
  `id_autobus` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(100) NOT NULL,
  `modelo` VARCHAR(100) NOT NULL,
  `placas` VARCHAR(20) NOT NULL UNIQUE,
  `capacidad` INT NOT NULL CHECK (`capacidad` > 0),
  `estado` ENUM('activo', 'inactivo', 'en_reparacion') NOT NULL DEFAULT 'activo',
  `categoria` VARCHAR(50) NULL,
  `año` YEAR NOT NULL,
  PRIMARY KEY (`id_autobus`)
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Chofer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chofer` (
  `id_chofer` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido_p` VARCHAR(100) NOT NULL,
  `apellido_m` VARCHAR(100) NULL,
  `sueldo` DECIMAL(10, 2) NOT NULL CHECK (`sueldo` > 0),
  `num_telefono` VARCHAR(15) NOT NULL,
`rfc` VARCHAR(13) NOT NULL UNIQUE,
  `permiso_conducir` VARCHAR(50) NOT NULL,
  `estado` ENUM('activo', 'inactivo', 'suspendido') NOT NULL DEFAULT 'activo',
  `domicilio` VARCHAR(255) NULL,
  `fecha_nacimiento` DATE NOT NULL,
  PRIMARY KEY (`id_chofer`)
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Registro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`registro` (
  `id_registro` INT NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(100) NOT NULL UNIQUE,
  `hash_contraseña` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_registro`)
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Login`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`login` (
  `id_login` INT NOT NULL AUTO_INCREMENT,
  `id_registro` INT NOT NULL,
  `fecha_hora` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resultado` ENUM('exitoso', 'fallido') NOT NULL,
  PRIMARY KEY (`id_login`),
  CONSTRAINT `fk_login_registro`
    FOREIGN KEY (`id_registro`)
    REFERENCES `mydb`.`registro` (`id_registro`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`administrador` (
  `id_administrador` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido_p` VARCHAR(100) NOT NULL,
  `apellido_m` VARCHAR(100) NULL,
  `id_login` INT NOT NULL UNIQUE,
  PRIMARY KEY (`id_administrador`),
  CONSTRAINT `fk_administrador_login`
    FOREIGN KEY (`id_login`)
    REFERENCES `mydb`.`login` (`id_login`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Ruta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ruta` (
  `id_ruta` INT NOT NULL AUTO_INCREMENT,
  `origen` VARCHAR(150) NOT NULL,
  `destino` VARCHAR(150) NOT NULL,
  `hora_inicio` TIME NOT NULL,
  `hora_fin` TIME NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`id_ruta`)
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Ventanilla`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ventanilla` (
  `id_ventanilla` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido_p` VARCHAR(100) NOT NULL,
  `apellido_m` VARCHAR(100) NULL,
  `num_telefono` VARCHAR(15) NOT NULL,
`rfc` VARCHAR(13) NOT NULL UNIQUE,
  `direccion` VARCHAR(255) NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `id_registro` INT NOT NULL,
  PRIMARY KEY (`id_ventanilla`),
  foreign key (`id_registro`)
  references `mydb`.`registro` (`id_registro`)
  on delete cascade
  on update no action
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Pasajero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`pasajero` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido_p` VARCHAR(100) NOT NULL,
  `apellido_m` VARCHAR(100) NULL,
  `num_telefono` VARCHAR(15) NOT NULL UNIQUE,
  `correo_electronico` VARCHAR(150) NOT NULL UNIQUE,
  PRIMARY KEY (`id_cliente`),
  CONSTRAINT `chk_correo`
    CHECK (`correo_electronico` LIKE '%_@__%.__%')
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`numero_Asiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`asiento` (
  `id_asiento` INT NOT NULL AUTO_INCREMENT,
  `numero` INT NOT NULL,
  `estado` ENUM('disponible', 'ocupado', 'reservado') NOT NULL DEFAULT 'disponible',
  `id_autobus` INT NOT NULL,
  PRIMARY KEY (`id_asiento`),
  CONSTRAINT `fk_asiento_autobus`
    FOREIGN KEY (`id_autobus`)
    REFERENCES `mydb`.`autobus` (`id_autobus`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Boleto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`boleto` (
  `id_boleto` INT NOT NULL AUTO_INCREMENT,
  `id_pasajero` INT NOT NULL,
  `id_ruta` INT NOT NULL,
  `id_asiento` INT NOT NULL,
  `costo` DECIMAL(10, 2) NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`id_boleto`),
  CONSTRAINT `fk_boleto_pasajero`
    FOREIGN KEY (`id_pasajero`)
    REFERENCES `mydb`.`pasajero` (`id_cliente`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_boleto_ruta`
    FOREIGN KEY (`id_ruta`)
    REFERENCES `mydb`.`ruta` (`id_ruta`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_boleto_asiento`
    FOREIGN KEY (`id_asiento`)
    REFERENCES `mydb`.`asiento` (`id_asiento`)
    ON DELETE CASCADE
) ENGINE=InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Conduccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`conduccion` (
  `id_chofer` INT NOT NULL,
  `id_autobus` INT NOT NULL,
  PRIMARY KEY (`id_chofer`, `id_autobus`),
  INDEX `idx_chofer` (`id_chofer`),
  INDEX `idx_autobus` (`id_autobus`),
  CONSTRAINT `fk_conduccion_chofer`
    FOREIGN KEY (`id_chofer`)
    REFERENCES `mydb`.`chofer` (`id_chofer`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_conduccion_autobus`
    FOREIGN KEY (`id_autobus`)
    REFERENCES `mydb`.`autobus` (`id_autobus`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Asignacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`asignacion` (
  `id_autobus` INT NOT NULL,
  `id_ruta` INT NOT NULL,
  PRIMARY KEY (`id_autobus`, `id_ruta`),
  INDEX `idx_autobus` (`id_autobus`),
  INDEX `idx_ruta` (`id_ruta`),
  CONSTRAINT `fk_asignacion_autobus`
    FOREIGN KEY (`id_autobus`)
    REFERENCES `mydb`.`autobus` (`id_autobus`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_asignacion_ruta`
    FOREIGN KEY (`id_ruta`)
    REFERENCES `mydb`.`ruta` (`id_ruta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `mydb`.`Ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ticket` (
  `id_ticket` INT NOT NULL AUTO_INCREMENT,
  `fecha_hora_compra` DATETIME NOT NULL,
  `id_boleto` INT NOT NULL,
  PRIMARY KEY (`id_ticket`),
  INDEX `fk_ticket_boleto_idx` (`id_boleto`),
  CONSTRAINT `fk_ticket_boleto`
    FOREIGN KEY (`id_boleto`)
    REFERENCES `mydb`.`boleto` (`id_boleto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `mydb`.`registro` (nombre_usuario, hash_contraseña)
VALUES('gerente','contragerente');
