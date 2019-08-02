package cs120.bdevaughn.texascounties;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * This class will display the county chooser game. The user will be able to click on a county and then p
 * -ick from a group of 5 counties to match the right name of the county. There will be a whole map of the 
 * counties displayed. Counties correctly picked will turn gray indicating that its already be picked
 * @author Bryce DeVaughn
 *
 */
public class MainFrame extends JFrame {
	private Random rand;
	private static final int MAXY = 1000;
	private static final int MAXX = 1000;
	private ArrayList<JButton> buttons;//List holding the buttons for the button panel
	private QuizManager qm;//Manages the "game"
	private TexasPanel tPanel;//Draws the counties and regions
	private InstructionPanel iPanel;//Displays the intructions to the user
	public MainFrame() throws HeadlessException{
		setVisible(true);
		setMaximumSize(new Dimension(MAXX, MAXY));
		setMinimumSize(new Dimension(MAXX, MAXY));
		setFocusable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		rand = new Random();
		buttons = new ArrayList<JButton>();
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		/*
		 * Creating the QuizManager based off the width and height of the TexasPanel.
		 */
		try {
			qm = new QuizManager();
			qm.readCounties("co48_d00a.txt");
			qm.readCoordinates("co48_d00.txt");
			qm.setBoundaries();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Creating and adding the TexasPanel to the frame.
		 */
		try {
			tPanel = new TexasPanel(qm);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mainPanel.add(tPanel,BorderLayout.CENTER);
		
		iPanel = new InstructionPanel();
		mainPanel.add(iPanel,BorderLayout.NORTH);
		
		/*
		 * Creating the button panel
		 */
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.CENTER);
		for(int i = 0; i < 5; i++){
			JButton btn = new JButton("X");
			btn.setVisible(false);
			btn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onBtnPress(arg0);
				}
			});
			buttons.add(btn);
			buttonPanel.add(btn);
		
		}
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	/**
	 * This method will be called when a button is pressed.
	 * @param args
	 */
	public void onBtnPress(ActionEvent e){
		String cName = "";
		for(JButton btn: buttons){
			if(btn == e.getSource()){
			cName = btn.getText();
			}
		}
		
		if(cName.equals(qm.getSelectedCounty().getName())){
			qm.getSelectedCounty().setActive(false);
			int rId = 0;
			do{
				rId = rand.nextInt(qm.getCounties().size());
			}while(rId==qm.getSelectedCounty().getPID() && qm.getCounties().get(rId).isActive()==false);
			
			qm.setSelectedCounty(qm.findCountyById(rId));
			setupNewQuiz();
		}
		
		
		
	}
	
	/**
	 * Sets up a new quiz.
	 */
	public void setupNewQuiz(){
		iPanel.setLabel("Which county is this?");
		setButtons();
	}
	
	/**
	 * Sets the buttons to the randomly selected counties.
	 * @param args
	 */
	public void setButtons(){
		County[] newCounties = qm.randomCounty(4, qm.getSelectedCounty().getPID());
		int randomIndex = rand.nextInt(5);
		int e = 0;//This will keep track of which counties name has been applied to a button.
		for(int i = 0; i < buttons.size(); i++){
			if(i == randomIndex){
				JButton btn = buttons.get(randomIndex);
				btn.setText(qm.getSelectedCounty().getName());
				btn.setVisible(true);
			}else{
				JButton btn = buttons.get(i);
				btn.setText(newCounties[e].getName());
				btn.setVisible(true);
				e++;
			}
		}
		repaint();
	}
	
	/**
	 * Hides all of the buttons.
	 * @param args
	 */
	public void hideButtons(){
		for(JButton btn: buttons){
			btn.setVisible(false);
		}
	}
	
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();

	}
	
	/**
	 * This class will act as the display panel.
	 * @author Bryce DeVaughn
	 *
	 */
	private class TexasPanel extends JPanel implements MouseListener{
		
		private QuizManager qm;

		public TexasPanel(QuizManager qm) throws IOException {
			this.qm = qm;
			this.addMouseListener(this);
		}
		
		
		
		@Override
		public void paint(Graphics gfx){
			gfx.setColor(Color.BLACK);
			gfx.fillRect(0, 0, MAXX, MAXY);
			this.qm.setDimension(this.getWidth(),this.getHeight());
			if(this.qm.getCounties().get(0).getPoly().npoints==0){
				this.qm.makePolygons();
			}
			this.qm.drawCounties(gfx);
			
			for(Region r: this.qm.getExclusionZones()){
				r.drawOn(gfx);
			}
			
			/*
			 * Drawing outline for the selected county.
			 */
			if(this.qm.getSelectedCounty()!=null){
				Graphics2D gfx2 = (Graphics2D) gfx;
				gfx2.setColor(Color.WHITE);
				BasicStroke stroke = new BasicStroke(3.0f);
				gfx2.setStroke(stroke);
				gfx2.drawPolygon(qm.getSelectedCounty().getPoly());
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			onMouseClick(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * This method will be called when the user clicks on a county.
		 */
		public void onMouseClick(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			County c = this.qm.findCounty(x, y);
			if(c!=null && c.isActive()){
				this.qm.setSelectedCounty(c);
				setupNewQuiz();
			}else{
				iPanel.setLabel("Please select a county.");
				hideButtons();
			}
		}

	}
	
	
	/**
	 * This panel will display instructions.
	 */
	private class InstructionPanel extends JPanel{
		private JLabel instruction;
		
		public InstructionPanel(){
			instruction = makeLabel("Please select a county.");
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(instruction);
			this.setBackground(Color.BLACK);
		}
		
		public JLabel makeLabel(String text){
			JLabel rtnLabel = new JLabel(text);
			rtnLabel.setFont(new Font("Arial",Font.BOLD,20));
			rtnLabel.setForeground(Color.WHITE);
			return rtnLabel;
		}
		
		public void setLabel(String newText){
			instruction.setText(newText);
			repaint();
		}
	}

}
