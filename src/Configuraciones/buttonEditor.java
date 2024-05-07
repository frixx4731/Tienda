/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Configuraciones;

/**
 *
 * @author braul
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellEditor;

public class buttonEditor extends AbstractCellEditor implements TableCellEditor {
    JButton button;
    private String label;

    public buttonEditor(String text) {
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
        label = text;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        button.setText(label);
        if ("Eliminar".equals(label)) {
            button.setBackground(new java.awt.Color(255, 100, 100)); // Color rojo
        } else if ("Modificar".equals(label)) {
            button.setBackground(new java.awt.Color(100, 149, 237)); // Color azul
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}