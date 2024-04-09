import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    public static void main(String []args) throws SocketException, UnknownHostException, IOException{
        String mensaje = new String("Dame la hora local.");
        String servidor = new String("localhost");

        int puerto = 8080;
        int espera = 4000;

        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress hostservidor = InetAddress.getByName(servidor);

        DatagramPacket peticion = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, hostservidor, puerto);
        socketUDP.setSoTimeout(espera);
        System.out.println("Esperamos datos en un máximo de " + espera + " milisegundos...");
        socketUDP.send(peticion);

        try{
            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(respuesta);

            String[] respuestaSplit = new String(respuesta.getData(), 0, respuesta.getLength()).split(",");
            String strText = respuestaSplit[0];
            int calSegundos = Integer.parseInt(respuestaSplit[1]);

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaServidor = LocalDateTime.parse(strText, format);

            LocalDateTime horaCliente = LocalDateTime.now();
            int segundosLocal = horaCliente.toLocalTime().toSecondOfDay();
            int diferencia = Math.abs(calSegundos - segundosLocal);

            System.out.println("Hora del servidor es: " + horaServidor.format(format));
            System.out.println("Hora del cliente es: " + horaCliente.format(format));
            System.out.println("La diferencia entre las dos horas es: " + diferencia + " segundos");
            socketUDP.close();
        }catch(SocketTimeoutException s){
            System.out.println("Tiempo expirado para recibir respuesta: " + s.getMessage());
        }
    }
}
