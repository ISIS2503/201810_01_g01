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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;


/**
 * Ventana principal de la aplicación.
 */
public class Interfaz extends JFrame
{
    
    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
    
    /**
     * Panel con la imagen del encabezado.
     */
    private PanelImagen panelImagen;
    
    /**
     * Panel con la información del hotel de la reserva.
     */
    private PanelAlerta panelAlerta;
    
    
    // -----------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------
    
    /**
     * Constructor de la ventana principal.<br>
     * <b> post: </b> Construye la ventana principal de la aplicación.
     */
    public Interfaz( )
    {
        setLayout( new BorderLayout( ) );
        setTitle( "Policia nacional" );
        setSize( new Dimension( 550, 650 ) );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( true );
        
        panelImagen = new PanelImagen( );
        add( panelImagen, BorderLayout.NORTH );
        
        JPanel panelAux = new JPanel( );
        panelAux.setLayout( new BorderLayout( ) );
        TitledBorder bor = new TitledBorder( "Plataforma de alertas silenciosas" );
        bor.setTitleFont(new Font("SansSerif", Font.BOLD, 20));
        bor.setTitleJustification(2);
        panelAux.setBorder( bor  );
        
        panelAlerta = new PanelAlerta( this );
        panelAux.add( panelAlerta, BorderLayout.CENTER );
        
        add( panelAux, BorderLayout.CENTER );
       
    }
    
    // -----------------------------------------------------------------
    // Métodos
    // -----------------------------------------------------------------
    
    public void enviarAlerta(String alerta)
    {
    	panelAlerta.actualizar(alerta);
    }

}
