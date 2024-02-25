package socketchat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
//importaciones para el servidor FTP
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.IOException;
import javax.swing.JOptionPane;

/*Esta primera linea de codigo declara una clase llamada Cliente,
con la palabra public esto significa que la clase Cliente es accesible
y visible desde cualquier parte del codigo, esta clase 'Cliente', es la clase
principal*/
public class Cliente {
    /*Se encuentra el metodo main, es el metodo principal de java, es el metodo
    que siempre se ejecuta al iniciar cualquier programa, los parametros String[] args
    permiten pasar argumentos desde la linea de comandos, pero como se esta usando un IDE,
    no se estan utilizando*/
    public static void main(String[] args) {
       /*Se crea una instancia de la clase 'VistaCliente', llamada 'unCliente'
       esta instacia se utiliza para iniciar el cliente de este programa, cargando 
       la interfaz grafica del cliente*/
       VistaCliente unCliente = new VistaCliente();
       /*Permite asignar la tarea o función de que cuando se cierre el Frame o la 
        ventana de la clase cliente el programa se cancele o termine su
        ejecución, esta opcion se logra con JFrame.EXIT_ON_CLOSE*/
       unCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } 
}
/*Se crea la clase 'VistaCliente', esta clase es la encargada de la interfaz 
del servidor ya que hereda o extends de la clase 'JFrame', clase utilizada para 
crear una ventana de cliente*/
class VistaCliente extends JFrame{
    /*Se crea el constructor de la clase 'VistaCliente', en este constructor, se 
    configura la interfaz grafica del cliente*/
    public VistaCliente(){
        /*Se definen las dimensiones y la posición de la ventana o frame  del cliente
        * X: La cordenada 'x' de la esquina superior izquierda de la ventana
        * Y: La coordenada 'y' de la esquina superior  izquierda de la ventana
        * width: Ancho de la ventana
        * heignt: Altura de la ventana*/
        setBounds(600,300,280,350);
        /*Se crea una instancia de la clase 'LaminaVistaCliente' con el nombre de
        'milamina', para poder interacturar con los elementos de la clase 'LaminaVistaCliente'*/
        LaminaVistaCliente milamina = new LaminaVistaCliente();
        /*Se usa el metodo add del JFrame , para añadir el el objeto milamina al Frame*/
        add(milamina);
        /*Se usa el metodo setVisible, sobre el Frame para poder configurar la 
        visiblidad de la ventana, como esta true, la ventana sera visible en la 
        interfaz grafica*/
        setVisible(true); 
    }
}
/*Se crea la clase 'LaminaVistaCliente', esta clase es la encargada de los 
componenetes de la interfaz del cliente ya que hereda o extends de la clase 'JPanel', 
clase utilizada para crear un panel dentro de un frame en donde colocar los componentes
visuales, tambien se implementa la interfaz 'Runnable', esto para poder
ejecutar un hilo, este hilo se usa para escuchar los objetos reenviados de la clase
servidor y mostrarlos en la clase cliente, permite ver los mensajes recibidos de un
cliente*/
class LaminaVistaCliente extends JPanel implements Runnable{//se crean todos elementos panel
    /*Se crea el constructor de la clase 'LaminaVistaCliente', en este constructor, se 
    configura los componentes de al interfaz grafica y se crea un hilo para la conexion con la
    clase servidor para recibir el paquete reenviado*/
    public LaminaVistaCliente(){
        /*Se crea una instancia de JTextField, llamada nick con un ancho de 5 columnas
        permite ingresar texto en la interfaz, este campo es para que el cliente ingrese
        su nombre de usuario*/
        nick = new JTextField(5);
        /*Con el metodo add añade este campo al JPanel*/
        add(nick);
        /*Se crea una etiqueta JLabel llamada texto que muestra el texto -CHAT-
        en la interfaz*/
        JLabel texto=new JLabel("-CHAT-");
        add(texto);//Con el metodo add añade este campo al JPanel
        /*Crea una instancia de JTextField, llamada ip con un ancho de 8 columnas
        permite ingresar texto en la interfaz, este campo es para que el cliente ingrese
        la IP de destino o del dispositivo que desea enviar el mensaje*/
        ip = new JTextField(8);
        add(ip);//Con el metodo add añade este campo al JPanel
        /*Se crea componente de area de texto, con los parametros de numeros de filas
        y columnas que indica el area inicial del area de texto, en este campo se 
        podran observar los mensajes enviados del otro cliente*/
        campochat = new JTextArea(12,20);//cordenada campo
        add(campochat);//Con el metodo add añade este campo al JPanel
        /*Crea una instancia de JTextField, llamada campo1 con un ancho de 20 columnas
        permite ingresar texto en la interfaz, este campo es para que el cliente ingrese
        el mensaje a enviar por la red*/
        campo1 = new JTextField(20);//campo escritura
        add(campo1);//Con el metodo add añade este campo al JPanel
        /*Se crea un boton de JButton llamado 'miBoton', con el texto 'Enviar', usado
        para que el cliente envie los mesajes por la red*/
        miBoton = new JButton("Enviar");
        /*Se crea una instancia de la clase EnviaTexto, para poder asociar una
        accion o evento al boton miBoton que se creo anteriormente*/
        EnviaTexto mievento = new EnviaTexto();
        /*Se agrega una accion de escucha al boton, para que cuendo el boton sea clicado,
        se ejecute el codigo de la clase EnviaTexto*/
        miBoton.addActionListener( mievento);//boton a la escucha
        add(miBoton);//Con el metodo add añade este campo al JPanel  
        
        /*Este programa incorpora el protocolo FTP aparte del socket*/
        /*Se crea un boton de JButton llamado 'consultarArchivo', con el texto 
        'Ver archivos servidor FTP', usado para que el cliente pueda consultar
        los archivos del servidor FTP local un servidor aparte del servidor del socket*/
        consultarArchivo = new JButton("Ver archivos servidor FTP");
         /*Se crea una instancia de la clase FTP, para poder asociar una
        accion o evento al boton consultarArchivo que se creo anteriormente*/
        FTP unftp = new FTP();
        consultarArchivo.addActionListener( unftp);
        add(consultarArchivo);//Con el metodo add añade este campo al JPanel  
        
        /*Se crea un objeto Thread llamado 'mihilo', para poder ejecutar un hilo 
        en segundo plano, se pasa como argumento 'this', como argumento al constructor
        del hilo para poder indicar que este hilo se va a ejecutar el cosigo de la 
        clase actual
        Se usa para crear una servidor socket el cual recibira los objetos que son enviados
        por la clase servidor*/
        Thread mihilo = new Thread(this); 
        /*Se utiliza el metodo 'start', para iniciar la ejecucion del hilo en segundo
        plano*/
        mihilo.start();
    }

