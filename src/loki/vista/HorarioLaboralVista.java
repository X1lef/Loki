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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        setVisible(true);
    }

    private JPanel panelDiaLaboral () {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Día Laboral", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));


        jrbLunes = new JRadioButton("L", true);
        jrbLunes.setActionCommand("jrbLunes");
        jrbLunes.addActionListener(actionListener);

        jrbMartes = new JRadioButton("M");
        jrbMartes.setActionCommand("jrbMartes");
        jrbMartes.addActionListener(actionListener);

        jrbMiercoles = new JRadioButton("X");
        jrbMiercoles.setActionCommand("jrbMiercoles");
        jrbMiercoles.addActionListener(actionListener);

        jrbJueves = new JRadioButton("J");
        jrbJueves.setActionCommand("jrbJueves");
        jrbJueves.addActionListener(actionListener);

        jrbViernes = new JRadioButton("V");
        jrbViernes.setActionCommand("jrbViernes");
        jrbViernes.addActionListener(actionListener);

        jrbSabado = new JRadioButton("S");
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
        jtHorarioLaboralPorDia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtHorarioLaboralPorDia.getTableHeader().setReorderingAllowed(false);

        panel.add(new JScrollPane(jtHorarioLaboralPorDia), conf);

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

                    switch (event.getActionCommand()) {
                        case "jrbMartes":
                            if (jrbMartes.isSelected()) {
                                dia = "Martes";
                            }
                            break;

                        case "jrbMiercoles":
                            if (jrbMiercoles.isSelected()) {
                                dia = "Miercoles";
                            }
                            break;

                        case "jrbJueves":
                            if (jrbJueves.isSelected()) {
                                dia = "Jueves";
                            }
                            break;

                        case "jrbViernes":
                            if (jrbViernes.isSelected()) {
                                dia = "Viernes";
                            }
                            break;

                        case "jrbSabado":
                            if (jrbSabado.isSelected()) {
                                dia = "Sabado";
                            }
                            break;
                    }

                    jpHorarioLaboral.setBorder(BorderFactory.createTitledBorder(null, dia, TitledBorder.CENTER,
                            TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.BOLD, 11)));
                    //TODO: Se debe crear el método cargarTabla.
            }
        }
    }
}
