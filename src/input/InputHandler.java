package input;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.MouseInputListener;

public class InputHandler implements KeyListener, MouseInputListener, MouseWheelListener {
	
	private boolean mouseIn = true;
	private int mouseX = 0;
	private int mouseY = 0;
	private int mouseWheelPosition = 0;
	private HashMap<String, Input> inputs = new HashMap<String, Input>();
	private boolean inRebind = false;
	private String toBeRebound = "";
	private ArrayList<InputEvent> tempInputs = new ArrayList<InputEvent>();
	private ArrayList<InputEvent> heldKeys = new ArrayList<InputEvent>();
	
	public InputHandler(Component c) {
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
	}
	
	public void createInput(String name) {
		if (!inputs.keySet().contains(name))
			inputs.put(name, new Input(new int[] {}));
		else
			System.out.println("INP ERROR: Tried to add duplicate input '" + name + "'");
	}
	
	public void createInput(String name, int[] keys) {
		if (!inputs.keySet().contains(name))
			inputs.put(name, new Input(keys));
		else
			System.out.println("INP ERROR: Tried to add duplicate input '" + name + "'");
	}
	
	public void removeInput(String name) {
		if (inputs.keySet().contains(name)) 
			inputs.remove(name);
		else
			System.out.println("INP ERROR: Tried to remove missing input '" + name + "'");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//unused
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!inRebind) {//normal conditions
			for (Input i : inputs.values()) {
				for (int j : i.getKeys()) {
					if (j == e.getButton()) {
						i.togglePressed(true);
					}
				}
			}	
		} else {//if rebinding an input
			boolean in = false;
			for (InputEvent i : tempInputs) {
				if (i instanceof MouseEvent &&((MouseEvent)i).getButton() == e.getButton()) {
					in = true;
				}
			}
			if (!in) {
				tempInputs.add(e);
				heldKeys.add(e);
			}
			if (tempInputs.isEmpty()) {
				tempInputs.add(e);
				heldKeys.add(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!inRebind) {//normal conditions
			for (Input i : inputs.values()) {
				for (int j : i.getKeys()) {
					if (j == e.getButton()) {
						i.togglePressed(false);
						i.toggleFresh(true);
					}
				}
			}
		} else {//if rebinding an input
			ArrayList<InputEvent> queue = new ArrayList<InputEvent>();
			for (InputEvent h: heldKeys) {
				if (h instanceof MouseEvent && ((MouseEvent)h).getButton() == e.getButton()) queue.add(h);
			}
			for (InputEvent q : queue) {
				heldKeys.remove(q);
			}
			queue.clear();
			if (heldKeys.isEmpty() && !tempInputs.isEmpty()) {
				InputEvent temp[] = new InputEvent[tempInputs.size()];
				tempInputs.toArray(temp);
				int temp1[] = new int[temp.length];
				for (int i = 0; i < temp.length; i ++) {
					if(temp[i] instanceof KeyEvent)
						temp1[i] = ((KeyEvent)temp[i]).getKeyCode();
					else
						temp1[i] = ((MouseEvent)temp[i]).getButton();
				}
				inputs.get(toBeRebound).setKeys(temp1);
				inRebind = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseIn = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelPosition += e.getWheelRotation();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// unused
	}

	@Override
	public void keyPressed(KeyEvent e) {	
		if (!inRebind) {//normal conditions
			for (Input i : inputs.values()) {
				for (int j : i.getKeys()) {
					if (j == e.getKeyCode()) {
						i.togglePressed(true);
					}
				}
			}	
		} else {//if rebinding an input
			boolean in = false;
			for (InputEvent i : tempInputs) {
				if (i instanceof KeyEvent &&((KeyEvent)i).getKeyCode() == e.getKeyCode()) {
					in = true;
				}
			}
			if (!in) {
				tempInputs.add(e);
				heldKeys.add(e);
			}
			if (tempInputs.isEmpty()) {
				tempInputs.add(e);
				heldKeys.add(e);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!inRebind) {//normal conditions
			for (Input i : inputs.values()) {
				for (int j : i.getKeys()) {
					if (j == e.getKeyCode()) {
						i.togglePressed(false);
						i.toggleFresh(true);
					}
				}
			}
		} else {//if rebinding an input
			ArrayList<InputEvent> queue = new ArrayList<InputEvent>();
			for (InputEvent h: heldKeys) {
				if (h instanceof KeyEvent && ((KeyEvent)h).getKeyCode() == e.getKeyCode()) queue.add(h);
			}
			for (InputEvent q : queue) {
				heldKeys.remove(q);
			}
			queue.clear();
			if (heldKeys.isEmpty() && !tempInputs.isEmpty()) {
				InputEvent temp[] = new InputEvent[tempInputs.size()];
				tempInputs.toArray(temp);
				int temp1[] = new int[temp.length];
				for (int i = 0; i < temp.length; i ++) {
					if(temp[i] instanceof KeyEvent)
						temp1[i] = ((KeyEvent)temp[i]).getKeyCode();
					else
						temp1[i] = ((MouseEvent)temp[i]).getButton();
				}
				inputs.get(toBeRebound).setKeys(temp1);
				inRebind = false;
			}
		}
	}
	
	public boolean getMouseIn() {
		return mouseIn;
	}
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	public int getMouseWheelPosition() {
		return mouseWheelPosition;
	}
	
	public HashMap<String,Input> getInputs(){
		return inputs;
	}
	
	public boolean getPressed(String input) {
		if (inputs.containsKey(input)) {
			return inputs.get(input).isPressed();
		} else
			System.out.println("INP ERROR: Tried to access missing input '" + input + "'");
		return false;
	}
	
	public boolean getFresh(String input) {
		if (inputs.containsKey(input)) {
			return inputs.get(input).isFresh();
		} else
			System.out.println("INP ERROR: Tried to access missing input '" + input + "'");
		return false;
	}
	
	public void setFresh(String input, boolean toggle) {
		if (inputs.containsKey(input)) {
			inputs.get(input).toggleFresh(toggle);
		} else
			System.out.println("INP ERROR: Tried to access missing input '" + input + "'");
	}
	
	public String tempToString() {
		String string = "";
		boolean first = true;
		for (InputEvent e : tempInputs) {
			if (!first) string += ", ";
			else first = false;
			if (e instanceof KeyEvent)
				string += KeyEvent.getKeyText(((KeyEvent)e).getKeyCode());
			else 
				string += ((MouseEvent)e).getButton();
		}
		return string;
	}
	
	public void startRebind(String input) {
		if (inputs.containsKey(input)) {
			toBeRebound = input;
			inRebind = true;
			tempInputs.clear();
			heldKeys.clear();
		} else
			System.out.println("INP ERROR: Tried to rebind missing input '" + input + "'");
		
	}
	
	public boolean getInRebind() {
		return inRebind;
	}
	
	public String keyString(String input) {
		if (inputs.containsKey(input))
			return inputs.get(input).toString();
		else {
			System.out.println("INP ERROR: Tried to access keystring of missing input '" + input + "'");
			return "";
		}
	}

	public class Input {
		private boolean pressed = false;
		private boolean fresh = true;
		private int[] keys;
		
		public Input(int[] keys) {
			this.keys = keys;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
		public boolean isFresh() {
			return fresh;
		}
		
		public void togglePressed(boolean pressed) {
			this.pressed = pressed;
		}
		
		public void toggleFresh(boolean fresh) {
			this.fresh = fresh;
		}
		
		public int[] getKeys() {
			return keys;
		}
		
		public void setKeys(int[] keys) {
			this.keys = keys;
		}
		
		public String toString() {
			String string = "";
			boolean first = true;
			for (int k : keys) {
				if (!first)
					string += ", ";
				else
					first = false;
				string += KeyEvent.getKeyText(k).replace("Unknown keyCode: ", "");
			}
			return string;
		}
	}
	
}