    @Override
    public void run() {// escuche del cliente devuelta
        try{
            ServerSocket servidor_cliente = new  ServerSocket(540);
            Socket cliente; //canal recibe paquete socket
            PaqueteEnvio paqueteRecibido; //almacenar el paquete recibido devuelta
            
            while(true){
                cliente = servidor_cliente.accept(); //escuchar todas las peticiones
                //flujo de entrada capas de transportar objetos
                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
                //leer el objeto del flujo un cast para de objeto a paquete
                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();//esta variable tiene la informacion recibida
                
                //escribir en el area
                campochat.append("\n " + paqueteRecibido.getNick()+ ": " + paqueteRecibido.getMensaje());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    private  class EnviaTexto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(campo1);//escriba 
            //aparezca la conversacion de los clientes en campo
            campochat.append("\n" + campo1.getText());
            try {
            //crear Socket
            Socket misocket = new Socket("192.168.0.17",536);//cliente conectece al servidor con esta IP y utilice este puerto
            
            PaqueteEnvio datos = new  PaqueteEnvio();//empaquetar datos
            datos.setNick(nick.getText());//guardamos datos del campo
            datos.setIp(ip.getText());//guardamos datos de la ip
            datos.setMensaje(campo1.getText());//guardamos el mensaje escrito
            
            //flujo de datos de salida para enviar el objeto
            ObjectOutputStream paquete_datos = new ObjectOutputStream( misocket.getOutputStream());
            paquete_datos.writeObject(datos);
            //cerrar flujo
            paquete_datos.close();
            //cerrar socket
            misocket.close();
             } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    //clase con el protocolo FTP independiente del funcionamiento del socket de mensajes
    private class FTP  implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
   
            try {
                // Lógica para manejar el evento del botón FTP
                // Puedes obtener los valores necesarios, como rutas de archivo y credenciales, desde tu interfaz de usuario
                
                String server = "127.0.0.1";
                int port = 21; //cambio puerto se daña
                String username = "camilo"; //lo cree en el servidor del xammp permisos de archivos
                String password = "Puli";
                
                //creamos objetos
                FTPClient clienteFTP = new FTPClient();
                //validar conexion
                try {
                    clienteFTP.connect(server, port); //creamos la conexion
                    int respuesta = clienteFTP.getReplyCode();//codigo de respuesta del servidor
                    
                    //si la respuesta del servidor no es valida
                    if(!FTPReply.isPositiveCompletion(respuesta)){
                        System.out.println("Algo salio mal con la conexion del servidor");
                    }
                    
                    boolean inicioCorrecto = clienteFTP.login(username, password);//true
                    
                    if(inicioCorrecto){
                        JOptionPane.showMessageDialog(null, "Se inicio sesion en el servidor FTP de Xammp");
                    }else{
                         JOptionPane.showMessageDialog(null, "No se pudo hacer la conexion servidor las credenciales n oson validas");
                    }
                    
                } catch (IOException ex) {
                     JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
                
                // Crear una cadena para almacenar la lista de archivos
                StringBuilder listaArchivos = new StringBuilder();
                // listado de directorios, etc.
                
                String[] archivos;
                try {
                    archivos = clienteFTP.listNames();
                    for (String archivo : archivos) {
                        listaArchivos.append(archivo).append("\n"); // Agregar cada nombre de archivo y una nueva línea
                    }
                   JOptionPane.showMessageDialog(null, listaArchivos.toString(), "Lista de Archivos Servidor FTP", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
                
                clienteFTP.logout();
                clienteFTP.disconnect();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
    
    
     private JTextField campo1, nick, ip;
     private JTextArea campochat;//campo de los mensajes
     private JTextArea campochat1;//campo de los mensajes
     private JButton miBoton;
     private JButton consultarArchivo; //con FTP
}
//paquete de datos de texto
//serializable capas de convertirce en bits y reconvetir los bits para enviar por la red
/*Se crea la clase 'PaqueteEnvio', para crear objetos que contengan los parametros
de nick, ip y mensaje, estos objetos se empaquetaran y se enviaran por la red
por medio de la comunicacion del socket*/
  class PaqueteEnvio implements Serializable{ //Crear objeto para pasar los tres parametros
    /*variables para almacenar los datos del cliente para el envio del mensaje*/
    private String nick, ip, mensaje;
    /*Se crean los metodos get y set, get para poder obtener el valor
    del atributo guardado en las variables privadas anteriormente creadas
    y se usa el metodo set para escribir el valor del atributo en la variable
    */
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) { //almacenar
        this.nick = nick;
    }
    public String getIp() { //consultar
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
  }  


