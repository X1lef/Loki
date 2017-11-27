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

public class AcercaDeLoki extends JDialog {
    private JLabel jlSO, jlJRE, jlJVM;
    private static final String VERSION = "1.0";

    AcercaDeLoki (JFrame frame) {
        super (frame, "Acerca de Loki", true);
        setResizable(false);
        setLayout(new GridBagLayout());

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.weightx = 1.0;
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.insets = new Insets(10, 10, 0, 10);

        JLabel jlTitulo = new JLabel("Loki", JLabel.CENTER);
        jlTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        add (jlTitulo, conf);

        //Fila 1 columna 0.
        conf.gridy = 1;
        conf.insets = new Insets(18, 32, 10, 32);

        add (new JLabel("Versión : " + VERSION), conf);

        //Fila 2 columna 0.
        conf.gridy = 2;
        conf.insets = new Insets(0, 32, 10, 32);

        add (new JLabel("Licencia : GPL"), conf);

        //Fila 3 columna 0.
        conf.gridy = 3;
        add (new JLabel("Autor : Félix Pedrozo"), conf);

        //Fila 4 columna 0.
        conf.gridy = 4;

        jlSO = new JLabel();
        add (jlSO, conf);

        //Fila 5 columna 0.
        conf.gridy = 5;

        jlJRE = new JLabel();
        add (jlJRE, conf);

        //Fila 6 columna 0.
        conf.gridy = 6;
        conf.insets = new Insets(0, 32, 25, 32);

        jlJVM =  new JLabel();
        add (jlJVM, conf);

        cargarEtiquetas();
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void cargarEtiquetas () {
        jlSO.setText("S.O : " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        jlJVM.setText("JVM : " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
        jlJRE.setText("JRE : " + System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"));
    }
}
