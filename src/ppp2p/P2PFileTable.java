package ppp2p;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;



/**
 */
public abstract class P2PFileTable extends JTable implements MouseListener {
	private Vector headerVect = new Vector();

	private JTableHeader header;

	private SortButtonRenderer renderer = new SortButtonRenderer();
	
	private SortableTableModel model;

	/**
	 *
	 */
	public P2PFileTable() {
		super();

		// set HeaderRender
		header = getTableHeader();
		header.setDefaultRenderer(renderer);
		header.addMouseListener(this);

		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setColumnSelectionAllowed(false);
		this.setDragEnabled(false);
		this.setAutoscrolls(true);
	}

	/**
	 *
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public abstract boolean isDirectory(int row);

	public void setFileList(Vector data) {
		model.setDataVector(data, headerVect);
	}
	
	public void setModel(SortableTableModel tm){
		super.setModel(tm);
		this.model = tm;
	}
	public FileInformation getFile(int row) {
		FileInformation fi = new FileInformation(
				(String) model.getValueAt(row, 5), (String) model.getValueAt(row, 0),
				(Long) model.getValueAt(row, 1), (String) model.getValueAt(row, 2),
				(Date) model.getValueAt(row, 3), (String) model.getValueAt(row, 4),(String)model.getValueAt(row, 6));
		return fi;
	}

	/**
	 * @param headerStr The headerStr to set.
	 */
	public void setHeaderVect(String[] headerStr) {
		int length = headerStr.length;

		for (int i = 0; i < length; i++) {
			headerVect.add(headerStr[i]);
		}
	}

	public int getHeaderSize() {
		return headerVect.size();
	}

	public void mousePressed(MouseEvent e) {
		int col = header.columnAtPoint(e.getPoint());
		int sortCol = header.getTable().convertColumnIndexToModel(col);
		renderer.setPressedColumn(col);
		renderer.setSelectedColumn(col);
		header.repaint();

		if (header.getTable().isEditing()) {
			header.getTable().getCellEditor().stopCellEditing();
		}

		boolean isAscent;
		if (SortButtonRenderer.DOWN == renderer.getState(col)) {
			isAscent = true;
		} else {
			isAscent = false;
		}
		((SortableTableModel) header.getTable().getModel()).sortByColumn(
				sortCol, isAscent);
	}

	public void mouseReleased(MouseEvent e) {
		renderer.setPressedColumn(-1); // clear
		header.repaint();

	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}


}

class SortButtonRenderer extends JButton implements TableCellRenderer {
	public static final int NONE = 0;

	public static final int DOWN = 1;

	public static final int UP = 2;

	int pushedColumn;

	Hashtable state;

	JButton downButton, upButton;

	public SortButtonRenderer() {
		pushedColumn = -1;
		state = new Hashtable();

		setMargin(new Insets(0, 0, 0, 0));
		setHorizontalTextPosition(LEFT);
		setIcon(new BlankIcon());
		
		downButton = new JButton();
		downButton.setMargin(new Insets(0, 0, 0, 0));
		downButton.setHorizontalTextPosition(LEFT);
		downButton
				.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, false));
		downButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN,
				false, true));

		upButton = new JButton();
		upButton.setMargin(new Insets(0, 0, 0, 0));
		upButton.setHorizontalTextPosition(LEFT);
		upButton.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
		upButton.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP, false,
				true));

	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JButton button = this;
		Object obj = state.get(new Integer(column));
		if (obj != null) {
			if (((Integer) obj).intValue() == DOWN) {
				button = downButton;
			} else {
				button = upButton;
			}
		}
		button.setText((value == null) ? "" : value.toString());
		boolean isPressed = (column == pushedColumn);
		button.getModel().setPressed(isPressed);
		button.getModel().setArmed(isPressed);
		return button;
	}

	public void setPressedColumn(int col) {
		pushedColumn = col;
	}

	public void setSelectedColumn(int col) {
		if (col < 0)
			return;
		Integer value = null;
		Object obj = state.get(new Integer(col));
		if (obj == null) {
			value = new Integer(DOWN);
		} else {
			if (((Integer) obj).intValue() == DOWN) {
				value = new Integer(UP);
			} else {
				value = new Integer(DOWN);
			}
		}
		state.clear();
		state.put(new Integer(col), value);
	}

	public int getState(int col) {
		int retValue;
		Object obj = state.get(new Integer(col));
		if (obj == null) {
			retValue = NONE;
		} else {
			if (((Integer) obj).intValue() == DOWN) {
				retValue = DOWN;
			} else {
				retValue = UP;
			}
		}
		return retValue;
	}

}

