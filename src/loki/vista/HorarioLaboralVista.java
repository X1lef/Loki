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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.TreeSet;

public class HorarioLaboralVista extends JDialog {
    private JRadioButton jrbLunes, jrbMartes, jrbMiercoles, jrbJueves, jrbViernes, jrbSabado;
    private JTable jtHorarioLaboralPorDia;
    private JButton jbCancelar;
    private JPanel jpHorarioLaboral;
    private EventoActionListener actionListener;

    HorarioLaboralVista (JFrame frame) {
        super (frame, "Horario Laboral", true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        setSize(950, 500);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        actionListener = new EventoActionListener();

        JPanel panelPrinc = new JPanel();
        panelPrinc.setLayout(new BoxLayout(panelPrinc, BoxLayout.Y_AXIS));
        panelPrinc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelPrinc.add(panelDiaLaboral());
        panelPrinc.add(Box.createRigidArea(new Dimension(0,10)));
        jpHorarioLaboral = panelHorarioLaboralDia();
        panelPrinc.add(jpHorarioLaboral);
        panelPrinc.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrinc.add(panelBoton());

        add(panelPrinc);

        int indexDia = diaLaboralActual();
        cargarTabla(indexDia);
        seleccionarDia(indexDia);

        setVisible(true);
    }

    private int diaLaboralActual () {
        LocalDate ld = LocalDate.now();
        int dia = ld.getDayOfWeek().getValue();
        if (dia == 7) dia = 1;

        return dia;
    }

    private void seleccionarDia (int dia) {
        String diaSemana = "Sábado";

        switch (dia) {
            case 1:
                jrbLunes.setSelected(true);
                diaSemana = "Lunes";
                break;

            case 2:
                jrbMartes.setSelected(true);
                diaSemana = "Martes";
                break;

            case 3:
                jrbMiercoles.setSelected(true);
                diaSemana = "Miércoles";
                break;

            case 4:
                jrbJueves.setSelected(true);
                diaSemana = "Jueves";
                break;

            case 5:
                jrbViernes.setSelected(true);
                diaSemana = "Viernes";
                break;

            case 6:
                jrbSabado.setSelected(true);
                break;
        }

        jpHorarioLaboral.setBorder(BorderFactory.createTitledBorder(null, diaSemana, TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));
    }

    private JPanel panelDiaLaboral () {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Día Laboral", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));


        jrbLunes = new JRadioButton("L", true);
        jrbLunes.setFocusable(false);
        jrbLunes.setActionCommand("jrbLunes");
        jrbLunes.addActionListener(actionListener);

        jrbMartes = new JRadioButton("M");
        jrbMartes.setFocusable(false);
        jrbMartes.setActionCommand("jrbMartes");
        jrbMartes.addActionListener(actionListener);

        jrbMiercoles = new JRadioButton("X");
        jrbMiercoles.setFocusable(false);
        jrbMiercoles.setActionCommand("jrbMiercoles");
        jrbMiercoles.addActionListener(actionListener);

        jrbJueves = new JRadioButton("J");
        jrbJueves.setFocusable(false);
        jrbJueves.setActionCommand("jrbJueves");
        jrbJueves.addActionListener(actionListener);

        jrbViernes = new JRadioButton("V");
        jrbViernes.setFocusable(false);
        jrbViernes.setActionCommand("jrbViernes");
        jrbViernes.addActionListener(actionListener);

        jrbSabado = new JRadioButton("S");
        jrbSabado.setFocusable(false);
        jrbSabado.setActionCommand("jrbSabado");
        jrbSabado.addActionListener(actionListener);

        ButtonGroup bg = new ButtonGroup();
        bg.add(jrbLunes);
        bg.add(jrbMartes);
        bg.add(jrbMiercoles);
        bg.add(jrbJueves);
        bg.add(jrbViernes);
        bg.add(jrbSabado);

        panel.add(jrbLunes);
        panel.add(jrbMartes);
        panel.add(jrbMiercoles);
        panel.add(jrbJueves);
        panel.add(jrbViernes);
        panel.add(jrbSabado);

        return panel;
    }

    private JPanel panelHorarioLaboralDia () {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(null, "Lunes", TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.insets = new Insets(10, 10, 10, 10);
        conf.weightx = conf.weighty = 1.0;
        conf.fill = GridBagConstraints.BOTH;

        jtHorarioLaboralPorDia = new JTable(new ModeloTabla());
        jtHorarioLaboralPorDia.setDefaultRenderer(Object.class, new CeldaRenderizado());
        jtHorarioLaboralPorDia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtHorarioLaboralPorDia.getTableHeader().setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(jtHorarioLaboralPorDia);
        sp.getViewport().setBackground(Color.lightGray);
        panel.add(sp, conf);

        return panel;
    }

    private JPanel panelBoton () {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        jbCancelar = new JButton("Cancelar");
        jbCancelar.setActionCommand("jbCancelar");
        jbCancelar.addActionListener(actionListener);
        panel.add(jbCancelar);

        return panel;
    }

    private void cargarTabla (int indexDia) {
        jtHorarioLaboralPorDia.setModel(new ModeloTabla());

        DefaultTableModel tableModel = (DefaultTableModel) jtHorarioLaboralPorDia.getModel();

        //Obtener a todos los empleados activos.
        List<Empleado> listEmp = new EmpleadoDAO().todosLosEmpleadosActivos();
        String[] arrayEmp = new String[listEmp.size() + 1];
        StringBuilder nombreApellido;

        //Se carga el nombre de los instructores.
        for (int i = 0; i < listEmp.size(); i ++) {
            nombreApellido = new StringBuilder();
            nombreApellido.append(listEmp.get(i).getNombre());
            nombreApellido.append(" ");
            nombreApellido.append(listEmp.get(i).getApellido());
            arrayEmp[i] = nombreApellido.toString();
        }
        arrayEmp[listEmp.size()] = "Cantidad de instructores";

        //Se agrega la primera celda.
        tableModel.addColumn("Instructor", arrayEmp);

        //Se obtiene los horarios laborales de los empleados de un determinado día.
        List<HorarioLaboral> listHorarioLab = new HorarioLaboralDAO().obtenerHorarioLabDia(indexDia);

        TreeSet<Hora> tsHora = new TreeSet<>();
        Hora hora;

        //Se cargar el TreeSet.
        for (HorarioLaboral hl : listHorarioLab) {
            hora = new Hora();

            //Se guarda hora de entrada.
            hora.setHora(hl.getHoraEntrada());
            tsHora.add(hora);

            //Se guarda hora de salida.
            hora = new Hora();
            hora.setHora(hl.getHoraSalida());
            tsHora.add(hora);
        }

        List<Hora> listCabecera = new ArrayList<>(tsHora); //Contiene las horas que hay en el día seleccionado.
        int cantEmpl, length = listCabecera.size() - 1;
        String horaBuscar;

        for (int c = 1, k = 0; k < length; c ++, k ++) {
            horaBuscar = listCabecera.get(k).hora;
            cantEmpl = 0;

            //Se agrega las cabeceras.
            if (!jrbSabado.isSelected() && "12:00".equals(listCabecera.get(k + 1).hora) || k + 1 == length) {
                k ++;
                tableModel.addColumn(horaBuscar + " - " + listCabecera.get(k).hora);
            } else {
                tableModel.addColumn(horaBuscar);
            }

            if (c == 1) {
                for (int f = 0; f < tableModel.getRowCount() - 1; f++) {
                    for (HorarioLaboral hl : listHorarioLab) {
                        if (hl.getIdEmpleado().equals(listEmp.get(f).getNumeroDeCedula())) {
                            String h = hl.getHoraEntrada();

                            if (h.equals(horaBuscar)) {
                                tableModel.setValueAt(new ImageIcon(getClass().getResource("img/presente.png")), f, c);
                                cantEmpl++;
                                break;
                            }
                        }
                    }
                }
                //Se agrega cantidad de instructores.
                tableModel.setValueAt(cantEmpl, tableModel.getRowCount() - 1, c);

            } else {
                boolean entradaSalida;
                for (int f = 0; f < tableModel.getRowCount() - 1; f++) {
                    entradaSalida = false;
                    //Se comprueba si la hora de entrada y salida del empleado coincide con la hora buscada.
                    for (HorarioLaboral hl : listHorarioLab) {
                        if (hl.getIdEmpleado().equals(listEmp.get(f).getNumeroDeCedula())) {
                            String h = hl.getHoraEntrada();

                            if (h.equals(horaBuscar)) {
                                tableModel.setValueAt(new ImageIcon(getClass().getResource("img/presente.png")), f, c);
                                cantEmpl++;
                                entradaSalida = true;
                                break;
                            }

                            h = hl.getHoraSalida();

                            if (h.equals(horaBuscar)) {
                                entradaSalida = true;
                                break;
                            }
                        }
                    }

                    if (!entradaSalida) {
                        //Extraigo la hora anterior.
                        String horaAnt = listCabecera.get(k - 1).hora;

                        /*
                        Si son las 12:00hs todos los empleados salieron, así que al volver solo se tendrá
                        que cargar sus horarios de entrada.
                        Los sábados si se debe tener en cuenta las 12:00hs.
                         */
                        if (jrbSabado.isSelected() || !horaAnt.equals("12:00")) {
                            Icon icon = (Icon)tableModel.getValueAt(f, c - 1);
                            if (icon != null) {
                                cantEmpl++;
                                tableModel.setValueAt(new ImageIcon(getClass().getResource("img/presente.png")), f, c);
                            }
                        }
                    }
                }
                //Se agrega cantidad de instructores.
                tableModel.setValueAt(cantEmpl, tableModel.getRowCount() - 1, c);
            }
        }

        //Se configura el campo Instructor.
        TableColumn tc = jtHorarioLaboralPorDia.getColumn("Instructor");
        tc.setMinWidth(250);
        tc.setPreferredWidth(250);
        tc.setCellRenderer(jtHorarioLaboralPorDia.getTableHeader().getDefaultRenderer());
    }

    private class ModeloTabla extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private class EventoActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            switch (event.getActionCommand()) {
                case "jbCancelar":
                    dispose();
                    break;
                default:
                    String dia = "Lunes";
                    int indexDia = 1;

                    switch (event.getActionCommand()) {
                        case "jrbMartes":
                            if (jrbMartes.isSelected()) {
                                dia = "Martes";
                                indexDia = 2;
                            }
                            break;

                        case "jrbMiercoles":
                            if (jrbMiercoles.isSelected()) {
                                dia = "Miércoles";
                                indexDia = 3;
                            }
                            break;

                        case "jrbJueves":
                            if (jrbJueves.isSelected()) {
                                dia = "Jueves";
                                indexDia = 4;
                            }
                            break;

                        case "jrbViernes":
                            if (jrbViernes.isSelected()) {
                                dia = "Viernes";
                                indexDia = 5;
                            }
                            break;

                        case "jrbSabado":
                            if (jrbSabado.isSelected()) {
                                dia = "Sábado";
                                indexDia = 6;
                            }
                            break;
                    }

                    jpHorarioLaboral.setBorder(BorderFactory.createTitledBorder(null, dia, TitledBorder.CENTER,
                            TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));
                    cargarTabla(indexDia);
            }
        }
    }

