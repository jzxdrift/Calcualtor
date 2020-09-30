import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class MemoryOperationButton extends JButton implements ActionListener
{
	public MemoryOperationButton(String memOperator, KeyStroke key)
	{
		this.setText(memOperator);
		this.setFont(new Font("Arial", Font.PLAIN, 16));
		this.setFocusable(false);
		this.setPreferredSize(new Dimension(60, 60));
		this.addActionListener(this);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(key, "memOperator");
		this.getActionMap().put("memOperator", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doClick();
			}
		});
	}
	
	private void memoryClear()
	{
		CalculatorMain.setMemOperatorClicked(false);
		CalculatorMain.setMemoryValue(0);
	}
	
	private void memoryRecall()
	{
		CalculatorMain.setMemOperatorClicked(true);
		CalculatorMain.getResultField().setText(CalculatorMain.DECIMAL_FORMAT.format
				(CalculatorMain.getMemoryValue()));
		
		if(CalculatorMain.getResultField().getText().endsWith(".0"))
			CalculatorMain.getResultField().setText
					(CalculatorMain.getResultField().getText().replace(".0", ""));
	}
	
	private void memorySet()
	{
		CalculatorMain.setMemOperatorClicked(true);
		CalculatorMain.setMemoryValue(Double.parseDouble(CalculatorMain.getResultField().getText()));
	}
	
	private void memoryAdd()
	{
		CalculatorMain.setMemOperatorClicked(true);
		CalculatorMain.setMemoryValue(CalculatorMain.getMemoryValue() +
				Double.parseDouble(CalculatorMain.getResultField().getText()));
	}
	
	private void memorySubtract()
	{
		CalculatorMain.setMemOperatorClicked(true);
		CalculatorMain.setMemoryValue(CalculatorMain.getMemoryValue() -
				Double.parseDouble(CalculatorMain.getResultField().getText()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch(this.getText())
		{
			case "MC":
				memoryClear();
				break;
			case "MR":
				memoryRecall();
				break;
			case "MS":
				memorySet();
				break;
			case "M+":
				memoryAdd();
				break;
			case "M-":
				memorySubtract();
				break;
		}
		
		//handling memory label
		if(CalculatorMain.getMemoryValue() != 0)
			CalculatorMain.getMemoryLabel().setText("M");
		else
			CalculatorMain.getMemoryLabel().setText("");
	}
}