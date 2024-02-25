package socketchat;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/*Esta primera linea de codigo declara una clase llamada servidor,
con la palabra public esto significa que la clase servidor es accesible
y visible desde cualquier parte del codigo, esta clase 'Servidor', es la clase
principal*/
public class Servidor {
    /*Se encuentra el metodo main, es el metodo principal de java, es el metodo
    que siempre se ejecuta al iniciar cualquier programa, los parametros String[] args
    permiten pasar argumentos desde la linea de comandos, pero como se esta usando un IDE,
    no se estan utilizando*/
    public static void main(String[] args) {
        /*Se crea una instancia de la clase 'ServidorCliente', llamada 'unServidor'
        esta instacia se utiliza para iniciar el servidor de este programa, cargando 
        la interfaz grafica del servidor*/
        ServidorCliente unServidor = new  ServidorCliente();
        /*Permite asignar la tarea o función de que cuando se cierre el Frame o la 
        ventana de la clase servidor el programa se cancele o termine su
        ejecución, esta opcion se logra con JFrame.EXIT_ON_CLOSE*/
        unServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } 
}
/*Se crea la clase 'ServidorCliente', esta clase es la encargada de la interfaz 
del servidor ya que hereda o extends de la clase 'JFrame', clase utilizada para 
crear una ventana, tambien se implementa la interfaz 'Runnable', esto para poder
ejecutar un hilo, este hilo se usa para escuhar las peticiones que envian los 
clientes por medio del socket*/
class ServidorCliente extends JFrame implements Runnable{
    /*Se declara una variable llamada 'areaTexto' de tipo JTextArea, esta variable
    se usa para poder mostrar los mensajes o la información que es enviada por 
    el cliente en la interfaz grafica del servidor*/
    private JTextArea areatexto;
    /*Se crea el constructor de la clase 'Servidor', en este constructor, se 
    configura la interfaz grafica y se crea un hilo para las conexiones con los 
    clientes*/
    public ServidorCliente(){
        /*Se definen las dimensiones y la posición de la ventana del servidor
        * X: La cordenada 'x' de la esquina superior izquierda de la ventana
        * Y: La coordenada 'y' de la esquina superior  izquierda de la ventana
        * width: Ancho de la ventana
        * heignt: Altura de la ventana*/
        setBounds(600,300,280,350);
        /*Se crea un objeto de 'JPanel' llamado 'milamina', este objeto se usa 
        como panel de la interfaz, adentro de este panel estaran los demas elementos
        como los botones*/
	JPanel milamina= new JPanel();
        /*Se llama al metodo 'setLayout', con el objeto 'milamina', para  utilizar 
        un el diseño con 'BorderLayout' para organizar los elementos por regiones
        como sur, este, oeste o centro*/
	milamina.setLayout(new BorderLayout());
        /*Se crea un objeto JTexArea llamado 'areatexto', es una caja de texto 
        se usa para mostrar la informacion que los clientes envian*/
        areatexto=new JTextArea();
        /*Se usa el metodo 'add', del objeto 'milamina', para añadirle un componente
        a este panel, en este case se paso el componente 'areatexto', al cual por medio
        de 'BorderLayout.CENTER', se posiciono el componente en el centro del panel*/
        milamina.add(areatexto,BorderLayout.CENTER);
        /*Se crea un objetode 'JLabel', llamado 'jLabelText', el cual se usa para
        mostrar un mensaje en la interfaz del servidor*/
        JLabel jLabelText = new JLabel("Esperando mensaje del cliente...");
        /*Con el metodo add del objeto milamina se agrega el jLabelText, en la 
        parte sur del panel*/
        milamina.add(jLabelText, BorderLayout.SOUTH); 
        /*Se usa el metodo add del JFrame que representa la instacia de la clase
        'ServidorCliente', para añadir el JPanel al Frame*/
        add(milamina);
        /*Se usa el metodo setVisible, sobre el Frame para poder configurar la 
        visiblidad de la ventana, como esta true, la ventana sera visible en la 
        interfaz grafica*/
        setVisible(true); 
        /*Se crea un objeto Thread llamado 'mihilo', para poder ejecutar un hilo 
        en segundo plano, se pasa como argumento 'this', como argumento al constructor
        del hilo para poder indicar que este hilo se va a ejecutar el cosigo de la 
        clase actual*/
        Thread mihilo = new Thread(this);
        /*Se utiliza el metodo 'start', para iniciar la ejecucion del hilo en segundo
        plano*/
        mihilo.start();
    }
    /*El @override indica que se esta implementando el metodo run de la interfaz
    Runnable, */
      @Override
      /*El metodo run define las acciones que debe ejecutar el hilo en segundo plano
      se usa como public void, porque necesita ser accedido desde cualquier parte del 
      programa, se usa void ya que este metodo no devuelve algun valor cuando se
      ejecuta, solo se usa para realizar acciones en segundo plano pero no devuelve 
      algun resultado especifico*/
        public void run() {
            /*Esta esctructura el try- catch, se usa para manejar excepciones, para
            validar errores o situaciones que sucedan durante el programa, evitando 
            que el programa se cierre sin alguna notificación de error*/
            try {
            //Codigo estar en la escucha hilo
            /*Se crea un objeto 'ServerSocket', utilizado para abrir un puerto
            de escucha en el servidor, se crea 'unServerSocket', una instancia del 
            'ServerSocket', en donde se especifica el puerto 536 que sera el puerto
            de escucha del servidor*/
            ServerSocket unServidor = new ServerSocket(536);//puerto de escucha abierto
            /*Se crean variables de tipo texto para almacenar la informacion 
             del paquete del cliente que fue enviado por el socket del cliente*/
            String nick, ip, mensaje;
            /*Se crea una variable llamada 'paquete_recibido' que es de tipo 'PaqueteEnvio'
            se usa para almacenar los objetos de tipo 'PaqueteEnvio', que son enviados 
            por la red.
            PaqueteEnvio es una clase de la clase Cliente, se usa para representar
            el paquete de datos que es enviado por el cliente al servidor, este paquete
            contiene la informacion de nick, ip, mensaje*/
            PaqueteEnvio paquete_recibido;
            //Se crea bucle infinito para escuchar siempre mensajes, por medio un hilo en segundo plano
            while(true){//Ciclo para escuchar siempre peticiones del cliente
                //aceptar conexion que venga para ese puerto
                /*Se acepta una conexion de un cliente en el servidor, el metodo
                accept() se usa para esperar y aceptar las conexiones de clientes*/
                Socket misocket = unServidor.accept();
                /*Se usa para tener una notificacion en consola que se acepto la 
                conexion del cliente*/
                System.out.println("Si entro a mensaje");
                //Flujo de entrada para recoger lo que envio el flujo paquete el cliente
                //(Flujo de mensaje)
                /*Se crea un flujo de entrada con 'ObjectInputStream' usado para leer
                objetos desde la conexion 'misocket', la conexion del cliente, este flujo 
                de entrada permite leer objetos serializados, del flujo se crea una
                variable llamada 'paquete_datos', para poder interacturar con el
                flujo de entrada*/
                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
                //Guardar en el objeto lo que viene en la red
                /*La variable 'paquete_recibido' anteriormente creada, almacena
                la lectura del objeto 'paquete_datos', obtenido de la red, con el 
                metodo readObject().
                'paquete_recibido' es un objeto de la clase 'PaqueteEnvio', clase
                del cliente, que contine la informacion enviada por el cliente, como
                nombre de usuario, ip y el mensaje*/
                paquete_recibido= (PaqueteEnvio) paquete_datos.readObject();//leer el flujo de datos
                //Obtener del paquete los datos
                /*Se recupera el valor de el nombre de usuario 'nick' del objeto
                'paquete_recibido', se usa el metodo getNick() de la clase 'PaqueteEnvio'
                para obtener el valor del nombre y guardarlo en la variable nick de la
                clase servidor
                Este proceso se realiza con los demas datos*/
                nick = paquete_recibido.getNick();
                ip = paquete_recibido.getIp();
                mensaje = paquete_recibido.getMensaje();
                /*Se agrega al componente 'areatexto' con el metodo append la información
                obtenida del paquete enviado por el cliente y leido por el flujo de entrada,
                para ser mostrada en el servidor*/
                areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);
                
                //<<Codigo para reenviar informacion del servidor al otro cliente>>
                /*Se crea una nueva conexion de socket llamada 'enviaDestinatario'
                a la cual se le pasa por parametro la variable ip, obtenida del objeto del
                cliente, la cual tiene la IP  del cliente al que se le envia el
                mensaje, ademas un puerto de escucha que seria el puerto devuelta el 
                cual debe estar vinculado a un socket de servidor para que reciba 
                el objeto del servidor principal*/
                Socket enviaDestinatario = new  Socket(ip,540);//puerto devuelta
                //Se crea un flujo de salida 
                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                /*se usa el flujo de salida paqueteReenvio, para escribir el objeto 
                de 'paquete_recibido', enviado por el cliente con el metodo writeObject,
                con el fin de enviar el 'paqueteReenvio', con la informacion del mensaje al
                cliente receptor*/
                paqueteReenvio.writeObject(paquete_recibido);
                //cerrar flujo datos
                paqueteReenvio.close();
                enviaDestinatario.close();
                //cerrar socket,cerramos la conexion
                misocket.close(); 
            }
           } catch (IOException ex) {
                Logger.getLogger(ServidorCliente.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(ServidorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
}
