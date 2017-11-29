CREATE DATABASE horario_laboral

CREATE TABLE empleado(
   cedula_identidad character varying NOT NULL, 
   nombre character varying NOT NULL, 
   apellido character varying NOT NULL, 
   activo boolean NOT NULL, 
   PRIMARY KEY (cedula_identidad)
)

CREATE TABLE horario_laboral(
   id serial NOT NULL, 
   id_empleado character varying NOT NULL, 
   dia_laboral integer NOT NULL, 
   hora_entrada time without time zone NOT NULL, 
   hora_salida time without time zone NOT NULL, 
   PRIMARY KEY (id),
   FOREIGN KEY (id_empleado) REFERENCES empleado (cedula_identidad) ON UPDATE CASCADE ON DELETE RESTRICT
) 