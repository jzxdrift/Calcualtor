import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class UnaryOperationButton extends JButton implements ActionListener
{
	public UnaryOperationButton(String unOperator, KeyStroke key)
	{
		this.setText(unOperator);
		this.setFont(new Font("Arial", Font.PLAIN, 16));
		this.setFocusable(false);
		this.setPreferredSize(new Dimension(60, 60));
		this.addActionListener(this);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(key, "unOperator");
		this.getActionMap().put("unOperator", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doClick();
			}
		});
	}
	
	private void percent()
	{
		CalculatorMain.setUnOperatorClicked(true);
		//use display number as percentage
		if(CalculatorMain.getTempOperand() == 0)
			CalculatorMain.setPercentValue(Double.parseDouble
					(CalculatorMain.getResultField().getText()) / 100);
		//use display number as percentage from first operand
		else
			CalculatorMain.setPercentValue((CalculatorMain.getTempOperand() *
					Double.parseDouble(CalculatorMain.getResultField().getText()) / 100));
		
		CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
				(CalculatorMain.getPercentValue()));
	}
	
	private void squareRoot()
	{
		CalculatorMain.setUnOperatorClicked(true);
		if(Double.parseDouble(CalculatorMain.getResultField().getText()) < 0)
			CalculatorMain.getResultField().setText("Error");
		else
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
					(Math.sqrt(Double.parseDouble(CalculatorMain.getResultField().getText()))));
	}
	
	private void square()
	{
		CalculatorMain.setUnOperatorClicked(true);
		CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
				(Math.pow(Double.parseDouble(CalculatorMain.getResultField().getText()), 2)));
	}
	
	private void cube()
	{
		CalculatorMain.setUnOperatorClicked(true);
		CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
				(Math.pow(Double.parseDouble(CalculatorMain.getResultField().getText()), 3)));
	}
	
	private void inverse()
	{
		CalculatorMain.setUnOperatorClicked(true);
		if(CalculatorMain.getResultField().getText().equals("0"))
			return;
		else
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
					(1 / Double.parseDouble(CalculatorMain.getResultField().getText())));
	}
	
	private void changeSign()
	{
		if(CalculatorMain.getResultField().getText().equals("0"))
			return;
		else
			CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
					(Double.parseDouble(CalculatorMain.getResultField().getText()) * (-1)));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(this.getText())
		{
			case "%":
				percent();
				break;
			case "\u221a":
				squareRoot();
				break;
			case "x\u00b2":
				square();
				break;
			case "x\u00b3":
				cube();
				break;
			case "\u00b9\u2044\u2093":
				inverse();
				break;
			case "\u00b1":
				changeSign();
				break;
		}
		
		if(CalculatorMain.getResultField().getText().endsWith(".0"))
			CalculatorMain.getResultField().setText
					(CalculatorMain.getResultField().getText().replace(".0", ""));
	}
}