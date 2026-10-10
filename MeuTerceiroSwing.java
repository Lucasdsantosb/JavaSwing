import javax.swing.JOptionPane;

public class MeuTerceiroSwing {

	public static void main(String[] args) {
	
		String entrada = JOptionPane.showInputDialog("Digite seu nome");{
		}
		
		String[] cores = {"amarelo", "azul", "branco"};
		Object result = JOptionPane.showInputDialog(null, "Qual sua cor preferida?", "Cores", JOptionPane.QUESTION_MESSAGE, null, cores, cores[0]);
		
}
	}
