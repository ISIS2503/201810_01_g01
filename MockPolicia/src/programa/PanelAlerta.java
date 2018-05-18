/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n7_cupiViajes
 * Autor: Equipo Cupi2 2015
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
 */
package programa;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Panel con la información detallada de un hotel.
 */
public class PanelAlerta extends JPanel
{
    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
    
    /**
     * Ventana principal de la aplicación.
     */
    private Interfaz principal;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------
    
    

    /**
     * Campo de texto con el nombre del hotel reservado.
     */
    private JTextField txtAlerta;
    
    // -----------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------

    /**
     * Constructor del panel con la información del hotel.
     * <b> post: </b> Todos los elementos gráficos fueron inicializados.
     * @param pPrincipal Ventana principal de la aplicación. pPrincipal != null.
     */
    public PanelAlerta( Interfaz pPrincipal )
    {
        principal = pPrincipal;
        
        setBorder( new TitledBorder( "Alertas" ) );
        setLayout( new BorderLayout( ) );
        setPreferredSize( new Dimension( 550, 400 ) );

        txtAlerta = new JTextField( );
        txtAlerta.setEditable( false );
        Font font1 = new Font("SansSerif", Font.PLAIN, 16);
        txtAlerta.setFont(font1);
        add( txtAlerta );
    }

    // -----------------------------------------------------------------
    // Métodos
    // -----------------------------------------------------------------

    /**
     * Actualiza los campos con la información del hotel dado por parámetro.
     * @param pHotel Hotel seleccionado. pHotel != null.
     */
    public void actualizar( String alerta )
    {
        txtAlerta.setText(txtAlerta.getText()+"\n - "+alerta);
    }

}
