import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor{
    public static void main(String []args) throws SocketException, IOException{
        int puerto= 8080;

        int retardo = 3000;

        try (DatagramSocket socketUDP = new DatagramSocket(puerto)){
            byte[] buffer = new byte[1024];
            System.out.println("Servidor UDP escuchando en puerto: " + puerto + "...");
            while(true){
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(peticion);

                System.out.println("Datagrama recibido del host: " + peticion.getAddress());
                System.out.println("desde el puerto remoto: " + peticion.getPort());
                System.out.println("Datos recibidos: " + new String(peticion.getData()));

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String strFecha = now.format(format);
                System.out.println("La hora del servidor es: " + strFecha);

                int segundos = now.toLocalTime().toSecondOfDay();

                try {
                    System.out.println("Simulamos un retardo en milisegundos: " + retardo);
                    Thread.sleep(retardo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String mensajeRes = strFecha + "," + segundos;
                DatagramPacket respuesta = new DatagramPacket(mensajeRes.getBytes(), mensajeRes.getBytes().length, peticion.getAddress(), peticion.getPort());
                socketUDP.send(respuesta);
                System.out.println("Datos enviados al cliente.");
            }
        }
    }
}