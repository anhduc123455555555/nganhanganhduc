package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import View.Dangky;
import View.dangnhap;
import View.trangchu;
import model.ClientModel;

public class ClientController {
    private static final int port = 1000;
    private static String url = "localhost";
    private ClientModel model;
    private dangnhap dangnhapView;
    private Dangky dangkyView;
    private trangchu trangchuView;
   
    public ClientController() {
		// TODO Auto-generated constructor stub
	}
    public ClientController(dangnhap dangnhapView, Dangky dangkyView, trangchu trangchuView) {
        this.dangnhapView = dangnhapView;
        this.dangkyView = dangkyView;
        this.trangchuView = trangchuView;
        this.model = new ClientModel(); // Initialize model here
    }

    public void langnghe() {
    	if (trangchuView != null) {
            trangchuView.skchuyentien(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    trangchuView.showMessage("hfjsdjfjdsksd");
                }
            });
        }else {
        	System.out.println("null");
        }
    	
    	
        if (dangkyView != null) {
            dangkyView.skdangnhap(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dangkyView.getFrame().setVisible(false);
                    dangnhapView.getFrame().setVisible(true);
                }
            });

            dangkyView.addRegisterListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    taotaikhoan();
                }
            });
        }

        if (dangnhapView != null) {
            dangnhapView.addRegisterListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    login();
                }
            });

            dangnhapView.addRegisterListener1(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dangnhapView.getFrame().setVisible(false);
                    dangkyView.getFrame().setVisible(true);
                }
            });
        }
    }
    
    public void chuyentien() {
        if (trangchuView == null) {
            System.err.println("trangchuView is null, cannot perform chuyentien operation");
            return;
        }

        String phone = login();
        String tienguiString = trangchuView.getsotien();
        String tk = trangchuView.getstk();

        try {
            model.connect(url, port);
            model.sendMessage("chuyentien");
            model.sendMessage(phone);
            model.sendMessage(tienguiString);
            model.sendMessage(tk);
            
            String thongbao = model.receiveMessage();
            trangchuView.showMessage(thongbao);
            
            model.close();
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối đến server: " + e.getMessage());
        } finally {
            try {
                model.close(); // Đóng kết nối nếu có lỗi xảy ra
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    private String login() {
        if (dangnhapView == null) {
            System.err.println("dangnhapView is null, cannot perform login operation");
            return null; // or handle this case as needed
        }

        String sdt = dangnhapView.getUsername();
        String matkhau = dangnhapView.getPassword();
        
        try {
            model.connect(url, port);
            model.sendMessage("dangnhap");
            model.sendMessage(sdt);
            model.sendMessage(matkhau);

            String thongbao = model.receiveMessage();
            dangnhapView.showMessage(thongbao);
            
            if (thongbao.equals("Đăng nhập thành công")) {
                dangnhapView.getFrame().setVisible(false);
                trangchuView = new trangchu();
                trangchuView.getFrame().setVisible(true);
            }
            
            model.close();
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối đến server: " + e.getMessage());
        }
        
        return sdt;
    }

    private void taotaikhoan() {
        if (dangkyView == null) {
            System.err.println("dangkyView is null, cannot create account");
            return;
        }

        String tk = dangkyView.getstk();
        String ten = dangkyView.getname();
        String mk = dangkyView.getmk();
        String sdt = dangkyView.getsdt();

        try {
            model.connect(url, port);
            model.sendMessage("dangky");
            model.sendMessage(tk);
            model.sendMessage(ten);
            model.sendMessage(mk);
            model.sendMessage(sdt);

            String thongbao = model.receiveMessage();
            dangkyView.showMessage(thongbao);
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng ký tài khoản: " + e.getMessage());
        } finally {
            try {
                model.close();
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    dangnhap dn = new dangnhap();
                    Dangky dk = new Dangky();
                    trangchu tc = new trangchu();
                    ClientController controller = new ClientController(dn, dk, tc);
                    controller.langnghe();
                  
                    dn.getFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
