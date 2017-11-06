CREATE TABLE `empleado` (
	`cedula_identidad`	TEXT NOT NULL UNIQUE,
	`nombre`	TEXT NOT NULL,
	`apellido`	TEXT NOT NULL,
	`activo`	INTEGER NOT NULL,
	PRIMARY KEY(`cedula_identidad`)
);

CREATE TABLE `horario_laboral` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`id_empleado`	TEXT NOT NULL,
	`dia_laboral`	TEXT NOT NULL,
	`hora_entrada`	INTEGER NOT NULL,
	`hora_salida`	INTEGER NOT NULL,
	FOREIGN KEY(`id_empleado`) REFERENCES `empleado`(`cedula_identidad`) ON UPDATE CASCADE
);
