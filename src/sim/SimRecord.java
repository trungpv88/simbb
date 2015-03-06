package sim;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.util.Persistable;

public final class SimRecord implements Persistable{
	private long _date;
	private String _note;
	private int _id;

	SimRecord(String height, long date, int id) {
		_note = height;
		_date = date;
		_id = id;
	}
	
	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public long getDate() {
		return _date;
	}

	public void setDate(long _date) {
		this._date = _date;
	}

	public String getNote() {
		return _note;
	}

	public void setNote(String _height) {
		this._note = _height;
	}

	public String toString() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(_date));

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		StringBuffer buffer = new StringBuffer();

		buffer.append(day).append('/').append(month).append('/').append(year)
				.append(" : ").append(" ").append(_note);

		return buffer.toString();
	}
}
