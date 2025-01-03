-- MySQL Script generated by MySQL Workbench
-- Thu Jan  2 10:41:18 2025
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
-- Table `mydb`.`Cliente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Cliente` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Cliente` (
  `idCliente` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `PW` VARCHAR(45) NOT NULL,
  `Longitud` DOUBLE NULL,
  `Latitud` DOUBLE NULL,
  PRIMARY KEY (`idCliente`),
  UNIQUE INDEX `Nombre_UNIQUE` (`Nombre` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Receptor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Receptor` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Receptor` (
  `Cliente_idCliente` INT NOT NULL,
  PRIMARY KEY (`Cliente_idCliente`),
  CONSTRAINT `fk_Receptor_Cliente`
    FOREIGN KEY (`Cliente_idCliente`)
    REFERENCES `mydb`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Remitente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Remitente` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Remitente` (
  `Cliente_idCliente` INT NOT NULL,
  PRIMARY KEY (`Cliente_idCliente`),
  CONSTRAINT `fk_Remitente_Cliente1`
    FOREIGN KEY (`Cliente_idCliente`)
    REFERENCES `mydb`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Transportista`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Transportista` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Transportista` (
  `idTransportista` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idTransportista`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Paquete`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Paquete` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Paquete` (
  `idPaquete` INT NOT NULL,
  PRIMARY KEY (`idPaquete`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Envio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Envio` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Envio` (
  `idEnvio` INT NOT NULL,
  `Transportista_idTransportista` INT NOT NULL,
  `Paquete_idPaquete` INT NOT NULL,
  `Receptor_Cliente_idCliente` INT NOT NULL,
  `Remitente_Cliente_idCliente` INT NOT NULL,
  `Finalizado` TINYINT NULL,
  PRIMARY KEY (`idEnvio`),
  INDEX `fk_Envio_Transportista1_idx` (`Transportista_idTransportista` ASC) VISIBLE,
  INDEX `fk_Envio_Paquete1_idx` (`Paquete_idPaquete` ASC) VISIBLE,
  INDEX `fk_Envio_Receptor1_idx` (`Receptor_Cliente_idCliente` ASC) VISIBLE,
  INDEX `fk_Envio_Remitente1_idx` (`Remitente_Cliente_idCliente` ASC) VISIBLE,
  CONSTRAINT `fk_Envio_Transportista1`
    FOREIGN KEY (`Transportista_idTransportista`)
    REFERENCES `mydb`.`Transportista` (`idTransportista`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Envio_Paquete1`
    FOREIGN KEY (`Paquete_idPaquete`)
    REFERENCES `mydb`.`Paquete` (`idPaquete`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Envio_Receptor1`
    FOREIGN KEY (`Receptor_Cliente_idCliente`)
    REFERENCES `mydb`.`Receptor` (`Cliente_idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Envio_Remitente1`
    FOREIGN KEY (`Remitente_Cliente_idCliente`)
    REFERENCES `mydb`.`Remitente` (`Cliente_idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Dato`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Dato` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Dato` (
  `idDato` INT NOT NULL,
  `Envio_idEnvio` INT NOT NULL,
  `Fecha` DATETIME NULL,
  PRIMARY KEY (`idDato`, `Envio_idEnvio`),
  INDEX `fk_Dato_Envio1_idx` (`Envio_idEnvio` ASC) VISIBLE,
  CONSTRAINT `fk_Dato_Envio1`
    FOREIGN KEY (`Envio_idEnvio`)
    REFERENCES `mydb`.`Envio` (`idEnvio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Ubicacion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Ubicacion` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Ubicacion` (
  `Dato_idDato` INT NOT NULL,
  `Longitud` DOUBLE NOT NULL,
  `Latitud` DOUBLE NOT NULL,
  `Velocidad` DOUBLE NULL,
  `Velocidad_via` DOUBLE NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`Dato_idDato`),
  CONSTRAINT `fk_Ubicacion_Dato1`
    FOREIGN KEY (`Dato_idDato`)
    REFERENCES `mydb`.`Dato` (`idDato`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`TH`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`TH` ;

CREATE TABLE IF NOT EXISTS `mydb`.`TH` (
  `Dato_idDato` INT NOT NULL,
  `Temperatura` DOUBLE NOT NULL,
  `Humedad` DOUBLE NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`Dato_idDato`),
  CONSTRAINT `fk_table5_Dato1`
    FOREIGN KEY (`Dato_idDato`)
    REFERENCES `mydb`.`Dato` (`idDato`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Estado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Estado` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Estado` (
  `Dato_idDato` INT NOT NULL,
  `Estado` VARCHAR(7) NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`Dato_idDato`),
  CONSTRAINT `fk_table6_Dato1`
    FOREIGN KEY (`Dato_idDato`)
    REFERENCES `mydb`.`Dato` (`idDato`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Ventilador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Ventilador` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Ventilador` (
  `Dato_idDato` INT NOT NULL,
  `Activo` TINYINT NOT NULL,
  `Fecha` DATETIME NOT NULL,
  PRIMARY KEY (`Dato_idDato`),
  CONSTRAINT `fk_table3_Dato1`
    FOREIGN KEY (`Dato_idDato`)
    REFERENCES `mydb`.`Dato` (`idDato`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