    private class CeldaRenderizado extends DefaultTableCellRenderer {
        protected void setValue(Object value) {
            //Valor por defecto.
            setText(null);
            setIcon(null);

            if (value instanceof Icon) {
                setIcon((Icon)value);
                setHorizontalAlignment(SwingConstants.CENTER);

            } else {
                if (value != null) {
                    setText(String.valueOf(value));
                    setFont(new Font("Tahoma", Font.BOLD, 11));
                    setForeground(new Color(0x013ADF));
                    setHorizontalTextPosition(SwingConstants.CENTER);
                }
            }
        }
    }

    //Clase que utiliza el TreeSet para guardar las horas y ordenarlas.
    private class Hora implements Comparable<Hora> {
        private String hora;

        String getHora() {
            return hora;
        }

        void setHora(String hora) {
            this.hora = hora;
        }

        @Override
        public int compareTo(Hora h) {
            //Se extrae la hora y el minuto.
            String[] hora1 = h.hora.split(":");
            String[] hora2 = hora.split(":");

            //Convertir a integer.
            int h1 = Integer.parseInt(hora1[0]);
            int m1 = Integer.parseInt(hora1[1]);

            int h2 = Integer.parseInt(hora2[0]);
            int m2 = Integer.parseInt(hora2[1]);

            if (h2 > h1) return 1;
            if (h2 < h1) return -1;
            if (h1 == h2) {
                if (m2 > m1) return 1;
                if (m2 < m1) return -1;
            }

            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Hora) {
                Hora h = (Hora) o;
                return h.hora.equals(hora);
            }

            return false;
        }
    }
}
