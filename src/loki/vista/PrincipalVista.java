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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalVista extends JFrame {
    private JButton jbInserEmpl, jbConsEmpl, jbReporteHorarioLab, jbAcercaDe;
    private EventoActionListener actionListener;

    PrincipalVista () {
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setTitle("Sistema de Gestión de Horario Laboral");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        actionListener = new EventoActionListener ();

        //Se configura los botones.
        jbInserEmpl = crearBoton("Insertar", "empleado.png", "jbInserEmpl");
        jbConsEmpl = crearBoton("Consultar", "empleado.png", "jbConsEmpl");
        jbReporteHorarioLab = crearBoton("Reporte", "reporte.png", "jbReporteHorarioLab");
        jbAcercaDe = crearBoton("Acerca De", "acerca_de.png", "jbAcercaDe");

        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(jbInserEmpl);
        tb.add(jbConsEmpl);
        tb.add(jbReporteHorarioLab);
        tb.addSeparator();
        tb.add(jbAcercaDe);

        add(tb, BorderLayout.PAGE_START);

        JPanel jpFondo = new JPanel();
        jpFondo.setBackground(Color.lightGray);
        add(jpFondo, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton crearBoton (String text, String nombreImg, String actionCommand) {
        JButton button = new JButton(text);

        button.setIcon(new ImageIcon(getClass().getResource("img/" + nombreImg)));
        button.setMargin(new java.awt.Insets(2, 12, 2, 12));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.addActionListener(actionListener);
        button.setActionCommand(actionCommand);
        button.setFocusable(false);

        return button;
    }

    private class EventoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "jbInserEmpl":
                    new EmpleadoVista(PrincipalVista.this);
                    break;

                case "jbConsEmpl":
                    new EmpleadoConsultaVista(PrincipalVista.this);
                    break;

                case "jbReporteHorarioLab":
                    new HorarioLaboralVista(PrincipalVista.this);
                    break;

                case "jbAcercaDe":
                    new AcercaDeLoki(PrincipalVista.this);
                    break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new PrincipalVista();
    }
}
