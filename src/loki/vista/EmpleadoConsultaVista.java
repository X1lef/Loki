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

import loki.bd.dao.EmpleadoDAO;
import loki.bd.vo.Empleado;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmpleadoConsultaVista extends JDialog {
    private JTextField jtfNombreApellido;
    private JTable jtEmpleado;
    private JPopupMenu popupMenu;
    private EventoActionListener actionListener;
    private EventoMouseListener mouseListener;
    private EventoDocumentListener documentListener;
    private List<Empleado> listEmpleado;
    private EmpleadoDAO empleadoDAO;
    private boolean realizoBusq;

    public EmpleadoConsultaVista (JFrame frame) {
        super(frame, "Consulta Empleado", true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        setSize(620, 500);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        actionListener = new EventoActionListener();
        mouseListener = new EventoMouseListener();
        documentListener = new EventoDocumentListener();
        empleadoDAO = new EmpleadoDAO();
        listEmpleado = new ArrayList<>();

        jtEmpleado = new JTable();
        jtEmpleado.addMouseListener(mouseListener);
        jtEmpleado.getTableHeader().setReorderingAllowed(false);
        jtEmpleado.setDefaultRenderer(Object.class, new CeldaRenderizado());
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

        cargarTabla(empleadoDAO.todosLosEmpleados());

        setVisible(true);
    }

    private JPanel panelBuscar () {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(null, "Ingrese nombre a buscar", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.weightx = 1.0;
        conf.anchor = GridBagConstraints.WEST;
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.insets = new Insets(20, 20, 20, 20);

        jtfNombreApellido = new JTextField(25);
        jtfNombreApellido.getDocument().addDocumentListener(documentListener);

        panel.add (jtfNombreApellido, conf);

        return panel;
    }

    private JPanel panelBotones () {
        JPanel panel = new JPanel ();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel jlInfo = new JLabel("Realice doble click sobre un registro para alterarlo.");
        jlInfo.setFont(new Font("Tahoma", Font.BOLD, 11));
        panel.add(jlInfo);
        panel.add(Box.createHorizontalGlue());

        JButton jbCancelar = new JButton("Cancelar");
        jbCancelar.setActionCommand("jbCancelar");
        jbCancelar.addActionListener(actionListener);
        panel.add(jbCancelar);

        return panel;
    }

    void cargarTabla (List<Empleado> listEmpleado) {
        this.listEmpleado = listEmpleado;

        ModeloTabla tableModel = new ModeloTabla();

        for (Empleado e : listEmpleado) tableModel.addRow(e.toArray());

        jtEmpleado.setModel(tableModel);
        jtEmpleado.updateUI();
    }

    private JPopupMenu crearMenuEmergente () {
        popupMenu = new JPopupMenu();

        JMenuItem jmiEliminar = new JMenuItem("Eliminar");
        jmiEliminar.setActionCommand("jmiEliminar");
        jmiEliminar.addActionListener(actionListener);

        popupMenu.add(jmiEliminar);

        return popupMenu;
    }

    private class EventoActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()) {
                case "jbCancelar":
                    dispose();
                    break;

                case "jmiEliminar":
                    int fila = jtEmpleado.getSelectedRow();

                    int resp = JOptionPane.showConfirmDialog(EmpleadoConsultaVista.this, "¿Esta seguro que desea eliminar el registro?");
                    if (resp == JOptionPane.OK_OPTION) {
                        Empleado emp = listEmpleado.remove(fila);
                        //Se elimina de la bd.
                        empleadoDAO.eliminarEmpleado(emp.getNumeroDeCedula());

                        ((DefaultTableModel) jtEmpleado.getModel()).removeRow(fila);
                    }
                    break;
            }
        }
    }

    private class EventoMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {

            if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {

                Empleado emp = listEmpleado.get(jtEmpleado.getSelectedRow());
                new EmpleadoVista (EmpleadoConsultaVista.this, emp);
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {

            if (event.getButton() == MouseEvent.BUTTON3) {
                Point p = event.getPoint();
                int fila = jtEmpleado.rowAtPoint(p);

                if (jtEmpleado.isRowSelected(fila)) {
                    popupMenu.show(jtEmpleado, p.x, p.y);
                }
            }
        }
    }

    private class EventoDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            buscarNombre();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (realizoBusq && jtfNombreApellido.getText().isEmpty()) {
                cargarTabla(empleadoDAO.todosLosEmpleados());
                realizoBusq = false;
            } else {
                buscarNombre();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}

        private void buscarNombre () {
            String nombre = jtfNombreApellido.getText().trim();

            if (!nombre.isEmpty()) {
                List<Empleado> listEmp = empleadoDAO.buscarEmpleado(nombre);
                realizoBusq = true;

                if (!listEmp.isEmpty()) {
                    cargarTabla(listEmp);
                    listEmpleado = listEmp;
                } else {
                    //Se retorna una lista vacía.
                    cargarTabla(Collections.emptyList());
                }
            }
        }
    }

    private class ModeloTabla extends DefaultTableModel {

        ModeloTabla () {
            super (new String[] {"C.I.", "Nombre", "Apellido", "Activo"}, 0);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private class CeldaRenderizado extends DefaultTableCellRenderer {
        protected void setValue(Object value) {
            //Valor por defecto.
            setText(null);
            setIcon(null);

            if (value instanceof Boolean) {
                if (value.equals(Boolean.TRUE)){
                    setIcon(new ImageIcon(getClass().getResource("img/presente.png")));
                    setHorizontalAlignment(SwingConstants.CENTER);
                }

            } else {
                if (value != null) {
                    setText(String.valueOf(value));
                    setHorizontalTextPosition(SwingConstants.LEFT);
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
            }
        }
    }
}
