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
import loki.bd.vo.HorarioLaboral;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioLaboralDAO {
    private final Conexion conexion = new Conexion();
    private PreparedStatement statement = null;

    public void insertarHorarioLaboral (HorarioLaboral horarioLaboral) {
        final String sql = "INSERT INTO horario_laboral (id_empleado, dia_laboral, hora_entrada, hora_salida) VALUES (?,?,?,?)";

        try {
            Connection dbConexion = conexion.abrir();
            statement = dbConexion.prepareStatement(sql);

            statement.setString(1, horarioLaboral.getIdEmpleado());
            statement.setInt(2, horarioLaboral.getDiaLaboral());
            //Formato de hora que necesita la clase Time hh:mm:ss.
            statement.setTime(3, Time.valueOf(horarioLaboral.getHoraEntrada() + ":00"));
            statement.setTime(4, Time.valueOf(horarioLaboral.getHoraSalida() + ":00"));

            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo ();
        }
    }

    public void eliminarHorarioLaboral (long id) {
        String sql = "DELETE FROM horario_laboral WHERE id = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }
    }

    public void eliminarHorarioLaboral (String idEmpleado) {
        String sql = "DELETE FROM horario_laboral WHERE id_empleado = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            statement.setString(1, idEmpleado);
            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }
    }

    public void actualizarHorarioLaboral (HorarioLaboral horarioLaboral) {
        String sql = "UPDATE horario_laboral SET id_empleado = ?, dia_laboral = ?, hora_entrada = ?, hora_salida = ? WHERE id = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement  = dbConnection.prepareStatement(sql);

            statement.setString(1, horarioLaboral.getIdEmpleado());
            statement.setInt(2, horarioLaboral.getDiaLaboral());
            //Formato de hora que necesita la clase Time hh:mm:ss.
            statement.setTime(3,  Time.valueOf(horarioLaboral.getHoraEntrada() + ":00"));
            statement.setTime(4,  Time.valueOf(horarioLaboral.getHoraSalida() + ":00"));
            statement.setLong(5, horarioLaboral.getIdCargaHoraria());

            statement.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo ();
        }
    }

    public List<HorarioLaboral> obtenerHorarioLaboral (String cedula_identidad) {
        String sql = "SELECT id, id_empleado, dia_laboral, to_char(hora_entrada, 'HH24:MI'), to_char(hora_salida, 'HH24:MI') " +
                "FROM horario_laboral WHERE id_empleado = ? ORDER BY dia_laboral, hora_entrada";
        List<HorarioLaboral> listHorarioLab = new ArrayList<>();

        try {
            Connection connection = conexion.abrir();
            statement = connection.prepareStatement(sql);

            statement.setString(1, cedula_identidad);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) listHorarioLab.add(convertir(rs));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return listHorarioLab;
    }

    public List<HorarioLaboral> obtenerHorarioLabDia (int indexDia) {
        String sql = "SELECT id, id_empleado, dia_laboral, to_char(hora_entrada, 'HH24:MI'),to_char(hora_salida, 'HH24:MI') " +
                "FROM horario_laboral INNER JOIN empleado ON id_empleado = cedula_identidad WHERE dia_laboral = ? and activo = true " +
                "ORDER BY hora_entrada";
        List<HorarioLaboral> listHorarioLab = new ArrayList<>();

        try {
            Connection connection = conexion.abrir();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, indexDia);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) listHorarioLab.add(convertir(rs));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            cerrarTodo();
        }

        return listHorarioLab;
    }

    private HorarioLaboral convertir (ResultSet rs) throws SQLException {
        HorarioLaboral horarioLaboral = new HorarioLaboral ();

        horarioLaboral.setIdCargaHoraria(rs.getLong(1));
        horarioLaboral.setIdEmpleado(rs.getString(2));
        horarioLaboral.setDiaLaboral(rs.getInt(3));
        horarioLaboral.setHoraEntrada(rs.getString(4));
        horarioLaboral.setHoraSalida(rs.getString(5));

        return horarioLaboral;
    }

    private void cerrarTodo () {
        try {
            conexion.cerrar();
        } finally {
            try {
                if (statement != null) {
                    statement.close();

                    //Elimino la referencia que posee.
                    statement = null;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
