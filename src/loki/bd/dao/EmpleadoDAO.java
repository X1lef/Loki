/*
 * Copyright (C) 2017 Félix Pedrozo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package loki.bd.dao;

import loki.bd.conexion.Conexion;
import loki.bd.vo.Empleado;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    private final Conexion conexion = new Conexion();
    private PreparedStatement statement = null;

    public boolean insertarEmpleado (Empleado empleado) {
        final String sql = "INSERT INTO empleado (cedula_identidad, nombre, apellido, activo) VALUES (?,?,?,?)";

        try {
            Connection dbConexion = conexion.abrir();
            statement = dbConexion.prepareStatement(sql);

            statement.setString(1, empleado.getNumeroDeCedula());
            statement.setString(2, empleado.getNombre());
            statement.setString(3, empleado.getApellido());
            statement.setBoolean(4, empleado.isActivo());

            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } finally {
            cerrarTodo ();
        }

        return true;
    }

    public void eliminarEmpleado (String numeroDeCedula) {
        String sql = "DELETE FROM empleado WHERE cedula_identidad = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            statement.setString(1, numeroDeCedula);
            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }
    }

    public boolean actualizarEmpleado (Empleado empleado) {
        String sql = "UPDATE empleado SET cedula_identidad = ?, nombre = ?, apellido = ?, activo = ? WHERE cedula_identidad = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement  = dbConnection.prepareStatement(sql);

            statement.setString(1, empleado.getNumeroDeCedulaNuevo());
            statement.setString(2, empleado.getNombre());
            statement.setString(3, empleado.getApellido());
            statement.setBoolean(4, empleado.isActivo());
            statement.setString(5, empleado.getNumeroDeCedula());

            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } finally {
            cerrarTodo ();
        }

        return true;
    }

    public List<Empleado> todosLosEmpleados () {
        final String sql = "SELECT cedula_identidad, nombre, apellido, activo FROM empleado ORDER BY nombre, apellido";

        List <Empleado> list = new ArrayList<>();

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            //Inserto los registros a la lista de Empleados.
            while (rs.next())
                list.add(convertir(rs));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return list;
    }

    public List<Empleado> todosLosEmpleadosActivos () {
        final String sql = "SELECT cedula_identidad, nombre, apellido, activo FROM empleado WHERE activo = 'true' ORDER BY nombre, apellido";

        List <Empleado> list = new ArrayList<>();

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            //Inserto los registros a la lista de Empleados.
            while (rs.next())
                list.add(convertir(rs));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return list;
    }

    public Empleado obtenerEmpleado (String cedula_identidad) {
        String sql = "SELECT * FROM empleado WHERE cedula_identidad = ?";
        Empleado Empleado = null;

        try {
            Connection connection = conexion.abrir();
            statement = connection.prepareStatement(sql);

            statement.setString(1, cedula_identidad);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) Empleado = convertir(rs);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return Empleado;
    }

    public List<Empleado> buscarEmpleado (String nombre) {
        String sql = "SELECT * FROM empleado WHERE UPPER (nombre || ' ' || apellido) LIKE UPPER(? || '%')";

        List <Empleado> list = new ArrayList<>();

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);
            statement.setString(1, nombre);

            ResultSet rs = statement.executeQuery();

            //Inserto los registros a la lista de Empleados.
            while (rs.next()) list.add(convertir(rs));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return list;
    }

    private Empleado convertir (ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado ();

        empleado.setNumeroDeCedula(rs.getString(1));
        empleado.setNombre(rs.getString(2));
        empleado.setApellido(rs.getString(3));
        empleado.setActivo(rs.getBoolean(4));

        return empleado;
    }

    private void cerrarTodo () {
        try {
            if (statement != null) {
                statement.close();

                //Elimino la referencia que posee.
                statement = null;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            conexion.cerrar();
        }
    }
}
