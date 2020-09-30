import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class BinaryOperationButton extends JButton implements ActionListener
{
	public BinaryOperationButton(String binOperator, KeyStroke key)
	{
		this.setText(binOperator);
		this.setFont(new Font("Arial", Font.PLAIN, 16));
		this.setFocusable(false);
		this.setPreferredSize(new Dimension(60, 60));
		this.addActionListener(this);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(key, "binOperator");
		this.getActionMap().put("binOperator", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doClick();
			}
		});
	}
	
	private void division()
	{
		CalculatorMain.setBinOperatorClicked(true);
		//if no previous operator exists
		if(CalculatorMain.getLastBinOperator().equals(""))
		{
			CalculatorMain.setTempOperand(Double.parseDouble(CalculatorMain.getResultField().getText()));
			CalculatorMain.setLastBinOperator("\u00f7");
		}
		//if any previous operator exists
		else if(!CalculatorMain.getLastBinOperator().equals(""))
		{
			performLastOperation();
			CalculatorMain.setLastBinOperator("\u00f7");
		}
		else
		{
			if(CalculatorMain.getResultField().getText().equals("0"))
				CalculatorMain.getResultField().setText("Error");
			else
			{
				CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() / 
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				CalculatorMain.setTempOperand(Double.parseDouble
						(CalculatorMain.getResultField().getText()));
			}
		}
	}
	
	private void multiplication()
	{
		CalculatorMain.setBinOperatorClicked(true);
		
		if(CalculatorMain.getLastBinOperator().equals(""))
		{
			CalculatorMain.setTempOperand(Double.parseDouble(CalculatorMain.getResultField().getText()));
			CalculatorMain.setLastBinOperator("\u00d7");
		}
		else if(!CalculatorMain.getLastBinOperator().equals(""))
		{
			performLastOperation();
			CalculatorMain.setLastBinOperator("\u00d7");
		}
		else
		{
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
					(CalculatorMain.getTempOperand() *
							Double.parseDouble(CalculatorMain.getResultField().getText())));
			CalculatorMain.setTempOperand(Double.parseDouble
					(CalculatorMain.getResultField().getText()));
		}
	}
	
	private void subtraction()
	{
		CalculatorMain.setBinOperatorClicked(true);
		
		if(CalculatorMain.getLastBinOperator().equals(""))
		{
			CalculatorMain.setTempOperand(Double.parseDouble(CalculatorMain.getResultField().getText()));
			CalculatorMain.setLastBinOperator("-");
		}
		else if(!CalculatorMain.getLastBinOperator().equals(""))
		{
			performLastOperation();
			CalculatorMain.setLastBinOperator("-");
		}
		else
		{
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
						(CalculatorMain.getTempOperand() -
								Double.parseDouble(CalculatorMain.getResultField().getText())));
			CalculatorMain.setTempOperand(Double.parseDouble
					(CalculatorMain.getResultField().getText()));
		}
	}
	
	private void addition()
	{
		CalculatorMain.setBinOperatorClicked(true);
		
		if(CalculatorMain.getLastBinOperator().equals(""))
		{
			CalculatorMain.setTempOperand(Double.parseDouble(CalculatorMain.getResultField().getText()));
			CalculatorMain.setLastBinOperator("+");
		}
		else if(!CalculatorMain.getLastBinOperator().equals(""))
		{
			performLastOperation();
			CalculatorMain.setLastBinOperator("+");
		}
		else
		{
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
						(CalculatorMain.getTempOperand() +
								Double.parseDouble(CalculatorMain.getResultField().getText())));
			CalculatorMain.setTempOperand(Double.parseDouble
					(CalculatorMain.getResultField().getText()));
		}
	}
	
	private void performLastOperation()
	{
		//if any previous operator exists
		switch(CalculatorMain.getLastBinOperator())
		{
			case "\u00f7":
				CalculatorMain.getResultField().setText
					(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() /
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				CalculatorMain.setTempOperand(Double.parseDouble
						(CalculatorMain.getResultField().getText()));
				break;
			case "\u00d7":
				CalculatorMain.getResultField().setText
					(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() *
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				CalculatorMain.setTempOperand(Double.parseDouble
						(CalculatorMain.getResultField().getText()));
				break;
			case "-":
				CalculatorMain.getResultField().setText
					(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() -
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				CalculatorMain.setTempOperand(Double.parseDouble
						(CalculatorMain.getResultField().getText()));
				break;
			case "+":
				CalculatorMain.getResultField().setText
					(CalculatorMain.DECIMAL_FORMAT.format
							(CalculatorMain.getTempOperand() +
									Double.parseDouble(CalculatorMain.getResultField().getText())));
				CalculatorMain.setTempOperand(Double.parseDouble
						(CalculatorMain.getResultField().getText()));
				break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(this.getText())
		{
			case "\u00f7":
				division();
				break;
			case "\u00d7":
				multiplication();
				break;
			case "-":
				subtraction();
				break;
			case "+":
				addition();
				break;
		}
		
		//removing trailing zeros
		if(CalculatorMain.getResultField().getText().endsWith(".0"))
			CalculatorMain.getResultField().setText
					(CalculatorMain.getResultField().getText().replace(".0", ""));
	}
}