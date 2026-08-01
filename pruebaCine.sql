
CREATE TABLE `SALA` (
	`id_sala` VARCHAR(10) NOT NULL,
	`tipo_sala` VARCHAR(20) NOT NULL COMMENT 'Si es VIP o normal',
	`capacidad` INTEGER NOT NULL CHECK (`capacidad` > 0),
	`SUCURSAL_id_sucursal` VARCHAR(10) NOT NULL,
	KEY(`SUCURSAL_id_sucursal`),
	PRIMARY KEY(`id_sala`)
) ENGINE=INNODB;
CREATE TABLE `PERSONA` (
	`id_persona` VARCHAR(10) NOT NULL,
	`num_identificacion` VARCHAR(20) NOT NULL UNIQUE,
	`nombre` VARCHAR(50) NOT NULL,
	`apellido` VARCHAR(50) NOT NULL,
	`fecha_nacimiento` DATE NOT NULL,
	`sexo` CHAR(1) NOT NULL CHECK (`sexo` IN ('M', 'F')),
	`telefono` VARCHAR(20) NOT NULL,
	`correo` VARCHAR(100) NOT NULL,
	`DIRECCION_id_direccion` VARCHAR(10) NOT NULL,
	KEY(`DIRECCION_id_direccion`),
	PRIMARY KEY(`id_persona`)
) ENGINE=INNODB;
CREATE TABLE `EMPLEADO` (
	`PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`PERSONA_id_persona`),
	`fecha_ingreso` DATETIME NOT NULL ,
	`SUCURSAL_id_sucursal` VARCHAR(10) NOT NULL,
	KEY(`SUCURSAL_id_sucursal`),
	`CARGO_id_cargo` VARCHAR(10) NOT NULL,
	KEY(`CARGO_id_cargo`),
	PRIMARY KEY(`PERSONA_id_persona`)
) ENGINE=INNODB;
CREATE TABLE `BOLETO` (
	`id_boleto` VARCHAR(10) NOT NULL,
	`precio` DECIMAL(10,2) NOT NULL CHECK (`precio` >= 0),
	`num_asiento` INTEGER NOT NULL,
	`FUNCION_id_funcion` VARCHAR(10) NOT NULL,
	KEY(`FUNCION_id_funcion`),
	PRIMARY KEY(`id_boleto`)
) ENGINE=INNODB;
CREATE TABLE `PELICULA` (
	`id_pelicula` VARCHAR(10) NOT NULL,
	`titulo` VARCHAR(150) NOT NULL,
	`portada` MEDIUMBLOB  NULL,
	`fecha_estreno` DATE NOT NULL,
	`duracion_pelicula` INTEGER NOT NULL CHECK (`duracion_pelicula` > 0),
	`clasificacion` VARCHAR(50) NOT NULL,
	`sinopsis` TEXT NOT NULL,
	PRIMARY KEY(`id_pelicula`)
) ENGINE=INNODB;
CREATE TABLE `GENERO` (
	`id_genero` VARCHAR(10) NOT NULL,
	`nombre` VARCHAR(50) NOT NULL UNIQUE,
	`descripcion` TEXT NOT NULL,
	PRIMARY KEY(`id_genero`)
) ENGINE=INNODB;
CREATE TABLE `VENTA` (
	`id_venta` VARCHAR(10) NOT NULL,
	`fecha_hora` DATETIME NOT NULL,
	`total` DECIMAL(10,2) NOT NULL CHECK (`total` >= 0),
	`CLIENTE_PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`CLIENTE_PERSONA_id_persona`),
	`EMPLEADO_PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`EMPLEADO_PERSONA_id_persona`),
	`SUCURSAL_id_sucursal` VARCHAR(10) NOT NULL,
	KEY(`SUCURSAL_id_sucursal`),
	PRIMARY KEY(`id_venta`)
) ENGINE=INNODB;
CREATE TABLE `FUNCION` (
	`id_funcion` VARCHAR(10) NOT NULL,
	`fecha_hora_inicio` DATETIME NOT NULL,
	`fecha_hora_fin` DATETIME NOT NULL ,
	`idioma` VARCHAR(30) NOT NULL,
	`subtitulada` ENUM('S','N') NOT NULL,
	`idioma_subtitulo` VARCHAR(50) NULL,
	`PELICULA_id_pelicula` VARCHAR(10) NOT NULL,
	KEY(`PELICULA_id_pelicula`),
	`SALA_id_sala` VARCHAR(10) NOT NULL,
	KEY(`SALA_id_sala`),
	PRIMARY KEY(`id_funcion`),
	CHECK (`fecha_hora_fin` > `fecha_hora_inicio`) 
) ENGINE=INNODB;
CREATE TABLE `CLIENTE` (
	`PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`PERSONA_id_persona`),
	`fecha_ingreso` DATETIME NOT NULL,
	`puntos_acumulados` INTEGER NOT NULL DEFAULT 0 CHECK (`puntos_acumulados` >= 0),
	PRIMARY KEY(`PERSONA_id_persona`)
) ENGINE=INNODB;
CREATE TABLE `GASTO` (
	`id_gasto` VARCHAR(10) NOT NULL,
	`descripcion` TEXT NOT NULL,
	`fecha` DATETIME NOT NULL,
	`tipo` ENUM('Mantenimiento', 'Servicios Publicos', 'Equipamiento', 'Otros') NOT NULL COMMENT 'Mantenimiento, servicios, ect',
	`monto` DECIMAL(10,2) NOT NULL CHECK (`monto` > 0),
	`SUCURSAL_id_sucursal` VARCHAR(10) NOT NULL,
	KEY(`SUCURSAL_id_sucursal`),
	PRIMARY KEY(`id_gasto`)
) ENGINE=INNODB;
CREATE TABLE `PARTICIPANTE` (
	`PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`PERSONA_id_persona`),
	`anios_experiencia` INTEGER NOT NULL CHECK (`anios_experiencia` >= 0),
	`biografia` TEXT NOT NULL,
	PRIMARY KEY(`PERSONA_id_persona`)
) ENGINE=INNODB;
CREATE TABLE `REPARTO` (
	`personaje` VARCHAR(50) NULL,
	`rol` VARCHAR(50) NOT NULL,
	`PELICULA_id_pelicula` VARCHAR(10) NOT NULL,
	KEY(`PELICULA_id_pelicula`),
	`PARTICIPANTE_PERSONA_id_persona` VARCHAR(10) NOT NULL,
	KEY(`PARTICIPANTE_PERSONA_id_persona`)
) ENGINE=INNODB;
CREATE TABLE `SUCURSAL` (
	`id_sucursal` VARCHAR(10) NOT NULL,
	`nombre` VARCHAR(50) NOT NULL,
	`telefono` VARCHAR(20) NOT NULL,
	`DIRECCION_id_direccion` VARCHAR(10) NOT NULL,
	KEY(`DIRECCION_id_direccion`),
	PRIMARY KEY(`id_sucursal`)
) ENGINE=INNODB;
CREATE TABLE `PAIS` (
	`id_pais` VARCHAR(10) NOT NULL,
	`nombre_pais` VARCHAR(100) NOT NULL UNIQUE,
	PRIMARY KEY(`id_pais`)
) ENGINE=INNODB;
CREATE TABLE `PROVINCIA` (
	`id_provincia` VARCHAR(10) NOT NULL,
	`nombre_provincia` VARCHAR(100) NOT NULL UNIQUE,
	`PAIS_id_pais` VARCHAR(10) NOT NULL ,
	KEY(`PAIS_id_pais`),
	PRIMARY KEY(`id_provincia`)
) ENGINE=INNODB;
CREATE TABLE `DETALLE_VENTA` (
	`id_detalle_venta` VARCHAR(10) NOT NULL,
	`descuento` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
	`subtotal` DECIMAL(10,2) NOT NULL CHECK (`subtotal` >= 0),
	`VENTA_id_venta` VARCHAR(10) NOT NULL,
	KEY(`VENTA_id_venta`),
	`BOLETO_id_boleto` VARCHAR(10) NOT NULL,
	KEY(`BOLETO_id_boleto`),
	PRIMARY KEY(`id_detalle_venta`)
) ENGINE=INNODB;
CREATE TABLE `PELICULA_GENERO` (
	`PELICULA_id_pelicula` VARCHAR(10) NOT NULL,
	KEY(`PELICULA_id_pelicula`),
	`GENERO_id_genero` VARCHAR(10) NOT NULL,
	KEY(`GENERO_id_genero`)
) ENGINE=INNODB;
CREATE TABLE `MUNICIPIO` (
	`id_municipio` VARCHAR(10) NOT NULL,
	`nombre_municipio` VARCHAR(100) NOT NULL,
	`PROVINCIA_id_provincia` VARCHAR(10) NOT NULL,
	KEY(`PROVINCIA_id_provincia`),
	PRIMARY KEY(`id_municipio`)
) ENGINE=INNODB;
CREATE TABLE `SECTOR` (
	`id_sector` VARCHAR(10) NOT NULL,
	`nombre_sector` VARCHAR(100) NOT NULL,
	`MUNICIPIO_id_municipio` VARCHAR(10) NOT NULL,
	KEY(`MUNICIPIO_id_municipio`),
	PRIMARY KEY(`id_sector`)
) ENGINE=INNODB;
CREATE TABLE `DIRECCION` (
	`id_direccion` VARCHAR(10) NOT NULL,
	`calle` VARCHAR(100) NOT NULL,
	`SECTOR_id_sector` VARCHAR(10) NOT NULL,
	KEY(`SECTOR_id_sector`),
	PRIMARY KEY(`id_direccion`)
) ENGINE=INNODB;
CREATE TABLE `CARGO` (
	`id_cargo` VARCHAR(10) NOT NULL,
	`nombre_cargo` VARCHAR(50) NOT NULL UNIQUE,
	`descripcion` TEXT NOT NULL,
	PRIMARY KEY(`id_cargo`)
) ENGINE=INNODB;


ALTER TABLE `SALA` ADD CONSTRAINT `sala_sucursal_sucursal_id_sucursal` FOREIGN KEY (`SUCURSAL_id_sucursal`) REFERENCES `SUCURSAL`(`id_sucursal`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `PERSONA` ADD CONSTRAINT `persona_direccion_direccion_id_direccion` FOREIGN KEY (`DIRECCION_id_direccion`) REFERENCES `DIRECCION`(`id_direccion`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `EMPLEADO` ADD CONSTRAINT `empleado_persona_persona_id_persona` FOREIGN KEY (`PERSONA_id_persona`) REFERENCES `PERSONA`(`id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `EMPLEADO` ADD CONSTRAINT `empleado_sucursal_sucursal_id_sucursal` FOREIGN KEY (`SUCURSAL_id_sucursal`) REFERENCES `SUCURSAL`(`id_sucursal`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `EMPLEADO` ADD CONSTRAINT `empleado_cargo_cargo_id_cargo` FOREIGN KEY (`CARGO_id_cargo`) REFERENCES `CARGO`(`id_cargo`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `BOLETO` ADD CONSTRAINT `boleto_funcion_funcion_id_funcion` FOREIGN KEY (`FUNCION_id_funcion`) REFERENCES `FUNCION`(`id_funcion`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `VENTA` ADD CONSTRAINT `venta_cliente_cliente_persona_id_persona` FOREIGN KEY (`CLIENTE_PERSONA_id_persona`) REFERENCES `CLIENTE`(`PERSONA_id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `VENTA` ADD CONSTRAINT `venta_empleado_empleado_persona_id_persona` FOREIGN KEY (`EMPLEADO_PERSONA_id_persona`) REFERENCES `EMPLEADO`(`PERSONA_id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `VENTA` ADD CONSTRAINT `venta_sucursal_sucursal_id_sucursal` FOREIGN KEY (`SUCURSAL_id_sucursal`) REFERENCES `SUCURSAL`(`id_sucursal`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `FUNCION` ADD CONSTRAINT `funcion_pelicula_pelicula_id_pelicula` FOREIGN KEY (`PELICULA_id_pelicula`) REFERENCES `PELICULA`(`id_pelicula`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `FUNCION` ADD CONSTRAINT `funcion_sala_sala_id_sala` FOREIGN KEY (`SALA_id_sala`) REFERENCES `SALA`(`id_sala`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `CLIENTE` ADD CONSTRAINT `cliente_persona_persona_id_persona` FOREIGN KEY (`PERSONA_id_persona`) REFERENCES `PERSONA`(`id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `GASTO` ADD CONSTRAINT `gasto_sucursal_sucursal_id_sucursal` FOREIGN KEY (`SUCURSAL_id_sucursal`) REFERENCES `SUCURSAL`(`id_sucursal`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `PARTICIPANTE` ADD CONSTRAINT `participante_persona_persona_id_persona` FOREIGN KEY (`PERSONA_id_persona`) REFERENCES `PERSONA`(`id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `REPARTO` ADD CONSTRAINT `reparto_pelicula_pelicula_id_pelicula` FOREIGN KEY (`PELICULA_id_pelicula`) REFERENCES `PELICULA`(`id_pelicula`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `REPARTO` ADD CONSTRAINT `reparto_participante_participante_persona_id_persona` FOREIGN KEY (`PARTICIPANTE_PERSONA_id_persona`) REFERENCES `PARTICIPANTE`(`PERSONA_id_persona`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `SUCURSAL` ADD CONSTRAINT `sucursal_direccion_direccion_id_direccion` FOREIGN KEY (`DIRECCION_id_direccion`) REFERENCES `DIRECCION`(`id_direccion`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `PROVINCIA` ADD CONSTRAINT `provincia_pais_pais_id_pais` FOREIGN KEY (`PAIS_id_pais`) REFERENCES `PAIS`(`id_pais`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `DETALLE_VENTA` ADD CONSTRAINT `detalle_venta_venta_venta_id_venta` FOREIGN KEY (`VENTA_id_venta`) REFERENCES `VENTA`(`id_venta`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `DETALLE_VENTA` ADD CONSTRAINT `detalle_venta_boleto_boleto_id_boleto` FOREIGN KEY (`BOLETO_id_boleto`) REFERENCES `BOLETO`(`id_boleto`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `PELICULA_GENERO` ADD CONSTRAINT `pelicula_genero_pelicula_pelicula_id_pelicula` FOREIGN KEY (`PELICULA_id_pelicula`) REFERENCES `PELICULA`(`id_pelicula`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `PELICULA_GENERO` ADD CONSTRAINT `pelicula_genero_genero_genero_id_genero` FOREIGN KEY (`GENERO_id_genero`) REFERENCES `GENERO`(`id_genero`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `MUNICIPIO` ADD CONSTRAINT `municipio_provincia_provincia_id_provincia` FOREIGN KEY (`PROVINCIA_id_provincia`) REFERENCES `PROVINCIA`(`id_provincia`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `SECTOR` ADD CONSTRAINT `sector_municipio_municipio_id_municipio` FOREIGN KEY (`MUNICIPIO_id_municipio`) REFERENCES `MUNICIPIO`(`id_municipio`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `DIRECCION` ADD CONSTRAINT `direccion_sector_sector_id_sector` FOREIGN KEY (`SECTOR_id_sector`) REFERENCES `SECTOR`(`id_sector`) ON DELETE NO ACTION ON UPDATE CASCADE;


ALTER TABLE boleto 
ADD CONSTRAINT uk_asiento_funcion UNIQUE (num_asiento, FUNCION_id_funcion);