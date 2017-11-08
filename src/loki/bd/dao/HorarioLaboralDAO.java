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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            statement.setString(2, horarioLaboral.getDiaLaboral());
            statement.setLong(3, horarioLaboral.getHoraEntrada());
            statement.setLong(4, horarioLaboral.getHoraSalida());

            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            cerrarTodo ();
        }
    }

    public void eliminarHorarioLaboral (String idEmpleado, String diaLaboral, long horaEntrada) {
        String sql = "DELETE FROM horario_laboral WHERE id_empleado = ? and diaLaboral = ? and hora_entrada = ?";

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            statement.setString(1, idEmpleado);
            statement.setString(2, diaLaboral);
            statement.setLong(3, horaEntrada);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

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

        } catch (SQLException e) {
            e.printStackTrace();

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
            statement.setString(2, horarioLaboral.getDiaLaboral());
            statement.setLong(3, horarioLaboral.getHoraEntrada());
            statement.setLong(4, horarioLaboral.getHoraSalida());
            statement.setInt(5, horarioLaboral.getIdCargaHoraria());

            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            cerrarTodo ();
        }
    }

    public List<HorarioLaboral> todosLosHorarioLaborales () {
        final String sql = "SELECT * FROM horario_laboral";

        List <HorarioLaboral> list = new ArrayList<>();

        try {
            Connection dbConnection = conexion.abrir();
            statement = dbConnection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            //Inserto los registros a la lista de HorarioLaborals.
            while (rs.next())
                list.add(convertir(rs));

        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            cerrarTodo();
        }

        return list;
    }

    public HorarioLaboral obtenerHorarioLaboral (String cedula_identidad) {
        String sql = "SELECT * FROM HorarioLaboral WHERE cedula_identidad = ?";
        HorarioLaboral HorarioLaboral = null;

        try {
            Connection connection = conexion.abrir();
            statement = connection.prepareStatement(sql);

            statement.setString(1, cedula_identidad);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) HorarioLaboral = convertir(rs);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cerrarTodo();
        }

        return HorarioLaboral;
    }

    private HorarioLaboral convertir (ResultSet rs) throws SQLException {
        HorarioLaboral horarioLaboral = new HorarioLaboral ();

        horarioLaboral.setIdCargaHoraria(rs.getInt(1));
        horarioLaboral.setIdEmpleado(rs.getString(2));
        horarioLaboral.setDiaLaboral(rs.getString(3));
        horarioLaboral.setHoraEntrada(rs.getLong(4));
        horarioLaboral.setHoraSalida(rs.getLong(5));

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
                ex.printStackTrace();
            }
        }
    }
}
