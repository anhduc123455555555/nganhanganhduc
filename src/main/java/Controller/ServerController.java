package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import View.ServerView;
import model.Account;
import model.Chucnang;
import model.ServerModel;

public class ServerController {
	private static final int port=1000;
    private ServerModel model;
    private ServerView view;

    public ServerController(ServerModel model, ServerView view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            view.displayMessage("Server đang lắng nghe trên cổng " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                view.displayMessage("Client đã kết nối từ " + clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket, model, view).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private ServerModel model;
        private ServerView view;

        public ClientHandler(Socket socket, ServerModel model, ServerView view) {
            this.clientSocket = socket;
            this.model = model;
            this.view = view;
        }
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                Chucnang cn= new Chucnang();
                while ((inputLine = in.readLine()) != null) {
                    view.displayMessage("Nhận từ client: " + inputLine);
                    if("dangnhap".equals(inputLine)) {
                    	String sdt= in.readLine();
                    	String matkhau= in.readLine();
                    	String result = cn.dangnhap(sdt, matkhau);
                        out.println(result); // Gửi phản hồi cho client                   	
                    }
                    if ("chuyentien".equals(inputLine)) {
                        String sdt = in.readLine();
                        String tienguistring = in.readLine();
                        BigDecimal tiengui = new BigDecimal(tienguistring);
                        String tk = in.readLine();
                        String result = cn.chuyetien(sdt, tiengui, tk);
                        out.println("ựkgfsdgjksdhs");
                       
                    }


                   
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        ServerModel model = new ServerModel();
        ServerView view = new ServerView();
        ServerController controller = new ServerController(model, view);
        controller.start();
    }
}
