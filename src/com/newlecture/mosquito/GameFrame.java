package com.newlecture.mosquito;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import com.newlecture.mosquito.canvas.FreeCanvas;
import com.newlecture.mosquito.canvas.MenuCanvas;
import com.newlecture.mosquito.canvas.RankCanvas;
import com.newlecture.mosquito.canvas.StageCanvas;
import com.newlecture.mosquito.gui.PlayerHpBar;
import com.newlecture.mosquito.service.DataService;

public class GameFrame extends Frame {
	
	private static GameFrame instance;
	
	public static final int STAGE_MENU = 1001;
	public static final int FREE_MENU = 1002;
	public static final int EXIT_MENU = 1003;
	
	public static int canvasWidth = 1500;
	public static int canvasHeight = 1000;
	public String userName;

	public GameFrame() {
		instance = this;
		MenuCanvas menuCanvas = new MenuCanvas();
		
		//add(menuCanvas);
		menuCanvas.start();
		
		this.setSize(canvasWidth, canvasHeight);
		this.setVisible(true);

		this.add(menuCanvas);
		
		// close 코드 
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

	}

	// 메뉴가 바뀌었을때 호출됨/
	public void switchCanvas(Canvas oldCanvas, Class newCanvas, boolean checkId) throws InstantiationException, IllegalAccessException{
		boolean change = true;
		if(oldCanvas instanceof MenuCanvas && checkId) {
			
			String id="";
			while(true) {
				
				id = JOptionPane.showInputDialog(null, "사용자 아이디를 입럭하시오.", "USER ID", TEXT_CURSOR);
				
				try {
					if(DataService.getInstance().checkId(id) == false
							&& newCanvas.getName().equals("com.newlecture.mosquito.canvas.FreeCanvas")) {
						JOptionPane.showMessageDialog(null, "저장되지 않은 유저 정보입니다. 해당 아이디로 새로운 계정을 생성하였습니다. 다시 접속해주세요.", "Warning",
						        JOptionPane.WARNING_MESSAGE);
						change = false;
						break;
					}
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(id == null) {
					System.out.println(id);
					change = false;
					id="";
				}
				
				if((id.length() > 8 || id.length() < 2) && id.length()!=0)
					JOptionPane.showMessageDialog(null, "2자리 이상, 8자리 이하의 이름을 입력하시오.", "Warning",
					        JOptionPane.WARNING_MESSAGE);
				else
					break;
			}
			
			
			
			userName = id;
		}
		
		System.out.println(newCanvas.getName().equals("com.newlecture.mosquito.canvas.FreeCanvas"));
		
		if(change) {
			Canvas canvas = (Canvas)newCanvas.newInstance();
			add(canvas);
			if(canvas instanceof StageCanvas) {
				StageCanvas stageCanvas = (StageCanvas) canvas;
				stageCanvas.start();
			} else if(canvas instanceof FreeCanvas) {
				FreeCanvas freeCanvas = (FreeCanvas) canvas;
				freeCanvas.start();
				
			} else if(canvas instanceof MenuCanvas) {			
				MenuCanvas menuCanvas = (MenuCanvas) canvas;
				menuCanvas.start();
			} else if(canvas instanceof RankCanvas) {			
				RankCanvas rankCanvas = (RankCanvas) canvas;
				rankCanvas.start();
			}
			revalidate();//재활성화(다시 유효하게 만든다)
			remove(oldCanvas);
		}
		
	}
	//instance 변수 / static 변수
	public static GameFrame getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
