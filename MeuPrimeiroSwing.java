import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MeuPrimeiroSwing {

	public static void main(String[] args) {
		JFrame janela = new JFrame("Minha primeira janela");
		
		JButton botao = new JButton("Clique aqui");
		
		botao.addActionListener(e ->{
			JOptionPane.showMessageDialog(janela, "Ola. Alunos");
		});
		
		janela.add(botao);
		
		janela.setSize(300,200);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);
		
		
		
		
		
		
		
		
		
	}

}
