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

package loki.bd.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String DB_CONNECTION = "jdbc:sqlite:horarioEmpleado.db";
    private Connection conexion = null;

    public Connection abrir () {
        try {
            conexion = DriverManager.getConnection(DB_CONNECTION);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return conexion;
    }

    public void cerrar () {
        try {
            if (conexion != null) {
                conexion.close();

                //Elimino la referencia que posee.
                conexion = null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
