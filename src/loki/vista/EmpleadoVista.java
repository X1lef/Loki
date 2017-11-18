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
import loki.bd.dao.HorarioLaboralDAO;
import loki.bd.vo.Empleado;
import loki.bd.vo.HorarioLaboral;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class EmpleadoVista extends JDialog {
    private JTextField jtfId, jtfNombre, jtfApellido;
    private JCheckBox jcbActivo;
    private JTable jtCargaHoraria;
    private JButton jbGuardar, jbCancelar, jbEliminar, jbInsertarFilas, jbEliminarFilas;
    private HorarioLaboralDAO horarioLaboralDAO;
    private EmpleadoConsultaVista empleadoConsultaVista;
    private EmpleadoDAO empleadoDAO;
    private EventoActionListener actionListener;
    private String numCedulaEmplOper;

    EmpleadoVista (JFrame frame) {
        super(frame);
        init();
        jbEliminar.setVisible(false);

        setVisible(true);
    }

    EmpleadoVista (JDialog dialog, Empleado empleado) {
        super(dialog);
        init();

        empleadoConsultaVista = (EmpleadoConsultaVista) dialog;
        numCedulaEmplOper = empleado.getNumeroDeCedula();

        //Se cambia el nombre del botón guardar por actualizar.
        jbGuardar.setText("Actualizar");
        jbGuardar.setActionCommand("jbActualizar");

        setVisible(true);
    }

    private void init () {
        setTitle("Empleado");
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(620, 500);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        actionListener = new EventoActionListener();
        horarioLaboralDAO = new HorarioLaboralDAO();
        empleadoDAO = new EmpleadoDAO();

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.insets = new Insets(20, 20, 10, 10);
        conf.anchor = GridBagConstraints.EAST;

        add(new JLabel("C.I. : "), conf);

        //Fila 1 columna 0.
        conf.gridy = 1;
        conf.insets = new Insets(0, 20, 10, 10);

        add(new JLabel("Nombre : "), conf);

        //Fila 2 columna 0.
        conf.gridy = 2;

        add(new JLabel("Apellido : "), conf);

        //Fila 0 columna 1.
        conf.gridx = 1;
        conf.gridy = 0;
        conf.weightx = 1.0;
        conf.insets = new Insets(20, 0, 10, 20);
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.anchor = GridBagConstraints.WEST;

        jtfId = new JTextField(15);
        add(jtfId, conf);

        //Fila 1 columna 1.
        conf.gridy = 1;
        conf.insets = new Insets(0, 0, 10, 20);

        jtfNombre = new JTextField(25);
        add(jtfNombre, conf);

        //Fila 2 columna 1.
        conf.gridy = 2;

        jtfApellido = new JTextField(25);
        add(jtfApellido, conf);

        //Fila 3 columna 1.
        conf.gridy = 3;

        jcbActivo = new JCheckBox("Activo");
        jcbActivo.setBorder(null);
        add(jcbActivo, conf);

        //Fila 4 columna 0.
        conf.gridx = 0;
        conf.gridy = 4;
        conf.gridwidth = 2;
        conf.weighty = conf.weightx = 1.0;
        conf.insets = new Insets(10, 20, 0, 20);
        conf.fill = GridBagConstraints.BOTH;

        add(panelHorarioLaboral(), conf);

        //Fila 5 columna 0.
        conf.gridy = 5;
        conf.weighty = 0.0;
        conf.insets = new Insets(10, 20, 20, 20);
        conf.fill = GridBagConstraints.HORIZONTAL;

        add(panelBotones(), conf);
    }

    private JPanel panelHorarioLaboral () {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(null, "Horario laboral", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.weightx = 1.0;
        conf.anchor = GridBagConstraints.EAST;
        conf.insets = new Insets(10, 10, 10, 10);

        jbInsertarFilas = new JButton("+");
        jbInsertarFilas.setToolTipText("Insertar filas");
        jbInsertarFilas.setActionCommand("jbInsertarFilas");
        jbInsertarFilas.addActionListener(actionListener);

        panel.add(jbInsertarFilas, conf);

        //Fila 0 columna 1.
        conf.gridx = 1;
        conf.weightx = 0.0;
        conf.insets = new Insets(10, 0, 10, 10);

        jbEliminarFilas = new JButton("-");
        jbEliminarFilas.setToolTipText("Eliminar filas");
        jbEliminarFilas.setActionCommand("jbEliminarFilas");
        jbEliminarFilas.addActionListener(actionListener);
        panel.add(jbEliminarFilas, conf);

        //Fila 1 columna 0.
        conf.gridx = 0;
        conf.gridy = 1;
        conf.gridwidth = 2;
        conf.weightx = conf.weighty = 1.0;
        conf.fill = GridBagConstraints.BOTH;
        conf.insets = new Insets(0, 10, 10, 10);

        jtCargaHoraria = new JTable(new ModeloTabla());
        jtCargaHoraria.setRowSelectionAllowed(false);
        jtCargaHoraria.setCellSelectionEnabled(true);
        jtCargaHoraria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtCargaHoraria.getTableHeader().setReorderingAllowed(false); //No permitir mover las columnas de la tabla.

        TableColumn tc = jtCargaHoraria.getColumn("");
        tc.setResizable(false);
        tc.setPreferredWidth(20);
        tc.setMinWidth(20);
        tc.setMaxWidth(20);
        tc.setCellRenderer(jtCargaHoraria.getTableHeader().getDefaultRenderer());

        JScrollPane sp = new JScrollPane(jtCargaHoraria);
        sp.getViewport().setBackground(Color.lightGray);
        panel.add(sp, conf);

        return panel;
    }

    private JPanel panelBotones () {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(Box.createHorizontalGlue());
        jbEliminar = new JButton("Eliminar");
        jbEliminar.setActionCommand("jbEliminar");
        jbEliminar.addActionListener(actionListener);
        panel.add(jbEliminar);

        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        jbGuardar = new JButton("Guardar");
        jbGuardar.setActionCommand("jbGuardar");
        jbGuardar.addActionListener(actionListener);
        panel.add(jbGuardar);

        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        jbCancelar = new JButton("Cancelar");
        jbCancelar.setActionCommand("jbCancelar");
        jbCancelar.addActionListener(actionListener);
        panel.add(jbCancelar);

        return panel;
    }

    private String convertirDia (int index) {
        switch (index) {
            case 1: return "L";
            case 2: return "M";
            case 3: return "X";
            case 4: return "J";
            case 5: return "V";
            default: return "S";
        }
    }

    private Calendar convertirHora (String horaConvertir) {
        String[] h = horaConvertir.split(":");
        int hora = Integer.parseInt(h[0].trim());
        int minuto = Integer.parseInt(h[1].trim());

        Calendar horaConvertida = Calendar.getInstance();
        horaConvertida.set(Calendar.HOUR_OF_DAY, hora);
        horaConvertida.set(Calendar.MINUTE, minuto);

        return horaConvertida;
    }

    private void limpiarComponentes () {
        jtfId.setText(null);
        jtfNombre.setText(null);
        jtfApellido.setText(null);
        jcbActivo.setSelected(true);

        DefaultTableModel tableModel = (DefaultTableModel) jtCargaHoraria.getModel();

        //Poner solo 4 filas.
        while (tableModel.getRowCount() > 4) {
            tableModel.removeRow(tableModel.getRowCount() -1);
            tableModel.removeRow(tableModel.getRowCount() -1);
        }

        //Limpiar tabla.
        for (int f = 0; f < 4; f ++) {
            for (int c = 1; c < 7; c ++) {
                tableModel.setValueAt("", f, c);
            }
        }
    }

    private class ModeloTabla extends DefaultTableModel {

        ModeloTabla () {
            super (new String [][] {
                    {"E", "", "", "", "", "", ""}, {"S", "", "", "", "", "", ""},
                    {"E", "", "", "", "", "", ""}, {"S", "", "", "", "", "", ""}
                    },
                    new String [] {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"}
            );
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    }

    private class EventoActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "jbGuardar":
                    DefaultTableModel tableModel = (DefaultTableModel)jtCargaHoraria.getModel();

                    //Se obtiene los datos del empleado.
                    Empleado empleado = new Empleado();
                    empleado.setNumeroDeCedula(jtfId.getText().trim());
                    empleado.setNombre(jtfNombre.getText().trim());
                    empleado.setApellido(jtfApellido.getText().trim());
                    empleado.setActivo(jcbActivo.isSelected());

                    empleadoDAO.insertarEmpleado(empleado);

                    //Se obtiene los datos del horario laboral del empleado.
                    Calendar horaEntrada, horaSalida;
                    String horaEntradaConv, horaSalidaConv, dia;
                    for (int f = 0; f < tableModel.getRowCount(); f += 2) {
                        for (int c = 1; c < 7; c++) {
                            horaEntradaConv = String.valueOf(tableModel.getValueAt(f, c));

                            if (!horaEntradaConv.trim().isEmpty()) { //Si la hora de entrada esta vacia saltear celda.
                                //Se obtiene la hora de entrada y la hora de salida.
                                horaSalidaConv = String.valueOf(tableModel.getValueAt(f + 1, c));

                                dia = convertirDia(c);
                                horaEntrada = convertirHora(horaEntradaConv);
                                horaSalida = convertirHora(horaSalidaConv);

                                HorarioLaboral horarioLaboral = new HorarioLaboral();
                                horarioLaboral.setIdEmpleado(jtfId.getText());
                                horarioLaboral.setDiaLaboral(dia);
                                horarioLaboral.setHoraEntrada(horaEntrada.getTimeInMillis());
                                horarioLaboral.setHoraSalida(horaSalida.getTimeInMillis());

                                horarioLaboralDAO.insertarHorarioLaboral(horarioLaboral);
                            }
                        }
                    }
                    limpiarComponentes();
                    break;

                case "jbEliminar":
                    int resp = JOptionPane.showConfirmDialog(EmpleadoVista.this, "¿Esta seguro que desea eliminar el registro?");
                    if (resp == JOptionPane.OK_OPTION) {
                        horarioLaboralDAO.eliminarHorarioLaboral(numCedulaEmplOper);
                        empleadoDAO.eliminarEmpleado(numCedulaEmplOper);
                        //Se actualiza la tabla de Empleado.
                        empleadoConsultaVista.cargarTabla(new EmpleadoDAO().todosLosEmpleados());
                        dispose();
                    }
                    break;

                case "jbCancelar":
                    dispose();
                    break;

                case "jbInsertarFilas":
                    tableModel = (DefaultTableModel) jtCargaHoraria.getModel();

                    if (tableModel.getRowCount() < 10) {
                        tableModel.addRow(new String[]{"E", "", "", "", "", "", ""});
                        tableModel.addRow(new String[]{"S", "", "", "", "", "", ""});

                        if (!jbEliminarFilas.isEnabled()) jbEliminarFilas.setEnabled(true);
                    }

                    if (tableModel.getRowCount() == 10){
                        jbInsertarFilas.setEnabled(false);
                    }
                    break;

                case "jbEliminarFilas":
                    tableModel = (DefaultTableModel) jtCargaHoraria.getModel();

                    if (tableModel.getRowCount() != 0) {
                        tableModel.removeRow(tableModel.getRowCount() - 1);
                        tableModel.removeRow(tableModel.getRowCount() - 1);

                        if (!jbInsertarFilas.isEnabled()) jbInsertarFilas.setEnabled(true);
                    }

                    if (tableModel.getRowCount() == 0) {
                        jbEliminarFilas.setEnabled(false);
                    }
                    break;
            }
        }
    }
}
