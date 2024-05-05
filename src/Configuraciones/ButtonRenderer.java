/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Configuraciones;

/**
 *
 * @author braul
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer extends DefaultTableCellRenderer {
    private final Icon icon;
    private boolean isPressed = false;
    private int pressedRow = -1;
    private int pressedCol = -1;

    public ButtonRenderer(Icon icon) {
        this.icon = icon;
        setHorizontalAlignment(JLabel.CENTER);
    }

    public void setPressed(boolean pressed, int row, int col) {
        isPressed = pressed;
        pressedRow = row;
        pressedCol = col;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
         super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText("");
        setIcon(icon);
        if (isPressed && row == pressedRow && column == pressedCol) {
            setBackground(Color.DARK_GRAY); // Color más oscuro al presionar
        } else {
            setBackground(Color.WHITE); // Color normal
        }
        return this;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}