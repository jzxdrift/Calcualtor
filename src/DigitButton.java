import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class DigitButton extends JButton implements ActionListener
{
	private static final int MAX_LENGTH = 8;
	
	public DigitButton(String digit)
	{
		this.setText(digit);
		this.setFont(new Font("Arial", Font.BOLD, 16));
		this.setFocusable(false);
		this.setPreferredSize(new Dimension(60, 60));
		this.addActionListener(this);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(KeyStroke.getKeyStroke(this.getText().charAt(0)), "digit");
		this.getActionMap().put("digit", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doClick();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//first symbol of new entry after some operation
		if((!CalculatorMain.getLastBinOperator().equals("") && CalculatorMain.isBinOperatorClicked())
				|| CalculatorMain.isUnOperatorClicked() || CalculatorMain.isResultOperatorClicked()
					|| CalculatorMain.isMemOperatorClicked()
						|| CalculatorMain.getResultField().getText().equals("Error"))
		{
			CalculatorMain.getResultField().setText(CalculatorMain.getResultField().getText().replace
					(CalculatorMain.getResultField().getText(), ""));
			CalculatorMain.setBinOperatorClicked(false);
			CalculatorMain.setUnOperatorClicked(false);
			CalculatorMain.setMemOperatorClicked(false);
			CalculatorMain.setResultOperatorClicked(false);
		}
		
		//rest of the entry
		if(CalculatorMain.getResultField().getText().length() < MAX_LENGTH)
		{
			//restricting decimal point
			if(this.getText().equals(".") && !CalculatorMain.getResultField().getText().contains("."))
				CalculatorMain.getResultField().setText(CalculatorMain.getResultField().getText()
						+ this.getText());
			
			if(CalculatorMain.getResultField().getText().equals("0"))
				CalculatorMain.getResultField().setText("" + this.getText());
			else if(!this.getText().equals("."))
				CalculatorMain.getResultField().setText(CalculatorMain.getResultField().getText()
						+ this.getText());
		}
	}
}