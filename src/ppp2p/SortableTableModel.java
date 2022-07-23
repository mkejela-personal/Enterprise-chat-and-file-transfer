package ppp2p;



import java.util.*;
import javax.swing.table.*;

/**
 * P2P File System
 */
public class SortableTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	int[] indexes;

	public SortableTableModel() {
		super();
	}

	public Object getValueAt(int row, int col) {
		int rowIndex = row;
		if (indexes != null) {
			rowIndex = indexes[row];
		}
		return super.getValueAt(rowIndex, col);
	}
	
	public void setDataVector(Vector data, Vector header){
		super.setDataVector(data, header);
		getIndexes();
	}

	public void setValueAt(Object value, int row, int col) {
		int rowIndex = row;
		if (indexes != null) {
			rowIndex = indexes[row];
		}
		super.setValueAt(value, rowIndex, col);
	}


	public void sortByColumn(int column, boolean isAscent) {
		sort(column, isAscent);
		fireTableDataChanged();
	}

	public int[] getIndexes() {
		int n = getRowCount();
		if (indexes != null) {
			if (indexes.length == n) {
				return indexes;
			}
		}
		indexes = new int[n];
		for (int i = 0; i < n; i++) {
			indexes[i] = i;
		}
		return indexes;
	}

	// sorter

	public void sort(int column, boolean isAscent) {
		int n = getRowCount();
		int[] indexes = getIndexes();

		for (int i = 0; i < n - 1; i++) {
			int k = i;
			for (int j = i + 1; j < n; j++) {
				if (isAscent) {
					if (compare(column, j, k) < 0) {
						k = j;
					}
				} else {
					if (compare(column, j, k) > 0) {
						k = j;
					}
				}
			}
			int tmp = indexes[i];
			indexes[i] = indexes[k];
			indexes[k] = tmp;
		}
	}

	// comparaters

	public int compare(int column, int row1, int row2) {
		Object o1 = getValueAt(row1, column);
		Object o2 = getValueAt(row2, column);
		if (o1 == null && o2 == null) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			Class type = getColumnClass(column);
			if (type.getSuperclass() == Number.class) {
				return compare((Number) o1, (Number) o2);
			} else if (type == String.class) {
				return ((String) o1).compareTo((String) o2);
			} else if (type == Date.class) {
				return compare((Date) o1, (Date) o2);
			} else if (type == Boolean.class) {
				return compare((Boolean) o1, (Boolean) o2);
			} else {
				return (o1.toString()).compareTo(o2.toString());
			}
		}
	}

	public int compare(Number o1, Number o2) {
		double n1 = o1.doubleValue();
		double n2 = o2.doubleValue();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	public int compare(Date o1, Date o2) {
		long n1 = o1.getTime();
		long n2 = o2.getTime();
		if (n1 < n2) {
			return -1;
		} else if (n1 > n2) {
			return 1;
		} else {
			return 0;
		}
	}

	public int compare(Boolean o1, Boolean o2) {
		boolean b1 = o1.booleanValue();
		boolean b2 = o2.booleanValue();
		if (b1 == b2) {
			return 0;
		} else if (b1) {
			return 1;
		} else {
			return -1;
		}
	}
}