class BlankIcon implements Icon {
	private Color fillColor;

	private int size;

	public BlankIcon() {
		this(null, 11);
	}

	public BlankIcon(Color color, int size) {
	
		fillColor = color;

		this.size = size;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (fillColor != null) {
			g.setColor(fillColor);
			g.drawRect(x, y, size - 1, size - 1);
		}
	}

	public int getIconWidth() {
		return size;
	}

	public int getIconHeight() {
		return size;
	}
}

class BevelArrowIcon implements Icon {
	public static final int UP = 0; // direction

	public static final int DOWN = 1;

	private static final int DEFAULT_SIZE = 11;

	private Color edge1;

	private Color edge2;

	private Color fill;

	private int size;

	private int direction;

	public BevelArrowIcon(int direction, boolean isRaisedView,
			boolean isPressedView) {
		if (isRaisedView) {
			if (isPressedView) {
				init(UIManager.getColor("controlLtHighlight"), UIManager
						.getColor("controlDkShadow"), UIManager
						.getColor("controlShadow"), DEFAULT_SIZE, direction);
			} else {
				init(UIManager.getColor("controlHighlight"), UIManager
						.getColor("controlShadow"), UIManager
						.getColor("control"), DEFAULT_SIZE, direction);
			}
		} else {
			if (isPressedView) {
				init(UIManager.getColor("controlDkShadow"), UIManager
						.getColor("controlLtHighlight"), UIManager
						.getColor("controlShadow"), DEFAULT_SIZE, direction);
			} else {
				init(UIManager.getColor("controlShadow"), UIManager
						.getColor("controlHighlight"), UIManager
						.getColor("control"), DEFAULT_SIZE, direction);
			}
		}
	}

	public BevelArrowIcon(Color edge1, Color edge2, Color fill, int size,
			int direction) {
		init(edge1, edge2, fill, size, direction);
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		switch (direction) {
		case DOWN:
			drawDownArrow(g, x, y);
			break;
		case UP:
			drawUpArrow(g, x, y);
			break;
		}
	}

	public int getIconWidth() {
		return size;
	}

	public int getIconHeight() {
		return size;
	}

	private void init(Color edge1, Color edge2, Color fill, int size,
			int direction) {
		this.edge1 = edge1;
		this.edge2 = edge2;
		this.fill = fill;
		this.size = size;
		this.direction = direction;
	}

	private void drawDownArrow(Graphics g, int xo, int yo) {
		g.setColor(edge1);
		g.drawLine(xo, yo, xo + size - 1, yo);
		g.drawLine(xo, yo + 1, xo + size - 3, yo + 1);
		g.setColor(edge2);
		g.drawLine(xo + size - 2, yo + 1, xo + size - 1, yo + 1);
		int x = xo + 1;
		int y = yo + 2;
		int dx = size - 6;
		while (y + 1 < yo + size) {
			g.setColor(edge1);
			g.drawLine(x, y, x + 1, y);
			g.drawLine(x, y + 1, x + 1, y + 1);
			if (0 < dx) {
				g.setColor(fill);
				g.drawLine(x + 2, y, x + 1 + dx, y);
				g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
			}
			g.setColor(edge2);
			g.drawLine(x + dx + 2, y, x + dx + 3, y);
			g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
			x += 1;
			y += 2;
			dx -= 2;
		}
		g.setColor(edge1);
		g.drawLine(xo + (size / 2), yo + size - 1, xo + (size / 2), yo + size
				- 1);
	}

	private void drawUpArrow(Graphics g, int xo, int yo) {
		g.setColor(edge1);
		int x = xo + (size / 2);
		g.drawLine(x, yo, x, yo);
		x--;
		int y = yo + 1;
		int dx = 0;
		while (y + 3 < yo + size) {
			g.setColor(edge1);
			g.drawLine(x, y, x + 1, y);
			g.drawLine(x, y + 1, x + 1, y + 1);
			if (0 < dx) {
				g.setColor(fill);
				g.drawLine(x + 2, y, x + 1 + dx, y);
				g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
			}
			g.setColor(edge2);
			g.drawLine(x + dx + 2, y, x + dx + 3, y);
			g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
			x -= 1;
			y += 2;
			dx += 2;
		}
		g.setColor(edge1);
		g.drawLine(xo, yo + size - 3, xo + 1, yo + size - 3);
		g.setColor(edge2);
		g.drawLine(xo + 2, yo + size - 2, xo + size - 1, yo + size - 2);
		g.drawLine(xo, yo + size - 1, xo + size, yo + size - 1);
	}
}