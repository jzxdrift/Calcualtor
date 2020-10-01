import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CalculatorMain extends JFrame implements ClipboardOwner
{
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat
		("#.######", DecimalFormatSymbols.getInstance(Locale.US));

	private static final int BUTTONS_GRID_ROWS = 6;
	private static final int BUTTONS_GRID_COLS = 5;
	private static final int SPACING = 5;

	private static final int DIG_BUTTONS = 11;
	private static final int BIN_BUTTONS = 4;
	private static final int UN_BUTTONS = 6;
	private static final int MEM_BUTTONS = 5;
	private static final int GEN_BUTTONS = 4;

	private static final String[] BIN_OPERATORS = {"\u00f7", "\u00d7", "-", "+"};
	private static final String[] UN_OPERATORS = {"%", "\u221a", "x\u00b2", "x\u00b3",
		"\u00b9\u2044\u2093", "\u00b1"};
	private static final String[] MEM_OPERATORS = {"MC", "MR", "MS", "M+", "M-"};
	private static final String[] GEN_OPERATORS = {"CE", "C", "\u25c4", "="};
	
	private static final KeyStroke[] BIN_KEYS =
	{
		KeyStroke.getKeyStroke('/'),
		KeyStroke.getKeyStroke('*'),
		KeyStroke.getKeyStroke('-'),
		KeyStroke.getKeyStroke('+')
	};
	private static final KeyStroke[] UN_KEYS =
	{
		KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.SHIFT_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.SHIFT_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),
		KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.SHIFT_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
		KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0)
	};
	private static final KeyStroke[] MEM_KEYS =
	{
		KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK),
		KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK)
	};
	private static final KeyStroke[] GEN_KEYS =
	{
		KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
		KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
		KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
	};

	private BorderLayout topBorder;
	private JPanel topPanel;
	private GridLayout buttonsGrid;
	private JPanel buttonsPanel;

	//static variables to use by all classes
	private static JLabel memoryLabel;
	private static JTextField resultField;
	private static String lastBinOperator;
	private static boolean binOperatorClicked;
	private static boolean unOperatorClicked;
	private static boolean memOperatorClicked;
	private static boolean resultOperatorClicked;
	private static double tempOperand;
	private static double percentValue;
	private static double memoryValue;

	private DigitButton[] digButtons;
	private BinaryOperationButton[] binButtons;
	private UnaryOperationButton[] unButtons;
	private MemoryOperationButton[] memButtons;
	private GeneralOperationButton[] genButtons;
	
	//clip board variables
	private StringSelection copyString;
	private Clipboard clipboard;
	private String pasteString;
	private Transferable contents;
	private boolean hasTransferableText;

	public CalculatorMain()
	{
		super("Calculator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());

		topBorder = new BorderLayout();
		topPanel = new JPanel(topBorder);
		topPanel.setBackground(Color.WHITE);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		//clip board copy function
		topPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "copy");
		topPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.CTRL_DOWN_MASK), "copy");
		topPanel.getActionMap().put("copy", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setClipboardContents(resultField.getText());
			}
		});
		//clip board paste function
		topPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
			(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "paste");
		topPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
		(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.SHIFT_DOWN_MASK), "paste");
		topPanel.getActionMap().put("paste", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				resultField.setText(getClipboardContents());
			}
		});

		memoryLabel = new JLabel("");
		memoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
		memoryLabel.setForeground(Color.RED);
		memoryLabel.setVerticalAlignment(SwingConstants.TOP);

		resultField = new JTextField("0");
		resultField.setFont(new Font("Arial", Font.PLAIN, 36));
		resultField.setBackground(Color.WHITE);
		resultField.setDisabledTextColor(Color.BLACK);
		resultField.setBorder(null);
		resultField.setEditable(false);
		resultField.setEnabled(false);
		resultField.setHorizontalAlignment(SwingConstants.RIGHT);
		
		topPanel.add(memoryLabel, BorderLayout.WEST);
		topPanel.add(resultField);

		buttonsGrid = new GridLayout(BUTTONS_GRID_ROWS, BUTTONS_GRID_COLS, SPACING, SPACING);
		buttonsPanel = new JPanel(buttonsGrid);
		this.getContentPane().add(buttonsPanel, BorderLayout.CENTER);

		digButtons = new DigitButton[DIG_BUTTONS];
		for(int i = 0; i < DIG_BUTTONS; i++)
		{
			if(i == DIG_BUTTONS - 1)
			{
				digButtons[i] = new DigitButton(".");
				break;
			}
			digButtons[i] = new DigitButton(Integer.toString(i));
		}

		binButtons = new BinaryOperationButton[BIN_BUTTONS];
		for(int i = 0; i < BIN_BUTTONS; i++)
			binButtons[i] = new BinaryOperationButton(BIN_OPERATORS[i], BIN_KEYS[i]);

		unButtons = new UnaryOperationButton[UN_BUTTONS];
		for(int i = 0; i < UN_BUTTONS; i++)
			unButtons[i] = new UnaryOperationButton(UN_OPERATORS[i], UN_KEYS[i]);

		memButtons = new MemoryOperationButton[MEM_BUTTONS];
		for(int i = 0; i < MEM_BUTTONS; i++)
			memButtons[i] = new MemoryOperationButton(MEM_OPERATORS[i], MEM_KEYS[i]);

		genButtons = new GeneralOperationButton[GEN_BUTTONS];
		for(int i = 0; i < GEN_BUTTONS; i++)
			genButtons[i] = new GeneralOperationButton(GEN_OPERATORS[i], GEN_KEYS[i]);

		//adding buttons to panel in specific order
		buttonsPanel.add(memButtons[0]);
		buttonsPanel.add(memButtons[1]);
		buttonsPanel.add(memButtons[2]);
		buttonsPanel.add(memButtons[3]);
		buttonsPanel.add(memButtons[4]);
		buttonsPanel.add(unButtons[0]);
		buttonsPanel.add(genButtons[0]);
		buttonsPanel.add(genButtons[1]);
		buttonsPanel.add(genButtons[2]);
		buttonsPanel.add(binButtons[0]);
		buttonsPanel.add(unButtons[1]);
		buttonsPanel.add(digButtons[7]);
		buttonsPanel.add(digButtons[8]);
		buttonsPanel.add(digButtons[9]);
		buttonsPanel.add(binButtons[1]);
		buttonsPanel.add(unButtons[2]);
		buttonsPanel.add(digButtons[4]);
		buttonsPanel.add(digButtons[5]);
		buttonsPanel.add(digButtons[6]);
		buttonsPanel.add(binButtons[2]);
		buttonsPanel.add(unButtons[3]);
		buttonsPanel.add(digButtons[1]);
		buttonsPanel.add(digButtons[2]);
		buttonsPanel.add(digButtons[3]);
		buttonsPanel.add(binButtons[3]);
		buttonsPanel.add(unButtons[4]);
		buttonsPanel.add(unButtons[5]);
		buttonsPanel.add(digButtons[0]);
		buttonsPanel.add(digButtons[10]);
		buttonsPanel.add(genButtons[3]);

		//initializing static variables
		lastBinOperator = "";
		binOperatorClicked = false;
		unOperatorClicked = false;
		memOperatorClicked = false;
		resultOperatorClicked = false;
		tempOperand = 0.0;
		percentValue = 0.0;
		memoryValue = 0.0;
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		pasteString = "";
		
		this.pack();
	}

	public static JLabel getMemoryLabel()
	{
		return memoryLabel;
	}

	public static JTextField getResultField()
	{
		return resultField;
	}

	public static String getLastBinOperator()
	{
		return lastBinOperator;
	}

	public static void setLastBinOperator(String lastBinOperator)
	{
		CalculatorMain.lastBinOperator = lastBinOperator;
	}

	public static boolean isBinOperatorClicked()
	{
		return binOperatorClicked;
	}

	public static void setBinOperatorClicked(boolean binOperatorClicked)
	{
		CalculatorMain.binOperatorClicked = binOperatorClicked;
	}
	
	public static boolean isUnOperatorClicked()
	{
		return unOperatorClicked;
	}

	public static void setUnOperatorClicked(boolean unOperatorClicked)
	{
		CalculatorMain.unOperatorClicked = unOperatorClicked;
	}

	public static boolean isMemOperatorClicked()
	{
		return memOperatorClicked;
	}

	public static void setMemOperatorClicked(boolean memOperatorClicked)
	{
		CalculatorMain.memOperatorClicked = memOperatorClicked;
	}

	public static boolean isResultOperatorClicked()
	{
		return resultOperatorClicked;
	}

	public static void setResultOperatorClicked(boolean resultOperatorClicked)
	{
		CalculatorMain.resultOperatorClicked = resultOperatorClicked;
	}

	public static double getTempOperand()
	{
		return tempOperand;
	}

	public static void setTempOperand(double tempOperand)
	{
		CalculatorMain.tempOperand = tempOperand;
	}

	public static double getPercentValue()
	{
		return percentValue;
	}

	public static void setPercentValue(double percentValue)
	{
		CalculatorMain.percentValue = percentValue;
	}

	public static double getMemoryValue()
	{
		return memoryValue;
	}

	public static void setMemoryValue(double memoryValue)
	{
		CalculatorMain.memoryValue = memoryValue;
	}
	
	public void setClipboardContents(String copyString)
	{
		this.copyString = new StringSelection(copyString);
	        clipboard.setContents(this.copyString, null);
	}
	
	public String getClipboardContents()
	{
		contents = clipboard.getContents(null);
		hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		
		if(hasTransferableText)
		{
			try
			{
				pasteString = (String)contents.getTransferData(DataFlavor.stringFlavor);
			}
			catch(UnsupportedFlavorException | IOException ex)
			{
				
			}
		}
		return pasteString;
	}
	
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				CalculatorMain calculator = new CalculatorMain();	//initialize components
				calculator.setResizable(false);
				calculator.setVisible(true);
			}
		});
	}
}
