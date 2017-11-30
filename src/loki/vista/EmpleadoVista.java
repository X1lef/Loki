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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private List<HorarioLaboral> listHorarioLab;

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
        //Se guarda el id del Empleado.
        numCedulaEmplOper = empleado.getNumeroDeCedula();
        cargarEmpleado(empleado);

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

        jcbActivo = new JCheckBox("Activo", true);
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

    private void cargarEmpleado (Empleado emp) {
        jtfId.setText(emp.getNumeroDeCedula());
        jtfNombre.setText(emp.getNombre());
        jtfApellido.setText(emp.getApellido());
        jcbActivo.setSelected(emp.isActivo());

        listHorarioLab = horarioLaboralDAO.obtenerHorarioLaboral(emp.getNumeroDeCedula());

        int c = 1, f = 0, cantFila = 4;
        DefaultTableModel tableModel = (DefaultTableModel) jtCargaHoraria.getModel();
        for (HorarioLaboral h : listHorarioLab) {

            if (c != h.getDiaLaboral()) {
                c ++;
                f = 0;
            } else {
                if (cantFila == f) {
                    cantFila += 2;
                    tableModel.addRow(new String[]{"E", "", "", "", "", "", ""});
                    tableModel.addRow(new String[]{"S", "", "", "", "", "", ""});
                }
            }
            tableModel.setValueAt(h.getHoraEntrada(), f, c);
            tableModel.setValueAt(h.getHoraSalida(), f + 1, c);

            f += 2;
        }
    }

    private boolean validarDatos () {
        if (jtfId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el número de cedula.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (jtfNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (jtfApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el apellido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        DefaultTableModel tm = (DefaultTableModel)jtCargaHoraria.getModel();

        if (tm.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el horario laboral.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String horaEntrada, horaSalida;
        LocalTime horaEntr, horaSal;

        for (int c = 1; c < 7; c ++) {
            for (int f = 0; f < tm.getRowCount(); f += 2) {
                horaEntrada = String.valueOf(tm.getValueAt(f, c));
                horaSalida = String.valueOf(tm.getValueAt(f + 1, c));

                if (!horaEntrada.trim().isEmpty()) {
                    if (horaValida(horaEntrada)) {
                        if (horaSalida.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Debe ingresar el horario de salida. Fila " + (f + 2) + ", Columna " + c,
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            jtCargaHoraria.setRowSelectionInterval(f + 1, f + 1);
                            jtCargaHoraria.setColumnSelectionInterval(c, c);
                            return false;
                        } else {
                            if (horaValida(horaSalida)) {
                                if (horaEntrada.equals(horaSalida)) {
                                    JOptionPane.showMessageDialog(this, "Las horas de entrada y salida no deben ser iguales. Fila " + (f + 1) + ", Columna " + c,
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    jtCargaHoraria.setRowSelectionInterval(f, f);
                                    jtCargaHoraria.setColumnSelectionInterval(c, c);
                                    return false;
                                }

                                //Hora de entrada y salida no estan vacios.
                                horaEntr = LocalTime.parse(horaEntrada, DateTimeFormatter.ofPattern("H:mm"));
                                horaSal = LocalTime.parse(horaSalida, DateTimeFormatter.ofPattern("H:mm"));

                                //Si la hora de entrada es mayor a la hora de salida.
                                if (horaEntr.isAfter(horaSal)) {
                                    JOptionPane.showMessageDialog(this, "La hora de entrada debe ser menor que la hora de salida. Fila " + (f + 1) + ", Columna " + c,
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    jtCargaHoraria.setRowSelectionInterval(f, f);
                                    jtCargaHoraria.setColumnSelectionInterval(c, c);
                                    return false;
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "El formato de la hora es incorrecto, formato correcto HH:MM. Fila " + (f + 2) + ", Columna " + c,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                jtCargaHoraria.setRowSelectionInterval(f + 1, f + 1);
                                jtCargaHoraria.setColumnSelectionInterval(c, c);
                                return false;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El formato de la hora es incorrecto, formato correcto HH:MM. Fila " + (f + 1) + ", Columna " + c,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        jtCargaHoraria.setRowSelectionInterval(f, f);
                        jtCargaHoraria.setColumnSelectionInterval(c, c);
                        return false;
                    }
                } else {
                    //Si la hora de entrada y la hora de salida están vacíos salir del ciclo.
                    if (f > 1 && horaSalida.trim().isEmpty()) break;

                    JOptionPane.showMessageDialog(this, "Debe ingresar el horario de entrada. Fila " + (f + 1) + ", Columna " + c,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jtCargaHoraria.setRowSelectionInterval(f, f);
                    jtCargaHoraria.setColumnSelectionInterval(c, c);
                    return false;
                }
            }

            //Validar que las horas de un día esteen en orden ascendente.
            for (int f = 1; f < tm.getRowCount() - 1; f += 2) {
                //Se obtiene hora de salida.
                horaSalida = String.valueOf(tm.getValueAt(f, c)).trim();
                //Se obtiene hora de entrada.
                horaEntrada = String.valueOf(tm.getValueAt(f + 1, c)).trim();
                if (!horaEntrada.isEmpty()) {
                    if (horaEntrada.equals(horaSalida)) {
                        JOptionPane.showMessageDialog(this, "Las horas de entrada y salida no deben ser iguales. Fila " + (f + 2) + ", Columna " + c,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        jtCargaHoraria.setRowSelectionInterval(f + 1, f + 1);
                        jtCargaHoraria.setColumnSelectionInterval(c, c);
                        return false;
                    }

                    horaEntr = LocalTime.parse(horaEntrada, DateTimeFormatter.ofPattern("H:mm"));
                    horaSal = LocalTime.parse(horaSalida, DateTimeFormatter.ofPattern("H:mm"));

                    if (horaSal.isAfter(horaEntr)) {
                        JOptionPane.showMessageDialog(this, "La hora de salida debe ser menor que la hora de entrada. Fila " + (f + 1) + ", Columna " + c,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        jtCargaHoraria.setRowSelectionInterval(f, f); //Se selecciona la fila.
                        jtCargaHoraria.setColumnSelectionInterval(c, c); //Se selecciona la columna.
                        return false;
                    }
                } else {
                    break;
                }
            }
        }

        return true;
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

    private boolean horaValida (String hora) {
        return hora.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
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
                    Empleado empleado = new Empleado();
                    HorarioLaboral horarioLaboral = new HorarioLaboral();
                    String horaEntrada, horaSalida;

                    if (validarDatos()) {
                        //Se obtiene los datos del empleado.
                        empleado.setNumeroDeCedula(jtfId.getText().trim());
                        empleado.setNombre(jtfNombre.getText().trim());
                        empleado.setApellido(jtfApellido.getText().trim());
                        empleado.setActivo(jcbActivo.isSelected());

                        //Se comprueba si se pudo guardar el empleado.
                        if (empleadoDAO.insertarEmpleado(empleado)) {

                            //Se obtiene los datos del horario laboral del empleado.
                            for (int f = 0; f < tableModel.getRowCount(); f += 2) {
                                for (int c = 1; c < 7; c++) {
                                    horaEntrada = String.valueOf(tableModel.getValueAt(f, c));

                                    //Si la hora de entrada esta vacia saltear celda.
                                    if (!horaEntrada.trim().isEmpty()) {
                                        horaSalida = String.valueOf(tableModel.getValueAt(f + 1, c));

                                        //Se obtiene el horario laboral
                                        horarioLaboral.setIdEmpleado(jtfId.getText());
                                        horarioLaboral.setDiaLaboral(c);
                                        horarioLaboral.setHoraEntrada(horaEntrada);
                                        horarioLaboral.setHoraSalida(horaSalida);

                                        horarioLaboralDAO.insertarHorarioLaboral(horarioLaboral);
                                    }
                                }
                            }

                            JOptionPane.showMessageDialog(EmpleadoVista.this, "Se ha guardado correctamente el registro.");
                            limpiarComponentes();
                        }
                    }
                    break;

                case "jbEliminar":
                    int resp = JOptionPane.showConfirmDialog(EmpleadoVista.this, "¿Esta seguro que desea eliminar el registro?");
                    if (resp == JOptionPane.OK_OPTION) {
                        horarioLaboralDAO.eliminarHorarioLaboral(numCedulaEmplOper);
                        empleadoDAO.eliminarEmpleado(numCedulaEmplOper);
                        //Se actualiza la tabla de Empleados.
                        empleadoConsultaVista.cargarTabla(new EmpleadoDAO().todosLosEmpleados());
                        dispose();
                    }
                    break;

                case "jbActualizar":
                    if (validarDatos()) {
                        tableModel = (DefaultTableModel) jtCargaHoraria.getModel();

                        //Se obtiene los datos del empleado.
                        empleado = new Empleado();
                        empleado.setNumeroDeCedula(numCedulaEmplOper);
                        empleado.setNumeroDeCedulaNuevo(jtfId.getText().trim());
                        empleado.setNombre(jtfNombre.getText().trim());
                        empleado.setApellido(jtfApellido.getText().trim());
                        empleado.setActivo(jcbActivo.isSelected());

                        //Se comprueba si se pudo actualizar el empleado.
                        if (empleadoDAO.actualizarEmpleado(empleado)) {

                            //Se obtiene el horario laboral del empleado.
                            int index = 0;
                            HorarioLaboral horarioLabMod;
                            horarioLaboral = new HorarioLaboral();
                            for (int c = 1; c < 7; c++) {
                                for (int f = 0; f < tableModel.getRowCount(); f += 2) {
                                    horaEntrada = String.valueOf(tableModel.getValueAt(f, c));

                                    //Si la hora de entrada esta vacia saltear celda.
                                    if (!horaEntrada.trim().isEmpty()) {
                                        horaSalida = String.valueOf(tableModel.getValueAt(f + 1, c));
                                        horarioLabMod = listHorarioLab.get(index);

                                        //Se comprueba si son iguales los días.
                                        if (horarioLabMod.getDiaLaboral() == c) {

                                            //Se comprueba si se ha alterado los datos.
                                            if (!horarioLabMod.getHoraEntrada().equals(horaEntrada) ||
                                                    !horarioLabMod.getHoraSalida().equals(horaSalida)) {

                                                //Operación para actualizar registro.
                                                horarioLaboral.setIdCargaHoraria(horarioLabMod.getIdCargaHoraria());
                                                horarioLaboral.setIdEmpleado(jtfId.getText().trim());
                                                horarioLaboral.setDiaLaboral(c);
                                                horarioLaboral.setHoraEntrada(horaEntrada);
                                                horarioLaboral.setHoraSalida(horaSalida);

                                                horarioLaboralDAO.actualizarHorarioLaboral(horarioLaboral);

                                            }
                                            index++;

                                        } else {
                                            if (listHorarioLab.get(index).getDiaLaboral() > c) {
                                                //Operación para insertar nuevo registro.
                                                horarioLaboral.setIdEmpleado(jtfId.getText().trim());
                                                horarioLaboral.setDiaLaboral(c);
                                                horarioLaboral.setHoraEntrada(horaEntrada);
                                                horarioLaboral.setHoraSalida(horaSalida);

                                                horarioLaboralDAO.insertarHorarioLaboral(horarioLaboral);

                                            } else {
                                                //Operación para eliminar registro.
                                                horarioLaboralDAO.eliminarHorarioLaboral(horarioLabMod.getIdCargaHoraria());

                                                f -= 2; //Se decrementa la fila de la tabla.
                                                index++;
                                            }
                                        }
                                    }
                                }
                            }

                            JOptionPane.showMessageDialog(EmpleadoVista.this, "Se ha actualizado correctamente el registro.");

                            //Se actualiza la tabla de Empleados.
                            empleadoConsultaVista.cargarTabla(new EmpleadoDAO().todosLosEmpleados());
                            dispose();
                        }
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