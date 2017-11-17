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

package loki.vista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

public class EmpleadoConsultaVista extends JDialog {
    private JTextField jtfNombreApellido;
    private JTable jtEmpleado;
    private JButton jbBuscar;
    private JPopupMenu popupMenu;

    public EmpleadoConsultaVista (JFrame frame) {
        super(frame, "Consulta Empleado", true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        setSize(620, 500);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        jtEmpleado = new JTable();
        jtEmpleado.getTableHeader().setReorderingAllowed(false);
        crearMenuEmergente();

        JPanel panelPrinc = new JPanel();
        panelPrinc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrinc.setLayout(new BoxLayout(panelPrinc, BoxLayout.Y_AXIS));

        panelPrinc.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrinc.add(panelBuscar());
        panelPrinc.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrinc.add(new JScrollPane(jtEmpleado));
        panelPrinc.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrinc.add(panelBotones());

        add(panelPrinc);

        setVisible(true);
    }

    private JPanel panelBuscar () {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(null, "Ingrese nombre", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.weightx = 1.0;
        conf.anchor = GridBagConstraints.WEST;
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.insets = new Insets(10, 20, 10, 10);

        jtfNombreApellido = new JTextField(25);
        panel.add (jtfNombreApellido, conf);

        //Fila 0 columna 1.
        conf.gridx = 1;
        conf.weightx = 0.0;
        conf.fill = GridBagConstraints.NONE;
        conf.insets = new Insets(10, 0, 10, 10);

        jbBuscar = new JButton("Buscar");
        jbBuscar.setActionCommand("jbBuscar");
        panel.add (jbBuscar, conf);

        return panel;
    }

    private JPanel panelBotones () {
        JPanel panel = new JPanel (new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JButton jbCancelar = new JButton("Cancelar");
        jbCancelar.setActionCommand("jbCancelar");

        panel.add(jbCancelar);

        return panel;
    }

    private boolean datosValidos () {
        //TODO: Comprobar que se ingrese solo letras.

        if (jtfNombreApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No puede estar vacío el campo de busqueda.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private JPopupMenu crearMenuEmergente () {
       popupMenu = new JPopupMenu();

        JMenuItem jmiEliminar = new JMenuItem("Eliminar");
        jmiEliminar.setActionCommand("jmiEliminar");

        popupMenu.add(jmiEliminar);

        return popupMenu;
    }
}
