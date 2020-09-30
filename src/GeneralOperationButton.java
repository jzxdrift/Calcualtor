import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class GeneralOperationButton extends JButton implements ActionListener
{
	public GeneralOperationButton(String genOperator, KeyStroke key)
	{
		this.setText(genOperator);
		this.setFont(new Font("Arial", Font.PLAIN, 16));
		this.setFocusable(false);
		this.setPreferredSize(new Dimension(60, 60));
		this.addActionListener(this);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(key, "genOperator");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(KeyStroke.getKeyStroke('='), "genOperator");
		this.getActionMap().put("genOperator", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doClick();
			}
		});
	}
	
	private void clearEntry()
	{
		CalculatorMain.getResultField().setText("0");
	}
	
	private void clearAll()
	{
		//reset everything
		CalculatorMain.getResultField().setText("0");
		CalculatorMain.setTempOperand(0);
		CalculatorMain.setPercentValue(0);
		CalculatorMain.setLastBinOperator("");
		CalculatorMain.setBinOperatorClicked(false);
		CalculatorMain.setUnOperatorClicked(false);
		CalculatorMain.setMemOperatorClicked(false);
		CalculatorMain.setResultOperatorClicked(false);
	}
	
	private void backspace()
	{
		if(CalculatorMain.getResultField().getText().equals("0"))
			return;
		else if(CalculatorMain.getResultField().getText().length() == 1
				|| CalculatorMain.isUnOperatorClicked())
			CalculatorMain.getResultField().setText("0");
		else
			CalculatorMain.getResultField().setText
				(CalculatorMain.getResultField().getText().substring
						(0, CalculatorMain.getResultField().getText().length() - 1));
	}
	
	private void result()
	{
		//calculate using stored operator
		switch(CalculatorMain.getLastBinOperator())
		{
			case "\u00f7":
				if(CalculatorMain.getResultField().getText().equals("0"))
					CalculatorMain.getResultField().setText("Error");
				else
					CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() /
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				break;
			case "\u00d7":
				CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() *
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				break;
			case "-":
				CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() -
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				break;
			case "+":
				CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() +
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				break;
		}
		
		//handling percentages
		if(CalculatorMain.getPercentValue() != 0 && CalculatorMain.getTempOperand() == 0)
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
						(CalculatorMain.getPercentValue()));
		else if(CalculatorMain.getPercentValue() != 0 && CalculatorMain.getTempOperand() != 0)
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
						(CalculatorMain.getPercentValue() + CalculatorMain.getTempOperand()));
		
		if(CalculatorMain.getResultField().getText().endsWith(".0"))
			CalculatorMain.getResultField().setText
				(CalculatorMain.getResultField().getText().replace(".0", ""));
		
		CalculatorMain.setTempOperand(0);
		CalculatorMain.setPercentValue(0);
		CalculatorMain.setLastBinOperator("");
		CalculatorMain.setBinOperatorClicked(false);
		CalculatorMain.setUnOperatorClicked(false);
		CalculatorMain.setMemOperatorClicked(false);
		CalculatorMain.setResultOperatorClicked(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(this.getText())
		{
			case "CE":
				clearEntry();
				break;
			case "C":
				clearAll();
				break;
			case "\u25c4":
				backspace();
				break;
			case "=":
				result();
				break;
		}
	}	
}